package graph;

import static trafficsim.TrafficSimConstants.CITY_SPEED_LIMIT;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import trafficsim.TrafficSimWorld;
import trafficsim.components.SpawnComponent;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.Road;
import trafficsim.roads.Road.Direction;
import trafficsim.spawning.FixedIntervalSpawningStrategy;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

public class GraphFactory {

	public static void addSpawnPointsTest(TrafficSimWorld world, Graph<Road> graph) {

		float length = 60;
		int interval = 2000;

		Vertex<Road> connection = graph.getVertex(0);
		Vector2 edgeB = connection.getData().getPointA();
		Vector2 edgeA = new Vector2(edgeB.x - length, edgeB.y);
		makeSpawnVertex(world, connection, graph, edgeA, edgeB, new Vector2(edgeA.x - 2 * LANE_WIDTH, edgeA.y), edgeA,
						true, interval * 100);

		// Vertex<Road> connection2 = graph.getVertex(29);
		// Vector2 edgeA2 = connection2.getData().getPointB();
		// Vector2 edgeB2 = new Vector2(edgeA2.x + length, edgeA2.y);
		// makeSpawnVertex(world, connection2, graph, edgeA2, edgeB2, edgeB2, new Vector2(edgeB2.x + 2 * LANE_WIDTH,
		// edgeB2.y), false);
		//
		// Vertex<Road> connection3 = graph.getVertex(4);
		// Vector2 edgeB3 = connection3.getData().getPointA();
		// Vector2 edgeA3 = new Vector2(edgeB3.x - length, edgeB3.y);
		// makeSpawnVertex(world, connection3, graph, edgeA3, edgeB3, new Vector2(edgeA3.x - 2 * LANE_WIDTH, edgeA3.y),
		// edgeA3, true);
		//
		// Vertex<Road> connection4 = graph.getVertex(25);
		// Vector2 edgeA4 = connection4.getData().getPointB();
		// Vector2 edgeB4 = new Vector2(edgeA4.x + length, edgeA4.y);
		// makeSpawnVertex(world, connection4, graph, edgeA4, edgeB4, edgeB4, new Vector2(edgeB4.x + 2 * LANE_WIDTH,
		// edgeB4.y), false);
	}

	// TODO Refactor this mess
	public static Vertex<Road> makeSpawnVertex(TrafficSimWorld world, Vertex<Road> connection, Graph<Road> graph,
			Vector2 edgeA, Vector2 edgeB, Vector2 vertexA, Vector2 vertexB, boolean AtoB, int interval) {
		Road intersection = new Road(vertexA, vertexB, 1, Direction.BOTH, CITY_SPEED_LIMIT);
		Vertex<Road> spawn1 = graph.addVertex(intersection);
		Road edge = new Road(edgeA, edgeB, 1, Direction.BOTH, CITY_SPEED_LIMIT);
		// System.out.println(VectorUtils.getAngle(intersection) + " " + VectorUtils.getAngle(edge));
		Edge<Road> roadEdge;
		if (AtoB)
			roadEdge = graph.addEdge(edge, spawn1, connection, false);
		else
			roadEdge = graph.addEdge(edge, connection, spawn1, false);
		EntityFactory.createRoad(world, spawn1).addToWorld();
		EntityFactory.createRoad(world, roadEdge).addToWorld();
		Entity spawnPoint = world.createEntity();
		spawnPoint.addComponent(new SpawnComponent(spawn1, new FixedIntervalSpawningStrategy(interval)));
		spawnPoint.addToWorld();
		return spawn1;
	}


	public static Graph<Road> createManhattanGraph(int width, int height, float distance, float startX, float startY) {
		Graph<Road> graph = new Graph<Road>();
		// Create vertices
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float x = startX + i * distance;
				float y = startY + j * distance;
				// TODO think this through and check for intersection between roads with different number of lanes
				float halfW = LANE_WIDTH;
				graph.addVertex(new Road(new Vector2(x, y), new Vector2(x + 2 * halfW, y), 1, Direction.BOTH,
											CITY_SPEED_LIMIT));
				// graph.addVertex(new Road(new Vector2(x - halfW, y), new Vector2(x + halfW, y), 1, Direction.BOTH,
				// CITY_SPEED_LIMIT));
			}
		}
		// Create edges
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vertex<Road> v1 = graph.getVertex(i * height + j);
				if (i < width - 1) {
					Vertex<Road> v2 = graph.getVertex((i + 1) * height + j);
					Vector2 pointA = VectorUtils.getMidPoint(v1.getData());
					pointA.set(pointA.x + LANE_WIDTH, pointA.y);
					Vector2 pointB = VectorUtils.getMidPoint(v2.getData());
					pointB.set(pointB.x - LANE_WIDTH, pointB.y);

					graph.addEdge(new Road(pointA, pointB, 1, Direction.BOTH, CITY_SPEED_LIMIT), v1, v2, false);
				}
				if (j < height - 1) {
					Vertex<Road> v3 = graph.getVertex(i * height + j + 1);
					Vector2 pointA = VectorUtils.getMidPoint(v1.getData());
					Vector2 pointB = VectorUtils.getMidPoint(v3.getData());
					pointA.set(pointA.x, pointA.y + LANE_WIDTH);
					pointB.set(pointB.x, pointB.y - LANE_WIDTH);
					graph.addEdge(new Road(pointA, pointB, 1, Direction.BOTH, CITY_SPEED_LIMIT), v1, v3, false);
				}
			}
		}
		return graph;
	}
}
