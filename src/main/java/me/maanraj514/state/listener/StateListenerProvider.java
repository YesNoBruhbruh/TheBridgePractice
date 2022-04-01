package me.maanraj514.state.listener;

import lombok.Getter;
import me.maanraj514.BridgePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@Getter
public abstract class StateListenerProvider implements Listener {

    private BridgePlugin plugin;

    public void onEnable(BridgePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
