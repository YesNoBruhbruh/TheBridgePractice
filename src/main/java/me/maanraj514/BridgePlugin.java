package me.maanraj514;

import me.maanraj514.managers.GameManager;
import me.maanraj514.managers.MapConfigManager;
import me.maanraj514.managers.PlayerManager;
import me.maanraj514.state.MapSelectionState;
import me.maanraj514.utility.ChatUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BridgePlugin extends JavaPlugin implements Listener {

    private GameManager gameManager;
    private MapConfigManager mapConfigManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        this.gameManager = new GameManager(this);
        this.mapConfigManager = new MapConfigManager(this);
        this.playerManager = new PlayerManager(this);

        this.gameManager.setState(new MapSelectionState());
    }

    @Override
    public void onDisable() {
        if (gameManager != null) gameManager.onDisable();
    }

    @EventHandler
    private void onLogin(PlayerLoginEvent event) {
        if (Bukkit.getOnlinePlayers().size() >= getGameManager().getMaxPlayers()) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatUtility.colorize("&cSorry the game is full!"));
        }
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public MapConfigManager getMapConfigManager() {
        return mapConfigManager;
    }
}