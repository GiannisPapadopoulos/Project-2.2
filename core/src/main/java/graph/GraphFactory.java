package graph;

import paramatricCurves.ParametricCurve;
import paramatricCurves.curveDefs.C_Linear;
import lombok.val;
import trafficsim.TrafficSimConstants;
import trafficsim.roads.CrossRoad;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;

import com.badlogic.gdx.math.Vector2;

public class GraphFactory {

	/*
	 * public static void addSpawnPointsTest(TrafficSimWorld world, Graph<Road>
	 * graph) {
	 * 
	 * Vertex<Road> connection = graph.getVertex(0); Vector2 edgeB =
	 * connection.getData().getPointA(); Vector2 edgeA = new Vector2(edgeB.x -
	 * 20, edgeB.y); makeSpawnVertex(world, connection, graph, edgeA, edgeB, new
	 * Vector2(edgeA.x - 2 * LANE_WIDTH, edgeA.y), edgeA, true);
	 * 
	 * Vertex<Road> connection2 = graph.getVertex(29); Vector2 edgeA2 =
	 * connection2.getData().getPointB(); Vector2 edgeB2 = new Vector2(edgeA2.x
	 * + 20, edgeA2.y); makeSpawnVertex(world, connection2, graph, edgeA2,
	 * edgeB2, edgeB2, new Vector2(edgeB2.x + 2 * LANE_WIDTH, edgeB2.y), false);
	 * 
	 * Vertex<Road> connection3 = graph.getVertex(4); Vector2 edgeB3 =
	 * connection3.getData().getPointA(); Vector2 edgeA3 = new Vector2(edgeB3.x
	 * - 20, edgeB3.y); makeSpawnVertex(world, connection3, graph, edgeA3,
	 * edgeB3, new Vector2(edgeA3.x - 2 * LANE_WIDTH, edgeA3.y), edgeA3, true);
	 * 
	 * Vertex<Road> connection4 = graph.getVertex(25); Vector2 edgeA4 =
	 * connection4.getData().getPointB(); Vector2 edgeB4 = new Vector2(edgeA4.x
	 * + 20, edgeA4.y); makeSpawnVertex(world, connection4, graph, edgeA4,
	 * edgeB4, edgeB4, new Vector2(edgeB4.x + 2 * LANE_WIDTH, edgeB4.y), false);
	 * }
	 */

	// TODO Refactor this mess
	/*
	 * public static Vertex<Road> makeSpawnVertex(TrafficSimWorld world,
	 * Vertex<Road> connection, Graph<Road> graph, Vector2 edgeA, Vector2 edgeB,
	 * Vector2 vertexA, Vector2 vertexB, boolean AtoB) { Road intersection = new
	 * Road(vertexA, vertexB, 1, CITY_SPEED_LIMIT); Vertex<Road> spawn1 =
	 * graph.addVertex(intersection); Road edge = new Road(edgeA, edgeB, 1,
	 * CITY_SPEED_LIMIT); //
	 * System.out.println(VectorUtils.getAngle(intersection) + " " +
	 * VectorUtils.getAngle(edge)); Edge<Road> roadEdge; if (AtoB) roadEdge =
	 * graph.addEdge(edge, spawn1, connection, false); else roadEdge =
	 * graph.addEdge(edge, connection, spawn1, false);
	 * EntityFactory.createRoad(world, spawn1).addToWorld();
	 * EntityFactory.createRoad(world, roadEdge).addToWorld(); Entity spawnPoint
	 * = world.createEntity(); int interval = 2000; spawnPoint.addComponent(new
	 * SpawnComponent(spawn1, new FixedIntervalSpawningStrategy(interval)));
	 * spawnPoint.addToWorld(); return spawn1; }
	 */
	/*
	 * public static Graph<Road> createManhattanGraph(int width, int height,
	 * float distance, float startX, float startY) { Graph<Road> graph = new
	 * Graph<Road>(); // Create vertices for (int i = 0; i < width; i++) { for
	 * (int j = 0; j < height; j++) { float x = startX + i * distance; float y =
	 * startY + j * distance; // TODO think this through and check for
	 * intersection between roads with different number of lanes float halfW =
	 * LANE_WIDTH; graph.addVertex(new Road(new Vector2(x, y), new Vector2(x + 2
	 * * halfW, y), 1, CITY_SPEED_LIMIT)); // graph.addVertex(new Road(new
	 * Vector2(x - halfW, y), new Vector2(x + halfW, y), 1, Direction.BOTH, //
	 * CITY_SPEED_LIMIT)); } }
	 * 
	 * // Create edges for (int i = 0; i < width; i++) { for (int j = 0; j <
	 * height; j++) { Vertex<Road> v1 = graph.getVertex(i * height + j); if (i <
	 * width - 1) { Vertex<Road> v2 = graph.getVertex((i + 1) * height + j);
	 * Vector2 pointA = VectorUtils.getMidPoint(v1.getData());
	 * pointA.set(pointA.x + LANE_WIDTH, pointA.y); Vector2 pointB =
	 * VectorUtils.getMidPoint(v2.getData()); pointB.set(pointB.x - LANE_WIDTH,
	 * pointB.y);
	 * 
	 * graph.addEdge(new Road(new ParametricCurve(new
	 * C_Linear(pointA,pointB)),1,1,CITY_SPEED_LIMIT),v1,v2,false); } if (j <
	 * height - 1) { Vertex<Road> v3 = graph.getVertex(i * height + j + 1);
	 * Vector2 pointA = VectorUtils.getMidPoint(v1.getData()); Vector2 pointB =
	 * VectorUtils.getMidPoint(v3.getData()); pointA.set(pointA.x, pointA.y +
	 * LANE_WIDTH); pointB.set(pointB.x, pointB.y - LANE_WIDTH);
	 * graph.addEdge(new Road(new ParametricCurve(new
	 * C_Linear(pointA,pointB)),1,1,CITY_SPEED_LIMIT), v1, v3, false); } } }
	 * 
	 * Vector2 a = new Vector2(); a.x = 0; a.y = 0; Vector2 b = new Vector2();
	 * b.x = 300; b.y = 300; graph.addVertex(new Road(new ParametricCurve(new
	 * C_Linear(a,b)),3,3,CITY_SPEED_LIMIT)); return graph; }
	 */
	public static Graph<NavigationObject> createNewSystem() {
		Graph<NavigationObject> graph = new Graph<NavigationObject>();

		graph.addVertex(new CrossRoad(10.0f, new Vector2(10.0f, 10.0f)));
		graph.addVertex(new CrossRoad(10.0f, new Vector2(100.0f, 10.0f)));

		CrossRoad cr1 = (CrossRoad) graph.getVertex(0).getData();
		CrossRoad cr2 = (CrossRoad) graph.getVertex(1).getData();

		graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(cr1.getPosition(),
						cr2.getPosition())), 2,
						TrafficSimConstants.CITY_SPEED_LIMIT, cr1, cr2), graph
						.getVertex(0), graph.getVertex(0), false);

		return graph;
	}
}
