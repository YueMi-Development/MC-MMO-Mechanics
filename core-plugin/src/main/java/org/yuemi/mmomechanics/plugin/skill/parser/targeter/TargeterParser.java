package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

public final class TargeterParser {

    public static @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        
        String base = clean;
        int bracketIndex = clean.indexOf('{');
        if (bracketIndex != -1) {
            base = clean.substring(0, bracketIndex).trim();
        }

        return switch (base) {
            case "self", "caster", "boss", "mob" -> new SelfParser().parse(clean);
            case "target", "t" -> new CasterTargetParser().parse(clean);
            case "trigger" -> new TriggerParser().parse(clean);
            case "nearestplayer" -> new NearestPlayerParser().parse(clean);
            case "ownerlocation" -> new OwnerLocationParser().parse(clean);
            case "owner", "wolfowner" -> new OwnerParser().parse(clean);
            case "parentlocation", "summonerlocation" -> new ParentLocationParser().parse(clean);
            case "parent", "summoner" -> new ParentParser().parse(clean);
            case "mount" -> new MountParser().parse(clean);
            case "passenger" -> new PassengerParser().parse(clean);
            case "playerbyname", "specificplayer" -> new PlayerByNameParser().parse(clean);
            case "uuid", "uniqueidentifier" -> new UuidParser().parse(clean);
            case "near" -> new NearParser().parse(clean);
            default -> null;
        };
    }
}
