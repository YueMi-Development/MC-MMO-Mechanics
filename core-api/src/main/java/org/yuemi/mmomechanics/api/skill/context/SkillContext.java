package org.yuemi.mmomechanics.api.skill.context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.trigger.Trigger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class SkillContext {
    private final Target caster;
    private final Trigger trigger;
    private final Map<String, Object> variables = new HashMap<>();

    public SkillContext(@NotNull Target caster, @NotNull Trigger trigger) {
        this.caster = Objects.requireNonNull(caster, "Caster cannot be null");
        this.trigger = Objects.requireNonNull(trigger, "Trigger cannot be null");
    }

    public @NotNull Target getCaster() {
        return caster;
    }

    public @NotNull Trigger getTrigger() {
        return trigger;
    }

    public void setVariable(@NotNull String name, @Nullable Object value) {
        variables.put(name, value);
    }

    public @NotNull Optional<Object> getVariable(@NotNull String name) {
        return Optional.ofNullable(variables.get(name));
    }

    public @NotNull Map<String, Object> getVariables() {
        return Map.copyOf(variables);
    }
}
