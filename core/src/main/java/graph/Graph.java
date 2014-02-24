package graph;

import java.util.ArrayList;

import lombok.Getter;

public class Graph {

	@Getter
	private ArrayList<Vertex> vertices;
	@Getter
	private ArrayList<Edge> edges;
	
	public Graph() {
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}
	
	public void addVertex(Vertex v) {
		vertices.add(v);
	}
	
}
