package org.yuemi.mmomechanics.plugin.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.plugin.MmoMechanicsPlugin;
import org.yuemi.mmomechanics.plugin.skill.SkillManager;

public final class MmoPlaceholderExpansion extends PlaceholderExpansion {

    private final MmoMechanicsPlugin plugin;
    private final SkillManager skillManager;

    public MmoPlaceholderExpansion(@NotNull MmoMechanicsPlugin plugin, @NotNull SkillManager skillManager) {
        this.plugin = plugin;
        this.skillManager = skillManager;
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors());
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mmomechanics";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("version")) {
            return getVersion();
        }
        if (params.equalsIgnoreCase("skills_count")) {
            return String.valueOf(skillManager.getSkillNames().size());
        }
        return null;
    }
}
