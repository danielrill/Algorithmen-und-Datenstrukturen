// O. Bittel;
// 22.02.2017
package aufgabe2.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Klasse für Tiefensuche.
 *
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class DepthFirstOrder<V> {

    private final List<V> preOrder = new LinkedList<>();
    private final List<V> postOrder = new LinkedList<>();
    private final DirectedGraph<V> myGraph;
    private int numberOfDFTrees = 0;

    /**
     * Führt eine Tiefensuche für g durch.
     *
     * @param g gerichteter Graph.
     */
    public DepthFirstOrder(DirectedGraph<V> g) {
        myGraph = g;
        visit_DF();
    }


    void visit_DF(){
        // Besucht Set anlgegen
        Set<V> visited = new TreeSet<>();
        // Für jeden Knoten im Graphen
        for(V value: myGraph.getVertexSet()){
            // Wenn noch nicht besucht
            if(!visited.contains(value)){
                // Rekursiv weiter
                visit_DF_r(value,visited);
                numberOfDFTrees++;
            }
        }
    }


    void visit_DF_r(V value,Set<V> visited){

        // Knoten wurde besucht
        visited.add(value);
        // Knoten in preOrder hinzufügen
        preOrder.add(value);

        // für jeden Nachfolger des aktuellen Knotens
        for(var v2: myGraph.getSuccessorVertexSet(value)){
            // Wenn er noch nicht besucht wurde
            if(!visited.contains(v2)){
                // Rekursiv weiter
                visit_DF_r(v2,visited);
            }
        }
        // Knoten in postOrder hinzufügen
        postOrder.add(value);
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Pre-Order-Reihenfolge zurück.
     *
     * @return Pre-Order-Reihenfolge der Tiefensuche.
     */
    public List<V> preOrder() {
        return Collections.unmodifiableList(preOrder);
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Post-Order-Reihenfolge zurück.
     *
     * @return Post-Order-Reihenfolge der Tiefensuche.
     */
    public List<V> postOrder() {
        return Collections.unmodifiableList(postOrder);
    }

    /**
     *
     * @return Anzahl der Bäume des Tiefensuchwalds.
     */
    public int numberOfDFTrees() {
        return numberOfDFTrees;
    }

    public static void main(String[] args) {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(2, 5);
        g.addEdge(5, 1);
        g.addEdge(2, 6);
        g.addEdge(3, 7);
        g.addEdge(4, 3);
        g.addEdge(4, 6);
        //g.addEdge(7,3);
        g.addEdge(7, 4);

        DepthFirstOrder<Integer> dfs = new DepthFirstOrder<>(g);
        System.out.println(dfs.numberOfDFTrees());	// 2
        System.out.println(dfs.preOrder());		// [1, 2, 5, 6, 3, 7, 4]
        System.out.println(dfs.postOrder());		// [5, 6, 2, 1, 4, 7, 3]

    }

    /**
     * Klasse für Bestimmung aller strengen Komponenten.
     * Kosaraju-Sharir Algorithmus.
     * @author Oliver Bittel
     * @since 02.03.2020
     * @param <V> Knotentyp.
     */
    public static class StrongComponents<V> {
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
}