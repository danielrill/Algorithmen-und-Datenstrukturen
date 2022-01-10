import java.util.*;

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
        UnionFind union = new UnionFind(edges.size());
        PriorityQueue<TelVerbindung> verbindung = new PriorityQueue<>();
        optTelNet = new LinkedList<>();


        edges.keySet().forEach(startKnoten -> edges.keySet().forEach(endKnoten -> {
            if( startKnoten.equals(endKnoten) )
                return;

            TelVerbindung t = new TelVerbindung(startKnoten,endKnoten);

            if(t.cost <= this.lbg)
                verbindung.add(t);
        }));

        while(union.size() > 1 && !verbindung.isEmpty()) {
            TelVerbindung verb = verbindung.poll();

            int v = edges.get(verb.start);
            int w = edges.get(verb.end);

            int t1 = union.find(v);
            int t2 = union.find(w);

            if (t1 != t2) {
                union.union(t1, t2);
                optTelNet.add(verb);
            }
        }
        if (verbindung.isEmpty() && union.size() != 1 ) {
             optTelNet = null;
             return false;
        }
        return true;
    }

    /*
    Zeichnet das gefundene optimale Telefonnetz mit der Größe xMax*yMax in ein Fenster.
     */
    public void drawOptTelNet(int xMax, int yMax) {
        StdDraw.clear();

        StdDraw.setPenRadius(0.019);
        StdDraw.setPenColor(StdDraw.RED);

        // draw Vertexes
        edges.keySet().forEach(i -> StdDraw.point(i.x / (double) xMax, i.y / (double) yMax));

        // draw connection
        StdDraw.setPenRadius(0.0055);
        StdDraw.setPenColor(StdDraw.BLUE);
        optTelNet.forEach(i -> {
            StdDraw.line(
                i.start.x / (double) xMax,
                i.start.y / (double) yMax,
                i.end.x / (double) xMax,
                i.end.y / (double) yMax);

        });

        StdDraw.show(0);
    }

    /*
    Fügt n zufällige Telefonknoten zum Netz dazu mit x-Koordinate aus [0,xMax] und y-Koordinate aus [0,yMax].
     */
    public void generateRandomTelNet(int n , int xMax, int yMax) {
        if ( n<= 0 ||xMax <= 0 ||yMax <= 0) {
            throw new UnsupportedOperationException();
        }

        Random rand = new Random();
        for (int i = 0 ; i < n ; i ++ ) {
            int x = rand.nextInt(xMax);
            int y = rand.nextInt(yMax);

            if(!addTelKnoten(x, y))
                i --;
        }
    }

    /*
    Liefert ein optimales Telefonnetz als Liste von Telefonverbindungen zurück.
     */
    public List<TelVerbindung> getOptTelNet() {
        return optTelNet;
    }

    /*
    Liefert die Gesamtkosten eines optimalen Telefonnetzes zurück.
     */
    public int getOptTelNetKosten() {
        int cost=0;
        for (var a : optTelNet)
            cost += a.cost ;

        return cost;
    }

    /*
    Liefert die Gesamtkosten eines optimalen Telefonnetzes zurück.
     */
    public int size() {
        return edges.size();
    }

    public String toString() {
        return this.toString();
    }

    public static void main(String[] args) {
        TelNet testNet = new TelNet(7);

        testNet.addTelKnoten(1,1);
        testNet.addTelKnoten(3,1);
        testNet.addTelKnoten(4,2);
        testNet.addTelKnoten(7,5);
        testNet.addTelKnoten(2,6);
        testNet.addTelKnoten(4,7);
        testNet.addTelKnoten(3,4);

        testNet.computeOptTelNet();

        System.out.println("Kosten : " + testNet.getOptTelNetKosten());

        testNet.drawOptTelNet(7,7);

        //System.out.println("#### \tTeste randomNet\t #### \n\n");
        //testRandom();
    }

    public static void testRandom() {
        int n = 20;
        // 1k  *  1k
        int x = n, y = n;

        TelNet randomNet = new TelNet(20);

        randomNet.generateRandomTelNet(n, x, y);
        randomNet.computeOptTelNet();

        //System.out.println("Kosten : " + randomNet.getOptTelNetKosten());

        randomNet.drawOptTelNet(x, y);
    }
}
