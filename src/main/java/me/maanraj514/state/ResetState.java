package me.maanraj514.state;

import me.maanraj514.BridgePlugin;
import me.maanraj514.state.listener.StateListenerProvider;
import org.bukkit.Bukkit;

public class ResetState extends GameState{

    @Override
    public void onEnable(BridgePlugin plugin) {
        super.onEnable(plugin);

        Bukkit.getServer().getScheduler().runTaskLater(plugin, Bukkit::shutdown, 20 * 6);
    }

    @Override
    public StateListenerProvider getListenerProvider() {
        return null;
    }
}
