package org.yuemi.mmomechanics.plugin.config.migration;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yuemi.config.api.MigrationStep;

public class MigrationStep1To2 implements MigrationStep {

    @Override
    public int getTargetVersion() {
        return 2;
    }

    @Override
    public void migrate(@NotNull FileConfiguration configuration) {
        configuration.set("features.command-enabled", true);
    }
}
