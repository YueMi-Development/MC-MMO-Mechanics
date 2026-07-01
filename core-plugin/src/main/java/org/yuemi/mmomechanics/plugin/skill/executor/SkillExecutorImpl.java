package org.yuemi.mmomechanics.plugin.skill.executor;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.executor.SkillExecutor;
import org.yuemi.mmomechanics.api.skill.mechanic.DelayMechanic;
import org.yuemi.mmomechanics.api.skill.mechanic.MechanicWrapper;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class SkillExecutorImpl implements SkillExecutor {

    private final JavaPlugin plugin;

    public SkillExecutorImpl(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run(
            @NotNull SkillContext context,
            @NotNull Collection<Target> initialTargets,
            @NotNull Collection<MechanicWrapper> mechanics
    ) {
        runStep(context, new ArrayList<>(mechanics), initialTargets, 0);
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
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                runStep(context, wrappers, currentTargets, index + 1);
            }, finalTicks);
            return;
        }

        Collection<Target> stepTargets = wrapper.targeter().getTargets(context);

        Collection<Target> filtered = stepTargets.stream()
                .filter(target -> wrapper.conditions().stream().allMatch(cond -> cond.test(context, target)))
                .collect(Collectors.toList());

        if (!filtered.isEmpty()) {
            wrapper.mechanic().execute(context, filtered);
        }

        runStep(context, wrappers, currentTargets, index + 1);
    }
}
