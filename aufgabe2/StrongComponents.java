// O. Bittel;
// 05-09-2018

package aufgabe2.src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 * @author Oliver Bittel
 * @since 02.03.2020
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {
    // comp speichert fuer jede Komponente die zughörigen Knoten.
    // Die Komponenten sind numeriert: 0, 1, 2, ...
    // Fuer Beispielgraph in Aufgabenblatt 2, Abb3:
    // Component 0: 5, 6, 7,
    // Component 1: 8,
    // Component 2: 1, 2, 3,
    // Component 3: 4,

    private final Map<Integer,Set<V>> comp = new TreeMap<>();
    private Set<V> visited = new TreeSet<>();
    private DirectedGraph<V> myGraph;
    private int teilbaum_counter = 0;

    /**
     * Ermittelt alle strengen Komponenten mit
     * dem Kosaraju-Sharir Algorithmus.
     * @param g gerichteter Graph.
     */
    public StrongComponents(DirectedGraph<V> g) {
        myGraph = g;
        ksa();
    }


    private List<V> reversePostOrder(){
        DepthFirstOrder<V> dfo = new DepthFirstOrder<>(myGraph);
        List<V> postOrder = new ArrayList<>(dfo.postOrder());
        Collections.reverse(postOrder);
        return postOrder;
    }


    private void ksa(){
        // umgekehrte Post Order Reihenfolge
        List<V> reverseGraph = reversePostOrder();
        // Graph invertieren
        DirectedGraph<V> inv = myGraph.invert();
        // Jeder Wert im Graphen
        for(V value : reverseGraph){
            // Schon besucht ?
            if(!visited.contains(value)){
                // Im ersten Teilbaum neues Set anlegen
                comp.put(teilbaum_counter,new TreeSet<>());
                // In neues Set den Knoten ablegen
                comp.get(teilbaum_counter).add(value);
                // Knoten wurde besucht
                visited.add(value);
                // Rekursiv zu den nächsten Nachfolgern
                ksa_r(value,inv);
                // Wenn keine Knoten mehr im Teilbaum dann Teilbaum counter erhöhen
                teilbaum_counter++;
            }
        }
    }

    private void ksa_r(V value, DirectedGraph<V> g){
        // Für alle Nachfolger von alten Knoten
        for(var p: g.getSuccessorVertexSet(value)){
            // Wenn noch nicht besucht
            if(!visited.contains(p)){
                // Im Teilbaum Knoten einfügen
                comp.get(teilbaum_counter).add(p);
                // Knoten wurde besucht
                visited.add(p);
                // Rekursiv weiter
                ksa_r(p,g);
            }
        }
    }

    /**
     *
     * @return Anzahl der strengen Komponeneten.
     */
    public int numberOfComp() {
        return comp.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for(var v: comp.entrySet()){
            sb.append("Component" + v.getKey() + " : ");
            for (var e: v.getValue()){
                sb.append(e + ", ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Liest einen gerichteten Graphen von einer Datei ein.
     * @param fn Dateiname.
     * @return gerichteter Graph.
     * @throws FileNotFoundException
     */
    public static DirectedGraph<Integer> readDirectedGraph(File fn) throws FileNotFoundException {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        Scanner sc = new Scanner(fn);
        sc.nextLine();
        sc.nextLine();
        while (sc.hasNextInt()) {
            int v = sc.nextInt();
            int w = sc.nextInt();
            g.addEdge(v, w);
        }
        return g;
    }

    private static void test1() {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1,2);
        g.addEdge(1,3);
        g.addEdge(2,1);
        g.addEdge(2,3);
        g.addEdge(3,1);

        g.addEdge(1,4);
        g.addEdge(5,4);

        g.addEdge(5,7);
        g.addEdge(6,5);
        g.addEdge(7,6);

        g.addEdge(7,8);
        g.addEdge(8,2);

        StrongComponents<Integer> sc = new StrongComponents<>(g);

        System.out.println(sc.numberOfComp());  // 4

        System.out.println(sc);
        // Component 0: 5, 6, 7,
        // Component 1: 8,
        // Component 2: 1, 2, 3,
        // Component 3: 4,
    }

    private static void test2() throws FileNotFoundException {
        DirectedGraph<Integer> g = readDirectedGraph(new File("mediumDG.txt"));
        System.out.println(g.getNumberOfVertexes());
        System.out.println(g.getNumberOfEdges());
        System.out.println(g);

        System.out.println("");

        StrongComponents<Integer> sc = new StrongComponents<>(g);
        System.out.println(sc.numberOfComp());  // 10
        System.out.println(sc);

    }

    public static void main(String[] args) throws FileNotFoundException {
        test1();
        //test2();
    }
}