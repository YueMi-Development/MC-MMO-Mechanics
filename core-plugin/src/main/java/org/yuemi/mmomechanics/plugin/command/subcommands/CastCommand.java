package org.yuemi.mmomechanics.plugin.command.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.trigger.Trigger;
import org.yuemi.mmomechanics.plugin.command.SubCommand;
import org.yuemi.mmomechanics.plugin.skill.SkillManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CastCommand implements SubCommand {

    private final SkillManager skillManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public CastCommand(@NotNull SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public @NotNull String getName() {
        return "cast";
    }

    @Override
    public @NotNull String getDescription() {
        return "Cast a skill on yourself";
    }

    @Override
    public @NotNull String getPermission() {
        return "mmomechanics.command.cast";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(miniMessage.deserialize("<red>Only players can cast skills.</red>"));
            return;
        }
        if (args.length < 1) {
            player.sendMessage(miniMessage.deserialize("<red>Usage: /mmo cast <skillName></red>"));
            return;
        }

        String skillName = args[0];
        Optional<Metaskill> skillOpt = skillManager.getSkill(skillName);
        if (skillOpt.isEmpty()) {
            player.sendMessage(miniMessage.deserialize("<red>Skill '" + skillName + "' not found.</red>"));
            return;
        }

        Metaskill skill = skillOpt.get();
        player.sendMessage(miniMessage.deserialize("<green>Casting skill: " + skill.getName() + "...</green>"));

        Trigger trigger = () -> "COMMAND";
        SkillContext context = new SkillContext(new EntityTarget(player), trigger);
        skill.execute(context, Collections.singletonList(context.getCaster()));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1) {
            return skillManager.getSkillNames().stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
