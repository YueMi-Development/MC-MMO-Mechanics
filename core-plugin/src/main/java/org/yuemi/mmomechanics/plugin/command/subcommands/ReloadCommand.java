package org.yuemi.mmomechanics.plugin.command.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.plugin.MmoMechanicsPlugin;
import org.yuemi.mmomechanics.plugin.command.SubCommand;
import org.yuemi.mmomechanics.plugin.skill.SkillManager;

import java.util.Collections;
import java.util.List;

public final class ReloadCommand implements SubCommand {

    private final MmoMechanicsPlugin plugin;
    private final SkillManager skillManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ReloadCommand(@NotNull MmoMechanicsPlugin plugin, @NotNull SkillManager skillManager) {
        this.plugin = plugin;
        this.skillManager = skillManager;
    }

    @Override
    public @NotNull String getName() {
        return "reload";
    }

    @Override
    public @NotNull String getDescription() {
        return "Reload configuration & skills";
    }

    @Override
    public @NotNull String getPermission() {
        return "mmomechanics.admin.reload";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        plugin.reloadConfig();
        skillManager.loadSkills();
        sender.sendMessage(miniMessage.deserialize("<green>MMO Mechanics configuration and skills reloaded successfully!</green>"));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
