package org.yuemi.mmomechanics.plugin.config.migration;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yuemi.config.api.MigrationStep;

public class MigrationStep2To3 implements MigrationStep {

    @Override
    public int getTargetVersion() {
        return 3;
    }

    @Override
    public void migrate(@NotNull FileConfiguration configuration) {
        // Add mmo-essence hook (enabled by default)
        configuration.set("hooks.mmo-essence", true);
    }
}
