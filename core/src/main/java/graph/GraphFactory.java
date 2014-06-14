package graph;

import static trafficsim.TrafficSimConstants.CITY_SPEED_LIMIT;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import trafficsim.TrafficSimWorld;
import trafficsim.components.SpawnComponent;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.Road;
import trafficsim.roads.Road.Direction;
import trafficsim.spawning.AbstractSpawnStrategy;
import trafficsim.spawning.FixedIntervalSpawningStrategy;
import trafficsim.spawning.PoissonSpawnStrategy;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

public class GraphFactory {

	public static void addSpawnPointsTest(TrafficSimWorld world, Graph<Road> graph) {

		float length = 60;
		int interval = 3000;

		int[] indices = { 0, graph.getVertexCount() - 1, (int) Math.sqrt(graph.getVertexCount() - 1),
							graph.getVertexCount() - (int) Math.sqrt(graph.getVertexCount()) };

		Vertex<Road> connection = graph.getVertex(indices[0]);
		Vector2 edgeB = connection.getData().getPointA();
		Vector2 edgeA = new Vector2(edgeB.x - length, edgeB.y);
		makeSpawnVertex(world, connection, graph, edgeA, edgeB, new Vector2(edgeA.x - 2 * LANE_WIDTH, edgeA.y), edgeA,
						true, interval);

		Vertex<Road> connection2 = graph.getVertex(indices[1]);
		Vector2 edgeA2 = connection2.getData().getPointB();
		Vector2 edgeB2 = new Vector2(edgeA2.x + length, edgeA2.y);
		makeSpawnVertex(world, connection2, graph, edgeA2, edgeB2, edgeB2, new Vector2(edgeB2.x + 2 * LANE_WIDTH,
																						edgeB2.y), false, interval);
		// makeSpawnVertex(world, connection2, graph, edgeA2, edgeB2, new Vector2(edgeB2.x + 2 * LANE_WIDTH, edgeB2.y),
		// edgeB2, false, interval);

		Vertex<Road> connection3 = graph.getVertex(indices[2]);
		Vector2 edgeB3 = connection3.getData().getPointA();
		Vector2 edgeA3 = new Vector2(edgeB3.x - length, edgeB3.y);
		makeSpawnVertex(world, connection3, graph, edgeA3, edgeB3, new Vector2(edgeA3.x - 2 * LANE_WIDTH, edgeA3.y),
						edgeA3, true, interval);

		Vertex<Road> connection4 = graph.getVertex(indices[3]);
		Vector2 edgeA4 = connection4.getData().getPointB();
		Vector2 edgeB4 = new Vector2(edgeA4.x + length, edgeA4.y);
		// makeSpawnVertex(world, connection4, graph, edgeA4, edgeB4, edgeB4, new Vector2(edgeB4.x + 2 * LANE_WIDTH,
		// edgeB4.y), false, interval);
		makeSpawnVertex(world, connection4, graph, edgeA4, edgeB4, edgeB4, new Vector2(edgeB4.x + 2 * LANE_WIDTH,
																						edgeB4.y), false, interval);
	}

	public static boolean poisson = true;

