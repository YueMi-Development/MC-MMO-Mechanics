package org.yuemi.mmomechanics.plugin;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.plugin.command.MmoCommand;
import org.yuemi.mmomechanics.plugin.command.subcommands.CastCommand;
import org.yuemi.mmomechanics.plugin.command.subcommands.ReloadCommand;
import org.yuemi.mmomechanics.plugin.config.migration.ConfigMigrator;
import org.yuemi.mmomechanics.plugin.config.migration.MigrationStep;
import org.yuemi.mmomechanics.plugin.config.migration.MigrationStep1To2;
import org.yuemi.mmomechanics.plugin.skill.SkillManager;

import java.io.File;
import java.util.List;

public final class MmoMechanicsPlugin extends JavaPlugin {

    private static final int LATEST_CONFIG_VERSION = 2;

    private MmoMechanicsApi api;
    private SkillManager skillManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        migrateConfig();

        // Load Skill Manager & load JSON5 skill configurations
        this.skillManager = new SkillManager(this);
        this.skillManager.loadSkills();

        // Register Command if enabled
        if (getConfig().getBoolean("features.command-enabled", true)) {
            MmoCommand cmd = new MmoCommand();
            cmd.registerSubCommand(new CastCommand(skillManager));
            cmd.registerSubCommand(new ReloadCommand(this, skillManager));
            var mmoCommand = getCommand("mmomechanics");
            if (mmoCommand != null) {
                mmoCommand.setExecutor(cmd);
                mmoCommand.setTabCompleter(cmd);
            }
        }

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
            List<MigrationStep> steps = List.of(new MigrationStep1To2());
            ConfigMigrator migrator = new ConfigMigrator(LATEST_CONFIG_VERSION, steps, getLogger());
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
