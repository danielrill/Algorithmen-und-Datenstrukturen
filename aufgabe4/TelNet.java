import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TelNet {

    int lbg;    // Leitungsbegrenzungswert
    HashMap<TelKnoten, Integer> edges;
    List<TelVerbindung> optTelNet;

    /*
    Legt ein neues Telefonnetz mit dem Leitungsbegrenzungswert lbg an.
     */
    public TelNet( int lbg ) {
        this.lbg = lbg;
        this.edges = new HashMap<>();
    }

    /*
    Fügt einen neuen Telefonknoten mit Koordinate (x,y) dazu.
     */
    public boolean addTelKnoten(int x, int y) {
        TelKnoten node = new TelKnoten(x, y);
        Integer nodeEdges = edges.size();

        if(edges.containsKey(node))
            return false;
        else {
            optTelNet = null;
            edges.put(node, nodeEdges);
            return true;
        }
    }

    /*
    Berechnet ein optimales Telefonnetz als minimal aufspannenden Baum mit dem Algorithmus von Kruskal.
     */
    public boolean computeOptTelNet() {
        return true;
    }

    /*
    Zeichnet das gefundene optimale Telefonnetz mit der Größe xMax*yMax in ein Fenster.
     */
    public void drawOptTelNet(int n, int xMax, int yMax) {

    }

    /*
    Fügt n zufällige Telefonknoten zum Netz dazu mit x-Koordinate aus [0,xMax] und y-Koordinate aus [0,yMax].
     */
    public void generateRandomTelNet(int n , int xMax, int yMax) {

    }

    /*
    Liefert ein optimales Telefonnetz als Liste von Telefonverbindungen zurück.
     */
    public List<TelVerbindung> getOptTelNet() {
        List<TelVerbindung> list = new LinkedList<TelVerbindung>();

        return list;
    }

    /*
    Liefert die Gesamtkosten eines optimalen Telefonnetzes zurück.
     */
    public int getOptTelNetKosten() {

        return 0;
    }

    /*
    Liefert die Gesamtkosten eines optimalen Telefonnetzes zurück.
     */
    public int size() {
        return 0;
    }

    public String toString() {
        return this.toString();
    }

    public static void main(String[] args) {
        TelNet testNet = new TelNet(7);

        testNet.addTelKnoten(1,1);
        testNet.addTelKnoten(3,1);
        testNet.addTelKnoten(5,2);
        testNet.addTelKnoten(7,5);
        testNet.addTelKnoten(2,6);
        testNet.addTelKnoten(3,3);
        testNet.addTelKnoten(4,2);

        System.out.println("Kosten : " + testNet.getOptTelNetKosten());

        testNet.drawOptTelNet(7,7,7);

        //System.out.println("#### \tTeste randomNet\t #### \n\n");
        //testRandom();
    }

    public static void testRandom() {
        int n = 1000;
        // 1k  *  1k
        int x = n, y = n;

        TelNet randomNet = new TelNet(n/10);

        randomNet.generateRandomTelNet(n, x, y);
        randomNet.computeOptTelNet();

        System.out.println("Kosten : " + randomNet.getOptTelNetKosten());

        randomNet.drawOptTelNet(n, x, y);
    }
}
