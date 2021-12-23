// O. Bittel;
// 22.02.2017

import aufgabe2.src.AdjacencyListDirectedGraph;
import aufgabe2.src.DirectedGraph;

import java.util.*;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class TopologicalSort<V> {
	private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge



	/**
	 * Führt eine topologische Sortierung für g durch.
	 * @param g gerichteter Graph.
	 */
	public TopologicalSort(DirectedGraph<V> g) {
		ts = topSort(g);
	}


	List<V> topSort(DirectedGraph<V> g){
		// Anzahl noch nicht besuchter Vorgänger
		Map<V,Integer> inDegree = new TreeMap<>();
		// Queue für noch nicht besuchte Knoten
		Queue<V> queue = new LinkedList<>();

		// Für jeden Knoten
		for(var v: g.getVertexSet()){
			// Anzahl Vorgänger ablgegen
			inDegree.put(v, g.getInDegree(v));

			// Wenn keine Vorgänger mehr dann knoten in die queue hinzufügen
			if(inDegree.get(v) == 0){
				queue.add(v);
			}
		}

		// Solange bis es keine nicht besuchten Vorgänger mehr gibt
		while(!queue.isEmpty()){
			// Löschen da Knoten nun besucht und in top. Liste eingefügt
			V v = queue.remove();
			ts.add(v);
			// Für jeden Nachfolger des jetzigen Knotens
			for(var w : g.getSuccessorVertexSet(v)){
				// Vorgänger um eins verringern
				inDegree.put(w,inDegree.get(w)-1);
				// Wenn keine Vorgänger mehr vorhanden dann in queue hinzufügen
				if(inDegree.get(w) == 0){
					queue.add(w);
				}
			}
		}



		// Zirkulär wenn top. sortierte Liste nicht so viele Einträge hat wie es Knoten im Graphen gibt
		if(ts.size() != g.getNumberOfVertexes()){
			// Leere LinkedList ausgeben
			List<V> nullList = new LinkedList<>();
			return nullList;
		} else {
			// top. Liste ausgeben
			return ts;
		}
	}

	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück,
	 * die topologisch sortiert ist.
	 * @return topologisch sortierte Liste
	 */
	public List<V> topologicalSortedList() {
		return Collections.unmodifiableList(ts);
	}


	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		g.addEdge(4, 6);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		System.out.println(g);

		TopologicalSort<Integer> ts = new TopologicalSort<>(g);

		if (ts.topologicalSortedList() != null) {
			System.out.println(ts.topologicalSortedList()); // [1, 2, 3, 4, 5, 6, 7]
		}
	}
}