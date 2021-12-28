

public class TelKnoten {
    public final int x, y;

    public TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(TelKnoten a, TelKnoten b) {
        return a.x == b.x && a.y == b.y;
    }
    public boolean hashEquals(TelKnoten a, TelKnoten b) {
        return a.hashCode() == b.hashCode();
    }

    public int hashCode() {
        return (Integer.hashCode(x) + Integer.hashCode(y) * 7);
    }

    public String toString(TelKnoten a) {
        return "TelKnoten(" + "x= " + x + ", y=" + y + ')';
    }

}
