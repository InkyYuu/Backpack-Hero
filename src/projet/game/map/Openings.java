package projet.game.map;

public enum Openings {
	ZERO_ZERO_ZERO_UN(0, 0, 0, 1),
    ZERO_ZERO_UN_ZERO(0, 0, 1, 0),
    ZERO_UN_ZERO_ZERO(0, 1, 0, 0),
    UN_ZERO_ZERO_ZERO(1, 0, 0, 0),
    ZERO_ZERO_UN_UN(0, 0, 1, 1),
    ZERO_UN_ZERO_UN(0, 1, 0, 1),
    ZERO_UN_UN_ZERO(0, 1, 1, 0),
    UN_ZERO_ZERO_UN(1, 0, 0, 1),
    UN_ZERO_UN_ZERO(1, 0, 1, 0),
    UN_UN_ZERO_ZERO(1, 1, 0, 0),
    ZERO_UN_UN_UN(0, 1, 1, 1),
    UN_ZERO_UN_UN(1, 0, 1, 1),
    UN_UN_ZERO_UN(1, 1, 0, 1),
    UN_UN_UN_ZERO(1, 1, 1, 0),
    UN_UN_UN_UN(1, 1, 1, 1);

    private int[] opens;

    Openings(int... coords) {
        this.opens = coords;
    }

    public int[] getCoords() {
        return opens;
    }
}
