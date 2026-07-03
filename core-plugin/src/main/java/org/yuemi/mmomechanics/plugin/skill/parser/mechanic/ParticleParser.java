package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.Particle;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.location.ParticleMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class ParticleParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        ParticleMechanic.Shape shape = ParticleMechanic.Shape.NORMAL;
        if (lower.startsWith("particlebox")) {
            shape = ParticleMechanic.Shape.BOX;
        } else if (lower.startsWith("particlering")) {
            shape = ParticleMechanic.Shape.RING;
        }

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        Particle particle = Particle.FLAME;
        String partStr = options.getOrDefault("particle", options.getOrDefault("p", options.getOrDefault("name", options.getOrDefault("n", "flame")))).toUpperCase();
        try {
            particle = Particle.valueOf(partStr);
        } catch (IllegalArgumentException ignored) {}

        int amount = 10;
        try {
            if (options.containsKey("amount")) amount = Integer.parseInt(options.get("amount"));
            else if (options.containsKey("a")) amount = Integer.parseInt(options.get("a"));
        } catch (NumberFormatException ignored) {}

        double speed = 0.1;
        try {
            if (options.containsKey("speed")) speed = Double.parseDouble(options.get("speed"));
            else if (options.containsKey("s")) speed = Double.parseDouble(options.get("s"));
        } catch (NumberFormatException ignored) {}

        double hSpread = 0.2;
        try {
            if (options.containsKey("hspread")) hSpread = Double.parseDouble(options.get("hspread"));
            else if (options.containsKey("hs")) hSpread = Double.parseDouble(options.get("hs"));
        } catch (NumberFormatException ignored) {}

        double vSpread = 0.2;
        try {
            if (options.containsKey("vspread")) vSpread = Double.parseDouble(options.get("vspread"));
            else if (options.containsKey("vs")) vSpread = Double.parseDouble(options.get("vs"));
        } catch (NumberFormatException ignored) {}

        double radius = 1.0;
        try {
            if (options.containsKey("radius")) radius = Double.parseDouble(options.get("radius"));
            else if (options.containsKey("r")) radius = Double.parseDouble(options.get("r"));
        } catch (NumberFormatException ignored) {}

        double size = 1.0;
        try {
            if (options.containsKey("size")) size = Double.parseDouble(options.get("size"));
            else if (options.containsKey("sz")) size = Double.parseDouble(options.get("sz"));
        } catch (NumberFormatException ignored) {}

        return new ParticleMechanic(particle, amount, speed, hSpread, vSpread, shape, radius, size);
    }
}
