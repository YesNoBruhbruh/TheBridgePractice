package me.maanraj514.state;

import me.maanraj514.state.listener.PlayingListenerProvider;
import me.maanraj514.state.listener.StateListenerProvider;

public class RoundPlayingState extends GameState{


    @Override
    public StateListenerProvider getListenerProvider() {
        return new PlayingListenerProvider();
    }
}
