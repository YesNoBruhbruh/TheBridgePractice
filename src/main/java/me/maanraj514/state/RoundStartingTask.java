package me.maanraj514.state;

import lombok.NoArgsConstructor;
import me.maanraj514.BridgePlugin;
import me.maanraj514.objects.runtime.GameTeam;
import me.maanraj514.state.listener.PregameListenerProvider;
import me.maanraj514.state.listener.StateListenerProvider;
import me.maanraj514.utility.ChatUtility;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;

@NoArgsConstructor
public class RoundStartingTask extends GameState{

    private GameTeam previousWinner = null;

    public RoundStartingTask(GameTeam previousWinner) {
        this.previousWinner = previousWinner;
    }

    private BukkitTask startTask;
    private int timeUntilStart = 5;

    @Override
    public void onEnable(BridgePlugin plugin) {
        super.onEnable(plugin);

        Optional<GameTeam> winner = getGameManager().getGameTeams().stream()
                .filter(team -> team.getScore() >= 5)
                .findFirst();

        if (winner.isPresent()) {
            getGameManager().setState(new GameOverState(winner.get()));
            return;
        }

        startTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (timeUntilStart <= 0) {
                getGameManager().setState(new RoundPlayingState());
                return;
            }

            Bukkit.getOnlinePlayers().forEach(player -> {
                String title = ChatUtility.colorize("&cThe match will start in " + timeUntilStart + "...");
                player.sendTitle(
                        title,
                        previousWinner != null ? previousWinner.getMapTeam().getName() + " scored (" + previousWinner.getScore() + "/5)!" : "",
                        0,
                        30,
                        timeUntilStart == 1 ? 20 : 0);
            });

            timeUntilStart--;
        }, 0, 20);

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setWalkSpeed(0);
            getPlugin().getPlayerManager().giveItems(player);
            getPlugin().getGameManager().getPlayerTeam(player).ifPresent(team -> player.teleport(team.getMapTeam().getSpawnLocation()));
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (startTask != null) startTask.cancel();

        Bukkit.getOnlinePlayers().forEach(player -> player.setWalkSpeed(0.2f));
    }

    @Override
    public StateListenerProvider getListenerProvider() {
        return new PregameListenerProvider();
    }
}
