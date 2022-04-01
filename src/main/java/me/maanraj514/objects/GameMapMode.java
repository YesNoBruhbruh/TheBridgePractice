package me.maanraj514.objects;

public enum GameMapMode {
    DUEL, TWO_V_TWO, FOUR_V_FOUR, FOUR_V_FOUR_V_FOUR_V_FOUR;

    public int getMaxTeamSize() {
        switch (this) {
            case DUEL: return 1;
            case TWO_V_TWO: return 2;
            case FOUR_V_FOUR:
            case FOUR_V_FOUR_V_FOUR_V_FOUR:
                return 4;
        }
        return 0;
    }

    public int getMaxPlayers() {
        switch (this) {
            case DUEL: return 2;
            case TWO_V_TWO: return getMaxTeamSize() * 2;
            case FOUR_V_FOUR:
            case FOUR_V_FOUR_V_FOUR_V_FOUR:
                return getMaxTeamSize() * 4;
        }

        return 0;
    }
}
