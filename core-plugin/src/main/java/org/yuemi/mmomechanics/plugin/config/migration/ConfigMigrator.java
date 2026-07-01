package org.yuemi.mmomechanics.plugin.config.migration;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.List;

public class ConfigMigrator {
    private final int latestVersion;
    private final List<MigrationStep> steps;

    public ConfigMigrator(int latestVersion, List<MigrationStep> steps) {
        this.latestVersion = latestVersion;
        this.steps = steps;
    }

    public void migrate(File configFile) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        int currentVersion = config.getInt("config-version", 1);
        
        if (currentVersion >= latestVersion) return;
        
        boolean modified = false;
        for (MigrationStep step : steps) {
            if (currentVersion == step.getTargetVersion() - 1) {
                step.migrate(config);
                currentVersion = step.getTargetVersion();
                config.set("config-version", currentVersion);
                modified = true;
            }
        }
        
        if (modified) {
            try {
                config.save(configFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
