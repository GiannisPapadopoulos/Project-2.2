package graph;

import static trafficsim.TrafficSimConstants.CITY_SPEED_LIMIT;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import lombok.val;
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

	public static void addTrafficLights(TrafficSimWorld world, Graph<Road> graph) {
		for(Edge<Road> edge : graph.getEdgeIterator()) {
			val iterator = edge.getAdjacentVertexIterator();
			Vertex<Road> vertexA = iterator.next();
			Vertex<Road> vertexB = iterator.next();
			if (vertexA.getAdjacentVertices().size() > 1) {
				edge.getData().getPointA();
				Vector2 pos = edge.getData().getPointA().cpy().add(VectorUtils.getVector(edge.getData()).nor().scl(-5));
				// EntityFactory.createTrafficLight(world, pos, 5, 2, 5, null, false, null)
			}
		}
	}

	public static void addSpawnPointsTest(TrafficSimWorld world, Graph<Road> graph) {
		Vertex<Road> connection = graph.getVertex(0);
		Vector2 pos = VectorUtils.getMidPoint(connection.getData());
		float x = pos.x - 60;
		float y = pos.y;
		Vertex<Road> spawn1 = makeSpawnVertex(x, y, connection, graph);
		Road edge = new Road(VectorUtils.getMidPoint(spawn1.getData()), VectorUtils.getMidPoint(connection.getData()),
								1, Direction.BOTH, CITY_SPEED_LIMIT);
		graph.addEdge(edge, spawn1, connection, false);
		EntityFactory.createRoad(world, spawn1.getData()).addToWorld();
		EntityFactory.createRoad(world, edge).addToWorld();
		Entity spawnPoint = world.createEntity();
		spawnPoint.addComponent(new SpawnComponent(spawn1, new FixedIntervalSpawningStrategy(2000)));
		spawnPoint.addToWorld();
	}

	public static Vertex<Road> makeSpawnVertex(float x, float y, Vertex<Road> connection, Graph<Road> graph) {
		float halfW = LANE_WIDTH;
		Road road = new Road(new Vector2(x - halfW, y), new Vector2(x + halfW, y), 1, Direction.BOTH, CITY_SPEED_LIMIT);
		Vertex<Road> spawn1 = graph.addVertex(road);
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
