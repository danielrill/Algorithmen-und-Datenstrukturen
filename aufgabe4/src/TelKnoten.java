

public class TelKnoten {
    public final int x, y;

    // Nummerierung für einzelne Knoten
    public int name;
    private static int counter = 0;


    public TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
        this.name = counter++;
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
