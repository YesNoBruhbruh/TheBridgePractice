package me.maanraj514.state;

import me.maanraj514.BridgePlugin;
import me.maanraj514.state.listener.PregameListenerProvider;
import me.maanraj514.state.listener.StateListenerProvider;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class WaitingState extends GameState{

    private BukkitTask checkIfGameCanStartTask;

    @Override
    public void onEnable(BridgePlugin plugin) {
        super.onEnable(plugin);

        checkIfGameCanStartTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (getGameManager().canGameStart()) {
                getGameManager().setState(new StartingState());
            }
        }, 0, 0);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        checkIfGameCanStartTask.cancel();
    }

    @Override
    public StateListenerProvider getListenerProvider() {
        return new PregameListenerProvider();
    }
}
