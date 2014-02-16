package graph;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class Vertex {

	@Getter
	@Setter
	private VertexData data;

	@Getter
	private ArrayList<Edge> edges;

	public Vertex() {
		edges = new ArrayList<Edge>();
	}

	public Vertex(VertexData data) {
		edges = new ArrayList<Edge>();
		this.data = data;
	}

	public void notifyVertexAddition(Edge newEdge) {
		if (!edges.contains(newEdge))
			edges.add(newEdge);
		else
			System.out.println("Already neighboring...");
	}

	public void notifyVertexDeletion(Edge deletedEdge) {
		if (edges.contains(deletedEdge))
			edges.remove(deletedEdge);
		else
			System.out.println("Vertex to be deleted is not neighbor of this!");
	}

	public ArrayList<Vertex> getAdjacentVertices() {
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		for (val e : edges) {
			result.add(e.getNeighbor(this));
		}
		return result;
	}

	public void destroy() {
		for (val e : edges) {
			e.destroy();
			edges = null;
		}
	}

	@Override
	public String toString() {
		return data.toString();
	}
}
