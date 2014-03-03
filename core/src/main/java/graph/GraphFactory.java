package graph;

import trafficsim.roads.Road;
import trafficsim.roads.Road.Direction;

import com.badlogic.gdx.math.Vector2;

public class GraphFactory {

	public static Graph<Road> createManhattanGraph(int width, int height, float distance, float startX, float startY) {
		Graph<Road> graph = new Graph<Road>();
		// Create vertices
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float x = startX + i * distance;
				float y = startY + j * distance;
				graph.addVertex(new Road(new Vector2(x, y), null, 1, Direction.BOTH));
			}
		}
		// Create edges
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vertex<Road> v1 = graph.getVertex(i * height + j);
				if (i < width - 1) {
					Vertex<Road> v2 = graph.getVertex((i + 1) * height + j);
					graph.addEdge(new Road(v1.getData().getPointA(), v2.getData().getPointA(), 1, Direction.BOTH), v1, v2, false);
				}
				if (j < height - 1) {
					Vertex<Road> v3 = graph.getVertex(i * height + j + 1);
					graph.addEdge(new Road(v1.getData().getPointA(), v3.getData().getPointA(), 1, Direction.BOTH), v1, v3, false);
				}
			}
		}
		return graph;
	}
}
