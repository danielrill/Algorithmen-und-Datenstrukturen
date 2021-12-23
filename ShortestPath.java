// O. Bittel;
// 01.04.2021


package aufgabe3.src;


import aufgabe2.src.AdjacencyListDirectedGraph;
import aufgabe2.src.DirectedGraph;
import aufgabe3.src.SYSimulation.SYSimulation;
import aufgabe3.src.SYSimulation.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {

	private DirectedGraph<V> graph = new AdjacencyListDirectedGraph<>();
	private Heuristic<V> h;
	IndexMinPQ<V,Double> cand;

	private SYSimulation sim;

	private Map<V, Double> distance = new TreeMap<V,Double>();
	private Map<V, V> predecessor = new TreeMap<V,V>();

	V start;
	V ziel;

	double INFINITE_DOUBLE = Double.MAX_VALUE;

	/*
	private HashMap<V, Double> openList;
	private Set<V> closedList;
	private List<V> vertexList = new LinkedList<>();
	 */

	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch
	 * mit dem Dijkstra-Verfahren.
	 *
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 *          dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) throws IOException {
		this.graph = g;
		this.h = h;
		cand = new IndexMinPQ<>();
	}
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		shortestPath(s, g, graph, distance, predecessor);
	}


	boolean shortestPath(V s, V z, DirectedGraph<V> g, Map<V, Double> dist, Map<V, V> predecessor) {
		// flush cand ; init vars
		cand.clear();
		LinkedList<V> candidates = new LinkedList<>();
		start = s;
		ziel = z;

		// Save all values
		for (var v : g.getVertexSet()) {
			dist.put(v, INFINITE_DOUBLE);
			predecessor.put(v, null);
		}

		// Init input with starting distance 0
		dist.put(s, 0.0);
		candidates.add(s);

		while (!candidates.isEmpty()) {
			V minV = s;
			double minDist = INFINITE_DOUBLE;

			for (var v: candidates) {
				if (h == null) {
					if (dist.get(v) < minDist) {
						minDist = dist.get(v);
						minV = v;
					}// alternativ minValue (linkedlist)

				} else {
					if ((dist.get(v) + h.estimatedCost(v, z)) < minDist) {
						minDist = dist.get(v) + h.estimatedCost(v, z);
						minV = v;
					}
				}
				if (v.equals(z)) {
					cand.add(v,dist.get(v));
					System.out.println("Besuche Knoten " + v + " mit d: " + dist.get(v));
					return true;
				}
			}

			candidates.remove(minV);
			V v = minV;

			System.out.println("Besuche Knoten " + v + " mit d: " + dist.get(v));

			cand.add(minV, dist.get(minV));

			for (V w : g.getSuccessorVertexSet(v)) {
				if (dist.get(w) == INFINITE_DOUBLE) {
					candidates.add(w);
				}
				if (dist.get(v) + g.getWeight(v, w) < dist.get(w)) {
					predecessor.put(w, v);
					dist.put(w, dist.get(v) + g.getWeight(v, w));
				}
			}
		}
		return false;
	}
	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		List<V> path = new LinkedList<>();
		V u = this.ziel;

		path.add(this.ziel);
		while(this.predecessor.get(u) != null) {
			u = this.predecessor.get(u);
			path.add(0, u);
		}

		return path;
	}



	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		double length = 0;
		V last = null;
		List<V> path = this.getShortestPath();

		for(V node : path) {
			if(last != null)
				length += this.graph.getWeight(last, node);

			last = node;
		}

		return length;
	}

/*
	private void dijkstra() {
		this.vertexList = graph.getVertexList();
		while(this.vertexList.size() > 0) {
			V u = this.vertexList.stream()
					.min((o1, o2) -> Double.compare(this.distance.get(o1), this.distance.get(o2))).get();
			this.vertexList.remove(u);

			if(sim != null && u instanceof Integer)
				sim.visitStation((Integer) u, Color.cyan);

			for(Object v : this.graph.getAdjacentVertexList(u))
				distanceUpdate(u, (V) v);

			if(u.equals(this.ziel))
				break;
		}
	}
	private void distanceUpdate(V u, V v) {
		double alt = this.distance.get(u) + this.graph.getWeight(u, v);

		if(alt < this.distance.get(v)) {
			this.distance.put(v, alt);
			this.predecessor.put(v, u);
		}
	}

	private void aStar() {
		while(this.openList.size() > 0) {
			V currentNode = this.openList.entrySet().stream()
					.min((o1, o2) -> Double.compare(o1.getValue(), o2.getValue())).get().getKey();
			this.openList.remove(currentNode);

			if(currentNode.equals(this.ziel))
				break;

			this.closedList.add(currentNode);

			if(sim != null && currentNode instanceof Integer)
				sim.visitStation((Integer) currentNode, Color.cyan);

			this.expandNode(currentNode);
		}
	}
	private void expandNode(V currentNode) {
		this.graph.getAdjacentVertexList(currentNode).stream()
				.filter(successor -> !this.closedList.contains(successor))
				.forEach(successor ->
				{
					double alt = this.distance.get(currentNode) + this.graph.getWeight(currentNode, (V) successor);

					if(this.openList.containsKey(successor) && alt >= this.distance.get(successor))
						return;

					this.predecessor.put((V)successor, currentNode);
					this.distance.put((V)successor, alt);

					alt += this.h.estimatedCost((V)successor, this.ziel);
					this.openList.put((V)successor, alt);
				});
	}
*/

}

