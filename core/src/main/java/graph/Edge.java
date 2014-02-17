package graph;

import lombok.Getter;
import lombok.Setter;

public class Edge {

	@Getter
	private Vertex v1;
	@Getter
	private Vertex v2;
	@Getter
	@Setter
	private EdgeData edgeData;
	
	public Edge(Vertex v1, Vertex v2, EdgeData edgeData) {
		this.v1 = v1;
		this.v2 = v2;
		v1.notifyVertexAddition(this);
		v2.notifyVertexAddition(this);
		this.edgeData = edgeData;
	}

	public Vertex getNeighbor(Vertex vertex) {
		if (vertex.equals(v1))
			return v2;
		else if (vertex.equals(v2))
			return v1;
		else {
			System.out.println("!!! Wrong edge/vertex definition !!!");
			return null;
		}
	}

	public void destroy() {
		v1.notifyVertexDeletion(this);
		v2.notifyVertexDeletion(this);
		v1 = null;
		v2 = null;
		edgeData = null;
	}
	
	public String toString() {
		return "Edge connecting "+v1.toString()+" and "+v2.toString();
	}

}
