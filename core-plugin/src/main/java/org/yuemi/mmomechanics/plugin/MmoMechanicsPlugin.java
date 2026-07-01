package org.yuemi.mmomechanics.plugin;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.plugin.config.migration.ConfigMigrator;

import java.io.File;
import java.util.Collections;

public final class MmoMechanicsPlugin extends JavaPlugin {

    private static final int LATEST_CONFIG_VERSION = 1;

    private MmoMechanicsApi api;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        migrateConfig();

        this.api = new MmoMechanicsApiImpl();

        getServer().getServicesManager().register(
                MmoMechanicsApi.class,
                api,
                this,
                ServicePriority.Normal
        );
    }

    private void migrateConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (configFile.exists()) {
            ConfigMigrator migrator = new ConfigMigrator(LATEST_CONFIG_VERSION, Collections.emptyList());
            migrator.migrate(configFile);
            reloadConfig();
        }
    }

    @Override
    public void onDisable() {
        if (api != null) {
            getServer().getServicesManager().unregister(MmoMechanicsApi.class, api);
        }
    }
}
