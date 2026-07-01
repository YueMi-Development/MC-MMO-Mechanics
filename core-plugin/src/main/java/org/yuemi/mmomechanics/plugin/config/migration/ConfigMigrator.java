package org.yuemi.mmomechanics.plugin.config.migration;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class ConfigMigrator {
    private final int latestVersion;
    private final List<MigrationStep> steps;
    private final Logger logger;

    public ConfigMigrator(int latestVersion, List<MigrationStep> steps, Logger logger) {
        this.latestVersion = latestVersion;
        this.steps = steps;
        this.logger = logger;
    }

    public void migrate(File configFile) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        int currentVersion = config.getInt("config-version", 1);
        
        if (currentVersion >= latestVersion) return;
        
        boolean modified = false;
        for (MigrationStep step : steps) {
            if (currentVersion == step.getTargetVersion() - 1) {
                int oldVersion = currentVersion;
                step.migrate(config);
                currentVersion = step.getTargetVersion();
                config.set("config-version", currentVersion);
                logger.info("Migrated " + configFile.getName() + " from version " + oldVersion + " to " + currentVersion);
                modified = true;
            }
        }
        
        if (modified) {
            try {
                config.save(configFile);
            } catch (Exception e) {
                logger.severe("Failed to save migrated configuration: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
