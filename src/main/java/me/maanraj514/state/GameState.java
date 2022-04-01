package me.maanraj514.state;

import lombok.Getter;
import me.maanraj514.BridgePlugin;
import me.maanraj514.managers.GameManager;
import me.maanraj514.state.listener.StateListenerProvider;

import javax.annotation.Nullable;

@Getter
public abstract class GameState {

    private BridgePlugin plugin;
    private GameManager gameManager;
    private StateListenerProvider listenerProvider;

    public void onEnable(BridgePlugin plugin){
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();

        listenerProvider = getListenerProvider();
        if (listenerProvider != null) listenerProvider.onEnable(plugin);
    }

    public void onDisable(){
        if (listenerProvider != null) listenerProvider.onDisable();
    }

    @Nullable
    public abstract StateListenerProvider getListenerProvider();
}
