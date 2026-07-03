package org.yuemi.mmomechanics.api.skill.context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.trigger.Trigger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Context wrapper passed during skill executions, containing the caster, the trigger, and local variables.
 */
public final class SkillContext {
    private final Target caster;
    private final Trigger trigger;
    private final Map<String, Object> variables = new HashMap<>();

    /**
     * Constructs a SkillContext.
     *
     * @param caster the casting entity/location target
     * @param trigger the trigger event causing this skill run
     */
    public SkillContext(@NotNull Target caster, @NotNull Trigger trigger) {
        this.caster = Objects.requireNonNull(caster, "Caster cannot be null");
        this.trigger = Objects.requireNonNull(trigger, "Trigger cannot be null");
    }

    /**
     * Gets the caster of the skill.
     *
     * @return the caster target
     */
    public @NotNull Target getCaster() {
        return caster;
    }

    /**
     * Gets the trigger event context of the skill.
     *
     * @return the trigger context
     */
    public @NotNull Trigger getTrigger() {
        return trigger;
    }

    /**
     * Sets a context-local variable.
     *
     * @param name the variable name
     * @param value the variable value, or null to clear it
     */
    public void setVariable(@NotNull String name, @Nullable Object value) {
        variables.put(name, value);
    }

    /**
     * Gets a context-local variable.
     *
     * @param name the variable name
     * @return an Optional containing the variable value, or empty if not set
     */
    public @NotNull Optional<Object> getVariable(@NotNull String name) {
        return Optional.ofNullable(variables.get(name));
    }

    /**
     * Gets a read-only copy of all context variables.
     *
     * @return a read-only map of variables
     */
    public @NotNull Map<String, Object> getVariables() {
        return Map.copyOf(variables);
    }
}
