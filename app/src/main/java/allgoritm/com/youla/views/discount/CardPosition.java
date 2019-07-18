package allgoritm.com.youla.views.discount;

enum CardPosition {
    TOP_LEFT(0),
    TOP_RIGHT(1),
    BOTTOM_LEFT(2),
    BOTTOM_RIGHT(3);

    private final int pos;
    CardPosition(int pos) {
        this.pos = pos;
    }

    public int getPosition() {
        return pos;
    }

    static CardPosition from(int pos) {
        for (CardPosition position : values()) {
            if (position.pos == pos) {
                return position;
            }
        }
        throw new IllegalArgumentException("unknown card position");
    }

    @Override
    public String toString() {
        return "CardPosition{" +
                "pos=" + name() +
                '}';
    }
}
