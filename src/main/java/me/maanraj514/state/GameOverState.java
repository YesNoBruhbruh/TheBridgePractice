package me.maanraj514.state;

import lombok.RequiredArgsConstructor;
import me.maanraj514.BridgePlugin;
import me.maanraj514.objects.runtime.GameTeam;
import me.maanraj514.state.listener.PregameListenerProvider;
import me.maanraj514.state.listener.StateListenerProvider;
import me.maanraj514.utility.ChatUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@RequiredArgsConstructor
public class GameOverState extends GameState{

    private final GameTeam winner;

    @Override
    public void onEnable(BridgePlugin plugin) {
        super.onEnable(plugin);

        Bukkit.broadcastMessage(ChatUtility.colorize("&a" + winner.getMapTeam().getName() + " wins!"));

        World world = Bukkit.getWorld("world");
        Location loc = world.getSpawnLocation();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> player.teleport(loc));
            getGameManager().setState(new ResetState());
        }, 20 * 8);
    }

    @Override
    public StateListenerProvider getListenerProvider() {
        return new PregameListenerProvider();
    }
}