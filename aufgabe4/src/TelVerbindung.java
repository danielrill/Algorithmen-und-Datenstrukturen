

public class TelVerbindung implements Comparable<TelVerbindung> {
    int cost;
    TelKnoten start;
    TelKnoten end;

    public TelVerbindung(TelKnoten x, TelKnoten y, int c) {
        this.start = x;
        this.end = y;
        this.cost = c;
    }

    public TelVerbindung(TelKnoten x, TelKnoten y) {
        this.start = x;
        this.end = y;
        this.cost = Math.abs(start.x - end.y) + Math.abs(start.y - end.y);
    }

    public String toString() {
        return "Telverbindung [ " + "start =" + start + "end =" + end + "cost =" + cost + "]";
    }

    @Override
    public int compareTo(TelVerbindung o) {
        if (o == null)
            throw new ArrayStoreException();

        return cost - o.cost;
    }
}
