package org.yuemi.mmomechanics.plugin;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;

public final class MmoMechanicsPlugin extends JavaPlugin {

    private MmoMechanicsApi api;

    @Override
    public void onEnable() {
        this.api = new MmoMechanicsApiImpl();

        getServer().getServicesManager().register(
                MmoMechanicsApi.class,
                api,
                this,
                ServicePriority.Normal
        );
    }

    @Override
    public void onDisable() {
        if (api != null) {
            getServer().getServicesManager().unregister(MmoMechanicsApi.class, api);
        }
    }
}
