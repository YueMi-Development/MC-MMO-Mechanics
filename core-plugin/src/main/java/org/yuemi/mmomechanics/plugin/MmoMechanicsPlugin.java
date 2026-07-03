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
import org.yuemi.mmomechanics.plugin.skill.executor.SkillExecutorImpl;
import org.yuemi.mmomechanics.plugin.skill.trigger.TriggerBindingManager;
import org.yuemi.mmomechanics.plugin.skill.trigger.TriggerManager;
import org.yuemi.mmomechanics.plugin.skill.trigger.TimerManager;
import org.yuemi.mmomechanics.plugin.skill.trigger.TriggerRegistration;

import java.io.File;
import java.util.List;

public final class MmoMechanicsPlugin extends JavaPlugin {

    private static final int LATEST_CONFIG_VERSION = 2;

    private MmoMechanicsApi api;
    private SkillManager skillManager;
    private TriggerBindingManager triggerBindingManager;
    private TriggerManager triggerManager;
    private TimerManager timerManager;
    private boolean placeholderApiEnabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        migrateConfig();

        // Load Skill Manager & load JSON5 skill configurations
        this.skillManager = new SkillManager(this);
        this.skillManager.loadSkills();

        // Initialize Triggers
        this.triggerBindingManager = new TriggerBindingManager(this, skillManager);
        this.triggerManager = new TriggerManager(triggerBindingManager);
        this.timerManager = new TimerManager(this, triggerBindingManager, triggerManager);
        this.timerManager.start();

        new TriggerRegistration(this, triggerManager, timerManager).register();

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

        this.placeholderApiEnabled = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")
                && getConfig().getBoolean("hooks.placeholder-api", true);

        if (this.placeholderApiEnabled) {
            new org.yuemi.mmomechanics.plugin.hook.MmoPlaceholderExpansion(this, skillManager).register();
            getLogger().info("Successfully hooked into PlaceholderAPI!");
        }

        this.api = new MmoMechanicsApiImpl(this, new SkillExecutorImpl(this));

        getServer().getServicesManager().register(
                MmoMechanicsApi.class,
                api,
                this,
                ServicePriority.Normal
        );
    }

    public boolean isPlaceholderApiEnabled() {
        return placeholderApiEnabled;
    }

    public SkillManager getSkillManager() {
        return skillManager;
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
        if (timerManager != null) {
            timerManager.stop();
        }
        if (api != null) {
            getServer().getServicesManager().unregister(MmoMechanicsApi.class, api);
        }
    }
}
