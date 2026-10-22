package org.yuemi.mmomechanics.plugin.skill.executor;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.folia.FoliaSchedulers;
import org.yuemi.mmomechanics.api.skill.context.CasterSnapshot;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.executor.SkillExecutor;
import org.yuemi.mmomechanics.api.skill.mechanic.DelayMechanic;
import org.yuemi.mmomechanics.api.skill.mechanic.MechanicWrapper;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class SkillExecutorImpl implements SkillExecutor {

    private final JavaPlugin plugin;
    private final TargetDispatcher dispatcher;

    public SkillExecutorImpl(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.dispatcher = new TargetDispatcher(plugin);
        TargetDispatcher.setInstance(this.dispatcher);
    }

    @Override
    public void run(
            @NotNull SkillContext context,
            @NotNull Collection<Target> initialTargets,
            @NotNull Collection<MechanicWrapper> mechanics
    ) {
        captureCasterSnapshot(context);
        runStep(context, new ArrayList<>(mechanics), initialTargets, 0);
    }

    /**
     * Captures the caster's position, direction and base damage on the caster's own thread.
     *
     * <p>Mechanics may later run on the region thread of a target in another region, where
     * reading the caster's live state would be a cross-region access.
     */
    private void captureCasterSnapshot(@NotNull SkillContext context) {
        if (context.getCasterSnapshot() != null) {
            return;
        }

        Location casterLocation = context.getCaster().getLocation();
        if (casterLocation.getWorld() == null) {
            return;
        }

        Vector direction = casterLocation.getDirection().clone();
        Double baseAttackDamage = null;

        Entity casterEntity = context.getCaster().getAsEntity();
        if (casterEntity instanceof LivingEntity livingCaster) {
            AttributeInstance attribute = livingCaster.getAttribute(Attribute.ATTACK_DAMAGE);
            if (attribute != null) {
                baseAttackDamage = attribute.getValue();
            }
        }

        context.setCasterSnapshot(new CasterSnapshot(casterLocation, direction, baseAttackDamage));
    }

    private void runStep(
            @NotNull SkillContext context,
            @NotNull List<MechanicWrapper> wrappers,
            @NotNull Collection<Target> currentTargets,
            int index
    ) {
        if (index >= wrappers.size()) {
            return;
        }

        MechanicWrapper wrapper = wrappers.get(index);

        if (wrapper.mechanic() instanceof DelayMechanic delayMechanic) {
            long ticks = 20;
            org.yuemi.mmomechanics.api.MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(org.yuemi.mmomechanics.api.MmoMechanicsApi.class);
            if (api != null) {
                String parsed = api.parsePlaceholders(context.getCaster(), delayMechanic.getExpression());
                try {
                    if (delayMechanic.isSeconds()) {
                        ticks = (long) (Double.parseDouble(parsed) * 20);
                    } else {
                        ticks = Long.parseLong(parsed);
                    }
                } catch (NumberFormatException ignored) {}
            }
            long finalTicks = ticks;
            Runnable continuation = () -> runStep(context, wrappers, currentTargets, index + 1);
            Entity caster = context.getCaster().getAsEntity();
            if (caster != null) {
                FoliaSchedulers.runEntityDelayed(plugin, caster, continuation, finalTicks, 0L);
            } else {
                FoliaSchedulers.runRegionDelayed(plugin, context.getCaster().getLocation(), continuation, finalTicks);
            }
            return;
        }

        Collection<Target> stepTargets = wrapper.targeter().getTargets(context);

        if (stepTargets.isEmpty()) {
            runStep(context, wrappers, currentTargets, index + 1);
            return;
        }

        if (!dispatcher.isFolia()) {
            applyInline(context, wrapper, stepTargets);
            runStep(context, wrappers, currentTargets, index + 1);
            return;
        }

        applyDispatched(context, wrappers, currentTargets, index, wrapper, stepTargets);
    }

    private void applyInline(
            @NotNull SkillContext context,
            @NotNull MechanicWrapper wrapper,
            @NotNull Collection<Target> stepTargets
    ) {
        Collection<Target> filtered = stepTargets.stream()
                .filter(target -> wrapper.conditions().stream().allMatch(cond -> cond.test(context, target)))
                .collect(Collectors.toList());

        if (!filtered.isEmpty()) {
            wrapper.mechanic().execute(context, filtered);
        }
    }

    private void applyDispatched(
            @NotNull SkillContext context,
            @NotNull List<MechanicWrapper> wrappers,
            @NotNull Collection<Target> currentTargets,
            int index,
            @NotNull MechanicWrapper wrapper,
            @NotNull Collection<Target> stepTargets
    ) {
        AtomicInteger remaining = new AtomicInteger(stepTargets.size());

        for (Target target : stepTargets) {
            boolean scheduled = dispatcher.dispatch(target, () -> {
                try {
                    if (dispatcher.isUsable(target)
                            && wrapper.conditions().stream().allMatch(cond -> cond.test(context, target))) {
                        wrapper.mechanic().execute(context, Collections.singletonList(target));
                    }
                } finally {
                    if (remaining.decrementAndGet() == 0) {
                        runStep(context, wrappers, currentTargets, index + 1);
                    }
                }
            });

            if (!scheduled && remaining.decrementAndGet() == 0) {
                runStep(context, wrappers, currentTargets, index + 1);
            }
        }
    }
}
