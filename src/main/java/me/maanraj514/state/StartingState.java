package me.maanraj514.state;

import me.maanraj514.BridgePlugin;
import me.maanraj514.objects.MapTeam;
import me.maanraj514.objects.runtime.GameTeam;
import me.maanraj514.state.listener.PregameListenerProvider;
import me.maanraj514.state.listener.StateListenerProvider;
import me.maanraj514.utility.ChatUtility;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class StartingState extends GameState{

    private BukkitTask startingTask;
    private int timeUntilStart = 5;

    @Override
    public void onEnable(BridgePlugin plugin) {
        super.onEnable(plugin);

        startingTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!getGameManager().canGameStart()){
                Bukkit.broadcastMessage(ChatUtility.colorize("&cNot enough players! cancelling start."));
                getGameManager().setState(new WaitingState());
                return;
            }

            if (timeUntilStart <= 0) {
                getGameManager().setState(new RoundStartingTask());
            }else{
                Bukkit.broadcastMessage(ChatUtility.colorize("&aStarting in " + timeUntilStart + "..."));
            }

            timeUntilStart--;
        }, 0, 20);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (startingTask != null) startingTask.cancel();

        Set<GameTeam> gameTeams = new HashSet<>();

        for (MapTeam team : getGameManager().getLoadedMap().getTeams()) {
            GameTeam gameTeam = new GameTeam(team);

            AtomicInteger slots = new AtomicInteger(getGameManager().getLoadedMap().getGameMapMode().getMaxTeamSize());

            Bukkit.getOnlinePlayers()
                    .stream().filter(player -> {
                        return !getGameManager().getPlayerTeam(player).isPresent();
                    }).forEach(player -> {
                        if (slots.get() > 0) {
                            slots.getAndDecrement();
                            gameTeam.getPlayers().add(player.getUniqueId());
                        }
                    });

                    gameTeams.add(gameTeam);
        }

        getGameManager().setGameTeams(gameTeams);
    }

    @Override
    public StateListenerProvider getListenerProvider() {
        return new PregameListenerProvider();
    }
}
