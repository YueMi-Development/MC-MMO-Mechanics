package org.yuemi.mmomechanics.plugin.skill.mechanic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class MechanicRegistry {

    private static final Map<String, Function<String, Mechanic>> REGISTRY = new HashMap<>();

    public static void register(@NotNull String name, @NotNull Function<String, Mechanic> parser) {
        REGISTRY.put(name.toLowerCase().trim(), parser);
    }

    public static @Nullable Mechanic parse(@NotNull String name) {
        String clean = name.trim();
        String base = clean;
        int bracketIndex = clean.indexOf('{');
        if (bracketIndex != -1) {
            base = clean.substring(0, bracketIndex).trim();
        }
        Function<String, Mechanic> parser = REGISTRY.get(base.toLowerCase());
        if (parser != null) {
            return parser.apply(clean);
        }
        return null;
    }
}
