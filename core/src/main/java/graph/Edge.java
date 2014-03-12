package graph;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Edge<E>
		extends Element<E> {

	protected Edge(Graph<E> parent, int ID, E data, Vertex<E> v1, Vertex<E> v2, boolean directed) {
		super(parent, ID, data);
		adjacentVertices.add(v1.getID());
		adjacentVertices.add(v2.getID());
		v1.getAdjacentVertices().add(v2.getID());
		v1.getAdjacentEdges().add(ID);
		if (!directed) {
			v2.getAdjacentVertices().add(v1.getID());
			v2.getAdjacentEdges().add(ID);
		}
	}

	/** Returns the other vertex of this edge or null if the given vertex is not part of the edge */
	public Vertex<E> getNeighbor(Vertex<E> vertex) {
		if (!adjacentVertices.contains(vertex.getID())) {
			return null;
		}
		Vertex<E> v1 = parent.getVertex(getAdjacentVertices().get(0));
		Vertex<E> v2 = parent.getVertex(getAdjacentVertices().get(1));
		return v1 == vertex ? v2 : v1;
	}

	// Do we have to support this?
	// public void destroy() {
	//
	// }

	public String toString() {
		return "Edge connecting " + adjacentVertices.get(0) + " and " + adjacentVertices.get(1);
	}

	public boolean isDirected() {
		Vertex<E> v1 = parent.getVertex(getAdjacentVertices().get(0));
		Vertex<E> v2 = parent.getVertex(getAdjacentVertices().get(1));
		return !v2.getAdjacentVertices().contains(v1.getID());
	}
}
