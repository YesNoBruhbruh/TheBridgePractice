package me.maanraj514.objects.runtime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.maanraj514.objects.MapTeam;
import me.maanraj514.state.GameState;
import me.maanraj514.state.listener.PlayingListenerProvider;
import me.maanraj514.state.listener.StateListenerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GameTeam extends GameState {

    private final MapTeam mapTeam;
    private List<UUID> players = new ArrayList<>();
    @Setter int score = 0;

    @Override
    public StateListenerProvider getListenerProvider() {
        return new PlayingListenerProvider();
    }
}
