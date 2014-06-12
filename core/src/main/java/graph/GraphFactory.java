package graph;

import paramatricCurves.ParametricCurve;
import paramatricCurves.curveDefs.C_Linear;
import trafficsim.TrafficSimConstants;
import trafficsim.roads.CrossRoad;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;

import com.badlogic.gdx.math.Vector2;

import de.matthiasmann.twlthemeeditor.gui.CollapsiblePanel.Direction;
import editor.EditorData;
import functions.VectorUtils;

public class GraphFactory {

	public static Graph<NavigationObject> createNewSystem() {
		Graph<NavigationObject> graph = new Graph<NavigationObject>();

		CrossRoad cr1 = new CrossRoad(30.0f, new Vector2(00.0f, 00.0f));
		CrossRoad cr2 = new CrossRoad(30.0f, new Vector2(200.0f, 00.0f));
		CrossRoad cr3 = new CrossRoad(30.0f, new Vector2(-200.0f, 00.0f));
		CrossRoad cr4 = new CrossRoad(30.0f, new Vector2(00.0f, 200.0f));
		CrossRoad cr5 = new CrossRoad(30.0f, new Vector2(00.0f, -200.0f));

		Vector2 test = new Vector2(000.0f, 300.0f);
		EditorData.debugPoints.add(test);

		float s = TrafficSimConstants.LANE_WIDTH / 2;

		Vector2 p1 = new Vector2(cr1.getPosition().x - cr1.getSize() / 2,
				cr1.getPosition().y - s);
		Vector2 p2 = new Vector2(cr1.getPosition().x + cr1.getSize() / 2,
				cr1.getPosition().y - s);
		Vector2 p3 = new Vector2(cr1.getPosition().x - cr1.getSize() / 2,
				cr1.getPosition().y + s);
		Vector2 p4 = new Vector2(cr1.getPosition().x + cr1.getSize() / 2,
				cr1.getPosition().y + s);
		Vector2 p5 = new Vector2(cr1.getPosition().x - s, cr1.getPosition().y
				- cr1.getSize() / 2);
		Vector2 p6 = new Vector2(cr1.getPosition().x + s, cr1.getPosition().y
				- cr1.getSize() / 2);
		Vector2 p7 = new Vector2(cr1.getPosition().x - s, cr1.getPosition().y
				+ cr1.getSize() / 2);
		Vector2 p8 = new Vector2(cr1.getPosition().x + s, cr1.getPosition().y
				+ cr1.getSize() / 2);
		Vector2 p9 = new Vector2(cr2.getPosition().x - cr2.getSize() / 2,
				cr2.getPosition().y - s);
		Vector2 p10 = new Vector2(cr2.getPosition().x - cr2.getSize() / 2,
				cr2.getPosition().y + s);
		Vector2 p11 = new Vector2(cr3.getPosition().x + cr3.getSize() / 2,
				cr2.getPosition().y - s);
		Vector2 p12 = new Vector2(cr3.getPosition().x + cr3.getSize() / 2,
				cr2.getPosition().y + s);
		Vector2 p13 = new Vector2(cr4.getPosition().x - s, cr4.getPosition().y
				- cr2.getSize() / 2);
		Vector2 p14 = new Vector2(cr4.getPosition().x + s, cr4.getPosition().y
				- cr2.getSize() / 2);
		Vector2 p15 = new Vector2(cr5.getPosition().x - s, cr5.getPosition().y
				+ cr2.getSize() / 2);
		Vector2 p16 = new Vector2(cr5.getPosition().x + s, cr5.getPosition().y
				+ cr2.getSize() / 2);

		EditorData.debugPoints2.add(p1);
		EditorData.debugPoints2.add(p2);
		EditorData.debugPoints2.add(p3);
		EditorData.debugPoints2.add(p4);
		EditorData.debugPoints2.add(p5);
		EditorData.debugPoints2.add(p6);
		EditorData.debugPoints2.add(p7);
		EditorData.debugPoints2.add(p8);
		EditorData.debugPoints2.add(p9);
		EditorData.debugPoints2.add(p10);
		EditorData.debugPoints2.add(p11);
		EditorData.debugPoints2.add(p12);
		EditorData.debugPoints2.add(p13);
		EditorData.debugPoints2.add(p14);
		EditorData.debugPoints2.add(p15);
		EditorData.debugPoints2.add(p16);

		Integer laneCount = 1;
		Road r1 = new Road(new ParametricCurve(new C_Linear(p11, p1)), laneCount,
				TrafficSimConstants.CITY_SPEED_LIMIT, cr3, cr1);
		Road r2 = new Road(new ParametricCurve(new C_Linear(p3, p12)), laneCount,
				TrafficSimConstants.CITY_SPEED_LIMIT, cr1, cr3);
		Road r3 = new Road(new ParametricCurve(new C_Linear(p16, p6)), laneCount,
				TrafficSimConstants.CITY_SPEED_LIMIT, cr5, cr1);
		Road r4 = new Road(new ParametricCurve(new C_Linear(p5, p15)), laneCount,
				TrafficSimConstants.CITY_SPEED_LIMIT, cr1, cr5);
		Road r5 = new Road(new ParametricCurve(new C_Linear(p2, p9)), laneCount,
				TrafficSimConstants.CITY_SPEED_LIMIT, cr1, cr2);
		Road r6 = new Road(new ParametricCurve(new C_Linear(p10, p4)), laneCount,
				TrafficSimConstants.CITY_SPEED_LIMIT, cr2, cr1);
		Road r7 = new Road(new ParametricCurve(new C_Linear(p8, p14)), laneCount,
				TrafficSimConstants.CITY_SPEED_LIMIT, cr1, cr4);
		Road r8 = new Road(new ParametricCurve(new C_Linear(p13, p7)), laneCount,
				TrafficSimConstants.CITY_SPEED_LIMIT, cr4, cr1);

		graph.addVertex(cr1);
		graph.addVertex(cr2);
		graph.addVertex(cr3);
		graph.addVertex(cr4);
		graph.addVertex(cr5);

		graph.addEdge(r1, graph.getVertex(2), graph.getVertex(0), false);
		graph.addEdge(r2, graph.getVertex(0), graph.getVertex(2), false);
		graph.addEdge(r3, graph.getVertex(4), graph.getVertex(0), false);
		graph.addEdge(r4, graph.getVertex(0), graph.getVertex(4), false);
		graph.addEdge(r5, graph.getVertex(0), graph.getVertex(1), false);
		graph.addEdge(r6, graph.getVertex(1), graph.getVertex(0), false);
		graph.addEdge(r7, graph.getVertex(0), graph.getVertex(3), false);
		graph.addEdge(r8, graph.getVertex(3), graph.getVertex(0), false);

		cr1.addConnection(r1, true);
		cr1.addConnection(r3, true);
		cr1.addConnection(r6, true);
		cr1.addConnection(r8, true);
		cr1.addConnection(r2, false);
		cr1.addConnection(r4, false);
		cr1.addConnection(r5, false);
		cr1.addConnection(r7, false);

		// cr2.addConnection(r5, true);
		// cr2.addConnection(r6, false);

		// cr3.addConnection(r2, true);
		// cr3.addConnection(r1, false);

		// cr4.addConnection(r7, true);
		// cr4.addConnection(r8, false);

		// cr5.addConnection(r4, true);
		// cr5.addConnection(r3, false);

		return graph;
	}

