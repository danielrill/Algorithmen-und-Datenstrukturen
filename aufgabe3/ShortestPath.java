// O. Bittel;
// 01.04.2021




import sim.SYSimulation;

import java.util.*;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ
	DirectedGraph<V> graph;
	Heuristic<V> h;
	V start;
	V ziel;
	double INFINITE_DOUBLE = Double.MAX_VALUE;


	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		this.h = h;
		graph = g;
	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
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
		shortestPath(s, g, graph, dist, pred);
	}


	boolean shortestPath(V s, V z, DirectedGraph<V> g, Map<V, Double> dist, Map<V, V> pred) {
		// flush cand ; init vars
		cand.clear();
		LinkedList<V> candidates = new LinkedList<>();
		start = s;
		ziel = z;

		// Save all values
		for (var v : g.getVertexSet()) {
			dist.put(v, INFINITE_DOUBLE);
			pred.put(v, null);
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
					}

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
					pred.put(w, v);
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
		while(this.pred.get(u) != null) {
			u = this.pred.get(u);
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
		try {
			return dist.get(ziel);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
