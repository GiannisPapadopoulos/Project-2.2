package search;

import graph.Graph;
import graph.GraphFactory;
import lombok.val;

import org.junit.Test;

import pathfinding.GraphBasedAstar;
import trafficsim.roads.Road;

public class AstarTestCase {

	@Test
	public void test() {
		Graph<Road> graph = null;
		GraphBasedAstar aStar = new GraphBasedAstar();
		@SuppressWarnings("unused")
		val path = aStar.findRoute(graph.getVertex(0), graph.getVertex(graph.getVertexCount() - 1));
		// System.out.println(path);
	}

}
