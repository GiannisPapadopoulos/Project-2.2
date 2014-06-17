package graph;

import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import lombok.val;
import paramatricCurves.ParametricCurve;
import paramatricCurves.curveDefs.C_Linear;
import trafficsim.TrafficSimConstants;
import trafficsim.roads.CrossRoad;
import trafficsim.roads.CrossRoad.CR_TYPE;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;
import trafficsim.roads.Road.Direction;
import trafficsim.roads.SubSystem;

import com.badlogic.gdx.math.Vector2;

import editor.EditorData;
import functions.VectorUtils;

public class GraphFactory {

	public static final boolean useManhattanGraph = false;

	/** */
	public static final boolean makeRoundabout = true;

	public static Graph<NavigationObject> createGraph() {
		if (useManhattanGraph) {
			// TODO add highway here
			return createManhattanGraph(10, 10, 100.0f, 0, 0);
		} else {
			return createTestOneGraph(makeRoundabout);
		}
	}

	public static Graph<NavigationObject> createManhattanGraphWithCircuit(
			int width, int height, float laneLength, int startX, int startY) {

		Graph<NavigationObject> graph = new Graph<NavigationObject>();

		float halfW = TrafficSimConstants.LANE_WIDTH * 2;
		// Create vertices
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float x = startX + i * laneLength;
				float y = startY + j * laneLength;

				float crossroadSize;
				if (i != width / 2 && j != height / 2)
					crossroadSize = 4 * LANE_WIDTH;
				else
					crossroadSize = 8 * LANE_WIDTH;

				graph.addVertex(new CrossRoad(crossroadSize, new Vector2(x, y),
						CrossRoad.CR_TYPE.CrossRoad));

			}
		}

		// Create edges
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vertex<NavigationObject> v1 = graph.getVertex(i * height + j);
				if (i < width - 1) {
					Vertex<NavigationObject> v2 = graph.getVertex((i + 1)
							* height + j);

					if (j == height / 2)// || j == 0 || j == height - 1)
						addEdgeBoth(graph, v1, v2, 2,
								TrafficSimConstants.CITY_SPEED_LIMIT);
					else
						addEdgeBoth(graph, v1, v2, 1,
								TrafficSimConstants.CITY_SPEED_LIMIT);

				}
				if (j < height - 1) {
					Vertex<NavigationObject> v3 = graph.getVertex(i * height
							+ j + 1);
					if (i == width / 2 )//|| i == 0 || i == width - 1)
						addEdgeBoth(graph, v1, v3, 2,
								TrafficSimConstants.CITY_SPEED_LIMIT);
					else
						addEdgeBoth(graph, v1, v3, 1,
								TrafficSimConstants.CITY_SPEED_LIMIT);
				}
			}

		}

		addighway(graph, width, height, laneLength, startX, startY);
		return graph;

	}


	public static Graph<NavigationObject> createTestOneGraph(
			boolean GiannisRound) {
		Graph<NavigationObject> graph = new Graph<NavigationObject>();

		float halfW = TrafficSimConstants.LANE_WIDTH * 2;
		float crossroadSize = 4 * LANE_WIDTH;
		float roadLength = 100;

		CrossRoad.CR_TYPE type;
		float bob;

		if (GiannisRound) {
			bob = crossroadSize * 2;
			type = CrossRoad.CR_TYPE.Roundabout;
		} else {
			bob = crossroadSize;
			type = CrossRoad.CR_TYPE.CrossRoad;
		}
		// center point
		CrossRoad cCross = new CrossRoad(bob, new Vector2(0, 0), type);
		Vertex<NavigationObject> vCCross = graph.addVertex(cCross);

		// left point
		CrossRoad lCross = new CrossRoad(crossroadSize, new Vector2(
				-roadLength, 0), CrossRoad.CR_TYPE.CrossRoad);
		Vertex<NavigationObject> vLCross = graph.addVertex(lCross);

		// right point
		CrossRoad rCross = new CrossRoad(crossroadSize, new Vector2(roadLength,
				0), CrossRoad.CR_TYPE.CrossRoad);
		Vertex<NavigationObject> vRCross = graph.addVertex(rCross);

		// bottom point
		CrossRoad bCross = new CrossRoad(crossroadSize, new Vector2(0,
				-roadLength), CrossRoad.CR_TYPE.CrossRoad);
		Vertex<NavigationObject> vBCross = graph.addVertex(bCross);

		// top point
		CrossRoad tCross = new CrossRoad(crossroadSize, new Vector2(0,
				roadLength), CrossRoad.CR_TYPE.CrossRoad);
		Vertex<NavigationObject> vTCross = graph.addVertex(tCross);

		Vector2 ver_s_pos_m = new Vector2(0, cCross.getSize() / 2);
		Vector2 ver_s_neg_m = new Vector2(0, -cCross.getSize() / 2);
		Vector2 hor_s_pos_m = new Vector2(cCross.getSize() / 2, 0);
		Vector2 hor_s_neg_m = new Vector2(-cCross.getSize() / 2, 0);

		Vector2 ver_s_pos = new Vector2(0, lCross.getSize() / 2);
		Vector2 ver_s_neg = new Vector2(0, -lCross.getSize() / 2);
		Vector2 hor_s_pos = new Vector2(lCross.getSize() / 2, 0);
		Vector2 hor_s_neg = new Vector2(-lCross.getSize() / 2, 0);

		Vector2 pcC = vCCross.getData().getPosition();
		Vector2 plC = vLCross.getData().getPosition();
		Vector2 prC = vRCross.getData().getPosition();
		Vector2 pbC = vBCross.getData().getPosition();
		Vector2 ptC = vTCross.getData().getPosition();

		// left to center and back
		graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(pcC, hor_s_neg_m), VectorUtils
						.add2Vectors(plC, hor_s_pos))), 1,
						TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT,
						(CrossRoad) vCCross.getData(), (CrossRoad) vLCross
								.getData(), Direction.UPSTREAM), vCCross,
				vLCross, true);
		graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(plC, hor_s_pos), VectorUtils.add2Vectors(
						pcC, hor_s_neg_m))), 1,
						TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT,
						(CrossRoad) vLCross.getData(), (CrossRoad) vCCross
								.getData(), Direction.DOWNSTREAM), vLCross,
				vCCross, true);

		// right to center and back
		graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(pcC, hor_s_pos_m), VectorUtils
						.add2Vectors(prC, hor_s_neg))), 1,
						TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT,
						(CrossRoad) vCCross.getData(), (CrossRoad) vRCross
								.getData(), Direction.UPSTREAM), vCCross,
				vRCross, true);
		graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(prC, hor_s_neg), VectorUtils.add2Vectors(
						pcC, hor_s_pos_m))), 1,
						TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT,
						(CrossRoad) vRCross.getData(), (CrossRoad) vCCross
								.getData(), Direction.DOWNSTREAM), vRCross,
				vCCross, true);

		// top to center and back
		val e1 = graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(pcC, ver_s_pos_m), VectorUtils
						.add2Vectors(ptC, ver_s_neg))), 1,
						TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT,
						(CrossRoad) vCCross.getData(), (CrossRoad) vTCross
								.getData(), Direction.UPSTREAM), vCCross,
				vTCross, true);
		val e2 = graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(ptC, ver_s_neg), VectorUtils.add2Vectors(
						pcC, ver_s_pos_m))), 1,
						TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT,
						(CrossRoad) vTCross.getData(), (CrossRoad) vCCross
								.getData(), Direction.DOWNSTREAM), vTCross,
				vCCross, true);

		// bottom to center and back
		graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(pcC, ver_s_neg_m), VectorUtils
						.add2Vectors(pbC, ver_s_pos))), 1,
						TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT,
						(CrossRoad) vCCross.getData(), (CrossRoad) vBCross
								.getData(), Direction.UPSTREAM), vCCross,
				vBCross, true);
		graph.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(pbC, ver_s_pos), VectorUtils.add2Vectors(
						pcC, ver_s_neg_m))), 1,
						TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT,
						(CrossRoad) vBCross.getData(), (CrossRoad) vCCross
								.getData(), Direction.DOWNSTREAM), vBCross,
				vCCross, true);

		return graph;
	}

	public static Graph<NavigationObject> createManhattanGraph(int width,
			int height, float distance, float startX, float startY) {
		Graph<NavigationObject> graph = new Graph<NavigationObject>();
		float halfW = TrafficSimConstants.LANE_WIDTH * 2;
		// Create vertices
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float x = startX + i * distance;
				float y = startY + j * distance;

				float crossroadSize = 4 * LANE_WIDTH;
				if (i * height + j == 10)
					graph.addVertex(new CrossRoad(crossroadSize, new Vector2(x,
							y), CrossRoad.CR_TYPE.Roundabout));
				else
					graph.addVertex(new CrossRoad(crossroadSize, new Vector2(x,
							y), CrossRoad.CR_TYPE.CrossRoad));

			}
		}

		// Create edges
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vertex<NavigationObject> v1 = graph.getVertex(i * height + j);
				if (i < width - 1) {
					Vertex<NavigationObject> v2 = graph.getVertex((i + 1)
							* height + j);
					Vector2 pointA = new Vector2(v1.getData().getPosition());
					pointA.set(pointA.x + halfW, pointA.y);
					Vector2 pointB = new Vector2(v2.getData().getPosition());
					pointB.set(pointB.x - halfW, pointB.y);

					graph.addEdge(new Road(new ParametricCurve(new C_Linear(
							pointA, pointB)), 1,
							TrafficSimConstants.CITY_SPEED_LIMIT,
							(CrossRoad) v1.getData(), (CrossRoad) v2.getData(),
							Direction.DOWNSTREAM), v1, v2, true);
					graph.addEdge(new Road(new ParametricCurve(new C_Linear(
							pointB, pointA)), 1,
							TrafficSimConstants.CITY_SPEED_LIMIT,
							(CrossRoad) v2.getData(), (CrossRoad) v1.getData(),
							Direction.UPSTREAM), v2, v1, true);
				}
				if (j < height - 1) {
					Vertex<NavigationObject> v3 = graph.getVertex(i * height
							+ j + 1);
					Vector2 pointA = v1.getData().getPosition();
					Vector2 pointB = v3.getData().getPosition();
					pointA.set(pointA.x, pointA.y + halfW);
					pointB.set(pointB.x, pointB.y - halfW);
					Edge<NavigationObject> e1 = graph.addEdge(new Road(
							new ParametricCurve(new C_Linear(pointA, pointB)),
							1, TrafficSimConstants.CITY_SPEED_LIMIT,
							(CrossRoad) v1.getData(), (CrossRoad) v3.getData(),
							Direction.DOWNSTREAM), v1, v3, true);
					Edge<NavigationObject> e2 = graph.addEdge(new Road(
							new ParametricCurve(new C_Linear(pointB, pointA)),
							1, TrafficSimConstants.CITY_SPEED_LIMIT,
							(CrossRoad) v3.getData(), (CrossRoad) v1.getData(),
							Direction.UPSTREAM), v3, v1, true);
					Road r1 = (Road) e1.getData();
					for (val key : r1.getRSubSystems().keySet()) {
						SubSystem system = r1.getRSubSystems().get(key);
						// System.out.println(system.getLanes().get(0));
					}
				}
			}

		}
		return graph;
	}

	public static Graph<NavigationObject> laneSwitchTest(
			Graph<NavigationObject> g) {

		float crossSize = 100.0f;

		CrossRoad c_0_0 = new CrossRoad(crossSize, new Vector2(0, 0),
				CrossRoad.CR_TYPE.HighWay_Cross);
		CrossRoad c_0_1 = new CrossRoad(crossSize, new Vector2(0, 400),
				CrossRoad.CR_TYPE.CrossRoad);

		Vertex<NavigationObject> v_0_0 = g.addVertex(c_0_0);
		Vertex<NavigationObject> v_0_1 = g.addVertex(c_0_1);

		g.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(c_0_0.getPosition(),
								new Vector2(0, c_0_0.getSize() / 2)),
						VectorUtils.add2Vectors(c_0_1.getPosition(),
								new Vector2(0, -c_0_1.getSize() / 2)))), 3,
						TrafficSimConstants.HIGHWAY_SPEED_LIMIT, c_0_0, c_0_1,
						Direction.UPSTREAM), v_0_0, v_0_1, true);

		g.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(c_0_1.getPosition(),
								new Vector2(0, -c_0_1.getSize() / 2)),
						VectorUtils.add2Vectors(c_0_0.getPosition(),
								new Vector2(0, c_0_0.getSize() / 2)))), 3,
						TrafficSimConstants.HIGHWAY_SPEED_LIMIT, c_0_1, c_0_0,
						Direction.DOWNSTREAM), v_0_1, v_0_0, true);

		CrossRoad c_1_0 = new CrossRoad(crossSize, new Vector2(400, 0),
				CrossRoad.CR_TYPE.CrossRoad);

		Vertex<NavigationObject> v_1_0 = g.addVertex(c_1_0);

		g.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(c_0_0.getPosition(),
								new Vector2(c_0_0.getSize() / 2, 0)),
						VectorUtils.add2Vectors(c_1_0.getPosition(),
								new Vector2(-c_1_0.getSize() / 2, 0)))), 3,
						TrafficSimConstants.HIGHWAY_SPEED_LIMIT, c_0_0, c_1_0,
						Direction.UPSTREAM), v_0_0, v_1_0, true);

		g.addEdge(
				new Road(new ParametricCurve(new C_Linear(VectorUtils
						.add2Vectors(c_1_0.getPosition(),
								new Vector2(-c_1_0.getSize() / 2, 0)),
						VectorUtils.add2Vectors(c_0_0.getPosition(),
								new Vector2(c_0_0.getSize() / 2, 0)))), 3,
						TrafficSimConstants.HIGHWAY_SPEED_LIMIT, c_1_0, c_0_0,
						Direction.DOWNSTREAM), v_1_0, v_0_0, true);

		return g;

	}

	public static Vertex<NavigationObject> addCrossRoad(
			Graph<NavigationObject> g, float size, Vector2 position,
			CR_TYPE type) {
		CrossRoad crossRoad = new CrossRoad(size, position, type);
		Vertex<NavigationObject> result = g.addVertex(crossRoad);
		return result;
	}

	public static Edge<NavigationObject> addEdge(Graph<NavigationObject> g,
			Vertex<NavigationObject> v1, Vertex<NavigationObject> v2,
			int numLanes, float speedLimit) {
		Vector2 p1 = v1.getData().getPosition().cpy();
		Vector2 p2 = v2.getData().getPosition().cpy();

		float angle1 = VectorUtils.getAngle(p1, p2);
		float angle2 = VectorUtils.getAngle(p2, p1);

		float shift1 = ((CrossRoad) v1.getData()).getSize() / 2;
		float shift2 = ((CrossRoad) v2.getData()).getSize() / 2;

		Vector2 shiftV1 = VectorUtils.getUnitVectorDegrees(angle1);
		Vector2 shiftV2 = VectorUtils.getUnitVectorDegrees(angle2);

		VectorUtils.multiplyVector(shiftV1, shift1);
		VectorUtils.multiplyVector(shiftV2, shift2);

		p1 = VectorUtils.add2Vectors(p1, shiftV1);
		p2 = VectorUtils.add2Vectors(p2, shiftV2);

		Road road = new Road(new ParametricCurve(new C_Linear(p1, p2)),
				numLanes, speedLimit, (CrossRoad) v1.getData(),
				(CrossRoad) v2.getData(), Direction.UPSTREAM);

		Edge<NavigationObject> result = g.addEdge(road, v1, v2, true);
		return result;
	}

	public static void addEdgeBoth(Graph<NavigationObject> g,
			Vertex<NavigationObject> v1, Vertex<NavigationObject> v2,
			int numLanes, float speedLimit) {
		addEdge(g, v1, v2, numLanes, speedLimit);
		addEdge(g, v2, v1, numLanes, speedLimit);
	}

	public static Graph<NavigationObject> addighway(Graph<NavigationObject> g,
			int width, int height, float distance, float startX, float startY) {

		float crossSize = 100.0f;

		Vertex<NavigationObject> v_0_0 = addCrossRoad(g, crossSize,
				new Vector2(startX - distance, startY - distance),
				CR_TYPE.CrossRoad);
		Vertex<NavigationObject> v_0_2 = addCrossRoad(g, crossSize,
				new Vector2(startX - distance, startY + (height) * distance),
				CR_TYPE.CrossRoad);
		Vertex<NavigationObject> v_2_0 = addCrossRoad(g, crossSize,
				new Vector2(startX + (width) * distance, startY - distance),
				CR_TYPE.CrossRoad);
		Vertex<NavigationObject> v_2_2 = addCrossRoad(g, crossSize,
				new Vector2(startX + (width) * distance, startY + (height)
						* distance), CR_TYPE.CrossRoad);

		Vertex<NavigationObject> v_0_1 = addCrossRoad(g, crossSize,
				VectorUtils.getMidPoint(v_0_0.getData().getPosition(), v_0_2
						.getData().getPosition()), CR_TYPE.HighWay_Cross);
		Vertex<NavigationObject> v_1_0 = addCrossRoad(g, crossSize,
				VectorUtils.getMidPoint(v_0_0.getData().getPosition(), v_2_0
						.getData().getPosition()), CR_TYPE.HighWay_Cross);
		Vertex<NavigationObject> v_2_1 = addCrossRoad(g, crossSize,
				VectorUtils.getMidPoint(v_2_0.getData().getPosition(), v_2_2
						.getData().getPosition()), CR_TYPE.HighWay_Cross);
		Vertex<NavigationObject> v_1_2 = addCrossRoad(g, crossSize,
				VectorUtils.getMidPoint(v_0_2.getData().getPosition(), v_2_2
						.getData().getPosition()), CR_TYPE.HighWay_Cross);

		addEdgeBoth(g, v_0_0, v_0_1, 3, TrafficSimConstants.HIGHWAY_SPEED_LIMIT);
		addEdgeBoth(g, v_0_1, v_0_2, 3, TrafficSimConstants.HIGHWAY_SPEED_LIMIT);
		addEdgeBoth(g, v_0_2, v_1_2, 3, TrafficSimConstants.HIGHWAY_SPEED_LIMIT);
		addEdgeBoth(g, v_1_2, v_2_2, 3, TrafficSimConstants.HIGHWAY_SPEED_LIMIT);
		addEdgeBoth(g, v_2_2, v_2_1, 3, TrafficSimConstants.HIGHWAY_SPEED_LIMIT);
		addEdgeBoth(g, v_2_1, v_2_0, 3, TrafficSimConstants.HIGHWAY_SPEED_LIMIT);
		addEdgeBoth(g, v_2_0, v_1_0, 3, TrafficSimConstants.HIGHWAY_SPEED_LIMIT);
		addEdgeBoth(g, v_1_0, v_0_0, 3, TrafficSimConstants.HIGHWAY_SPEED_LIMIT);

		addEdgeBoth(g, v_0_1, g.getVertex(height / 2), 2,
				TrafficSimConstants.CITY_SPEED_LIMIT);
		addEdgeBoth(g, v_1_0, g.getVertex((width / 2) * height), 2,
				TrafficSimConstants.CITY_SPEED_LIMIT);
		addEdgeBoth(g, v_2_1, g.getVertex((width - 1) * height + height / 2),
				2, TrafficSimConstants.CITY_SPEED_LIMIT);
		addEdgeBoth(g, v_1_2, g.getVertex((width / 2) * height + height - 1),
				2, TrafficSimConstants.CITY_SPEED_LIMIT);
		
		
		Vertex<NavigationObject> v_m1_1 = addCrossRoad(g, 8*TrafficSimConstants.LANE_WIDTH,new Vector2(startX-2*distance,VectorUtils.getMidPoint(v_0_0.getData().getPosition(), v_0_2.getData().getPosition()).y),CR_TYPE.CrossRoad);
		
		addEdgeBoth(g,v_0_1, v_m1_1,2,TrafficSimConstants.CITY_SPEED_LIMIT);

		return g;
	}

}
