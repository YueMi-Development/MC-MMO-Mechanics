package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;

public final class MechanicParser {

    public static @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        String base = clean;
        int bracketIndex = clean.indexOf('{');
        if (bracketIndex != -1) {
            base = clean.substring(0, bracketIndex).trim();
        }

        return switch (base.toLowerCase()) {
            /**
             * System Mechanic
             */
            case "delay" -> new DelayParser().parse(clean);
            case "skill", "cast", "metaskill" -> new SkillParser().parse(clean);

            /**
             * Effect Mechanic
             */
            case "lightning" -> new LightningParser().parse(clean);

            /**
             * Message Mechanic
             */
            case "message", "msg", "jsonmessage" -> new MessageParser().parse(clean);
            case "sendtitle", "title" -> new SendTitleParser().parse(clean);
            case "sendactionmessage", "actionbar" -> new SendActionMessageParser().parse(clean);
            
            /**
             * Player
             */
            case "feed" -> new FeedParser().parse(clean);
            case "closeinventory" -> new CloseInventoryParser().parse(clean);
            case "setgamemode", "gamemode" -> new SetGameModeParser().parse(clean);

            /**
             * General Entity Mechanics
             */
            case "damage", "basedamage", "percentdamage" -> new DamageParser().parse(clean);
            case "heal", "healpercent" -> new HealParser().parse(clean);
            case "potion", "potionclear" -> new PotionParser().parse(clean);
            case "velocity", "directionalvelocity", "throw", "propel" -> new VelocityParser().parse(clean);
            case "ignite", "extinguish" -> new IgniteParser().parse(clean);

            /**
             * Specific Entity Mechanics
             */
            case "addtrade" -> new AddTradeParser().parse(clean);
            case "animatearmorstand" -> new AnimateArmorStandParser().parse(clean);
            case "posearmorstand" -> new PoseArmorStandParser().parse(clean);
            case "armanimation", "swingoffhand" -> new ArmAnimationParser().parse(clean);
            case "barcreate", "barremove", "barset" -> new BossBarParser().parse(clean);
            case "equip", "equipcopy" -> new EquipParser().parse(clean);
            case "setname" -> new SetNameParser().parse(clean);
            case "togglesitting", "wolfsit" -> new ToggleSittingParser().parse(clean);
            case "setmobcolor" -> new SetMobColorParser().parse(clean);

            /**
             * Block Mechanics
             */
            case "breakblock", "breakblockandgiveitem" -> new BreakBlockParser().parse(clean);
            case "setblocktype" -> new SetBlockTypeParser().parse(clean);
            case "setblockopen" -> new SetBlockOpenParser().parse(clean);
            case "togglelever", "pushbutton" -> new ToggleLeverParser().parse(clean);
            case "pushblock" -> new PushBlockParser().parse(clean);
            case "blockdestabilize" -> new BlockDestabilizeParser().parse(clean);
            case "blockmask", "blockunmask" -> new BlockMaskParser().parse(clean);
            case "blockphysics" -> new BlockPhysicsParser().parse(clean);
            case "fillchest" -> new FillChestParser().parse(clean);
            case "spring" -> new SpringParser().parse(clean);

            /**
             * Location/World Mechanics
             */
            case "sound", "stopsound" -> new SoundParser().parse(clean);
            case "explosion", "fakeexplosion" -> new ExplosionParser().parse(clean);
            case "weather" -> new WeatherParser().parse(clean);
            case "time" -> new TimeParser().parse(clean);
            case "command", "cmd" -> new CommandParser().parse(clean);
            case "particle", "particlebox", "particlering" -> new ParticleParser().parse(clean);

            default -> null;
        };
    }
}


