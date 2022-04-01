package me.maanraj514.managers;

import dev.jcsoftware.jscoreboards.JPerPlayerMethodBasedScoreboard;
import lombok.Getter;
import lombok.Setter;
import me.maanraj514.BridgePlugin;
import me.maanraj514.objects.GameMap;
import me.maanraj514.objects.runtime.GameTeam;
import me.maanraj514.state.GameState;
import me.maanraj514.state.RoundPlayingState;
import me.maanraj514.state.RoundStartingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public class GameManager implements Listener {

    private final BridgePlugin plugin;
    @Setter @Getter private GameMap loadedMap;
    private GameState state;

    @Setter @Getter Set<GameTeam> gameTeams = new HashSet<>();

    private JPerPlayerMethodBasedScoreboard scoreboard;

    public GameManager(BridgePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getOnlinePlayers().forEach(player -> {
            scoreboard.setTitle(player, "&e&lBRIDGES");
            if (state instanceof RoundStartingTask || state instanceof RoundPlayingState){
                scoreboard.setLines(player, Arrays.asList("", "&a" + getPlayerTeam(player).get().getMapTeam().getName(), ""));
            }
        });
    }

    public void onDisable() {
    }

    public boolean canGameStart() {
        if (loadedMap == null) return false;

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int requiredPlayers;

        switch (loadedMap.getGameMapMode()) {
            case DUEL:
                requiredPlayers = 2;
                break;
            case TWO_V_TWO:
                requiredPlayers = 4;
                break;
            case FOUR_V_FOUR:
                requiredPlayers = 8;
                break;
            case FOUR_V_FOUR_V_FOUR_V_FOUR:
                requiredPlayers = 16;
                break;
            default: return false;
        }

        return onlinePlayers >= requiredPlayers;
    }

    public void setState(GameState state) {
        if (this.state != null) this.state.onDisable();
        this.state = state;
        this.state.onEnable(plugin);
    }

    public Optional<GameTeam> getPlayerTeam(Player player) {
        return gameTeams.stream().filter(team -> {
            return team.getPlayers().contains(player.getUniqueId());
        }).findAny();
    }

    public int getMaxPlayers() {
        if (getLoadedMap() == null) return 0;
        return getLoadedMap().getGameMapMode().getMaxPlayers();
    }

}