	public static Graph<NavigationObject> createManhattanGraph(int width,
			int height, float distance, float startX, float startY) {
		Graph<NavigationObject> graph = new Graph<NavigationObject>();
		float halfW = TrafficSimConstants.LANE_WIDTH*2;
		// Create vertices
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float x = startX + i * distance;
				float y = startY + j * distance;

				graph.addVertex(new CrossRoad(10.0f, new Vector2(x, y)));

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

					graph.addEdge(
							new Road(new ParametricCurve(new C_Linear(pointA,
									pointB)), 1,
									TrafficSimConstants.CITY_SPEED_LIMIT,
									(CrossRoad) v1.getData(), (CrossRoad) v2
											.getData()), v1, v2, false);
					graph.addEdge(
							new Road(new ParametricCurve(new C_Linear(pointB,
									pointA)), 1,
									TrafficSimConstants.CITY_SPEED_LIMIT,
									(CrossRoad) v2.getData(), (CrossRoad) v1
											.getData()), v2, v1, false);
				}
				if (j < height - 1) {
					Vertex<NavigationObject> v3 = graph.getVertex(i * height + j + 1);
					Vector2 pointA = v1.getData().getPosition();
					Vector2 pointB = v3.getData().getPosition();
					pointA.set(pointA.x, pointA.y + halfW);
					pointB.set(pointB.x, pointB.y - halfW);
					graph.addEdge(new Road(new ParametricCurve(new C_Linear(pointA,
							pointB)), 1,
							TrafficSimConstants.CITY_SPEED_LIMIT,
							(CrossRoad) v1.getData(), (CrossRoad) v3
									.getData()), v1, v3, false);
					graph.addEdge(new Road(new ParametricCurve(new C_Linear(pointB,
							pointA)), 1,
							TrafficSimConstants.CITY_SPEED_LIMIT,
							(CrossRoad) v3.getData(), (CrossRoad) v1
									.getData()), v3, v1, false);
				}
			}

		}
		return graph;
	}
}