	// TODO Refactor this mess
	public static Vertex<Road> makeSpawnVertex(TrafficSimWorld world, Vertex<Road> connection, Graph<Road> graph,
			Vector2 edgeA, Vector2 edgeB, Vector2 vertexA, Vector2 vertexB, boolean AtoB, int interval) {
		Road intersection = new Road(vertexA, vertexB, 1, Direction.BOTH, CITY_SPEED_LIMIT);
		Vertex<Road> spawn1 = graph.addVertex(intersection);

		Vector2 pointA1 = new Vector2(edgeA.x, edgeA.y - LANE_WIDTH / 2);
		Vector2 pointB1 = new Vector2(edgeB.x, edgeB.y - LANE_WIDTH / 2);

		Vector2 pointA2 = new Vector2(edgeA.x, edgeA.y + LANE_WIDTH / 2);
		Vector2 pointB2 = new Vector2(edgeB.x, edgeB.y + LANE_WIDTH / 2);

		Road edge1;
		Road edge2;

		if (AtoB) {

			edge1 = new Road(pointA1, pointB1, 1, Direction.BOTH, CITY_SPEED_LIMIT);
			edge2 = new Road(pointB2, pointA2, 1, Direction.BOTH, CITY_SPEED_LIMIT);
		}
		else {
			edge1 = new Road(pointA1, pointB1, 1, Direction.BOTH, CITY_SPEED_LIMIT);
			edge2 = new Road(pointB2, pointA2, 1, Direction.BOTH, CITY_SPEED_LIMIT);
		}
		// System.out.println(VectorUtils.getAngle(intersection) + " " + VectorUtils.getAngle(edge));
		Edge<Road> roadEdge1;
		Edge<Road> roadEdge2;
		if (AtoB) {
			roadEdge1 = graph.addEdge(edge1, spawn1, connection, true);
			roadEdge2 = graph.addEdge(edge2, connection, spawn1, true);
		}
		 else {
		 roadEdge1 = graph.addEdge(edge1, connection, spawn1, true);
		 roadEdge2 = graph.addEdge(edge2, spawn1, connection, true);
		 }
		EntityFactory.createRoad(world, spawn1).addToWorld();
		EntityFactory.createRoad(world, roadEdge1).addToWorld();
		EntityFactory.createRoad(world, roadEdge2).addToWorld();
		Entity spawnPoint = world.createEntity();
		AbstractSpawnStrategy spawnStrategy = poisson	? new PoissonSpawnStrategy(interval)
														: new FixedIntervalSpawningStrategy(interval);
		// AbstractSpawnStrategy spawnStrategy = new PoissonSpawnStrategy(interval);
		spawnPoint.addComponent(new SpawnComponent(spawn1, spawnStrategy));
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
					Vector2 pointB = VectorUtils.getMidPoint(v2.getData());

					Vector2 pointA1 = new Vector2(pointA.x + LANE_WIDTH, pointA.y - LANE_WIDTH / 2);
					Vector2 pointB1 = new Vector2(pointB.x - LANE_WIDTH, pointB.y - LANE_WIDTH / 2);

					Vector2 pointA2 = new Vector2(pointA.x + LANE_WIDTH, pointA.y + LANE_WIDTH / 2);
					Vector2 pointB2 = new Vector2(pointB.x - LANE_WIDTH, pointB.y + LANE_WIDTH / 2);

					// graph.addEdge(new Road(pointA, pointB, 1, Direction.BOTH, CITY_SPEED_LIMIT), v1, v2, false);

					graph.addEdge(new Road(pointA1, pointB1, 1, Direction.DOWNSTREAM, CITY_SPEED_LIMIT), v1, v2, true);
					graph.addEdge(new Road(pointB2, pointA2, 1, Direction.UPSTREAM, CITY_SPEED_LIMIT), v2, v1, true);
				}
				if (j < height - 1) {
					Vertex<Road> v3 = graph.getVertex(i * height + j + 1);
					Vector2 pointA = VectorUtils.getMidPoint(v1.getData());
					Vector2 pointB = VectorUtils.getMidPoint(v3.getData());

					Vector2 pointA1 = new Vector2(pointA.x + LANE_WIDTH / 2, pointA.y + LANE_WIDTH);
					Vector2 pointB1 = new Vector2(pointB.x + LANE_WIDTH / 2, pointB.y - LANE_WIDTH);

					Vector2 pointA2 = new Vector2(pointA.x - LANE_WIDTH / 2, pointA.y + LANE_WIDTH);
					Vector2 pointB2 = new Vector2(pointB.x - LANE_WIDTH / 2, pointB.y - LANE_WIDTH);

					pointA.set(pointA.x, pointA.y + LANE_WIDTH);
					pointB.set(pointB.x, pointB.y - LANE_WIDTH);

					// graph.addEdge(new Road(pointA, pointB, 1, Direction.BOTH, CITY_SPEED_LIMIT), v1, v3, false);

					graph.addEdge(new Road(pointA1, pointB1, 1, Direction.DOWNSTREAM, CITY_SPEED_LIMIT), v1, v3, true);
					graph.addEdge(new Road(pointB2, pointA2, 1, Direction.UPSTREAM, CITY_SPEED_LIMIT), v3, v1, true);
				}
			}
		}
		return graph;
	}
}
