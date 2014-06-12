package editor;

import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import functions.VectorUtils;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

import lombok.Data;
import trafficsim.TrafficSimConstants;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;

import com.badlogic.gdx.math.Vector2;

@Data
public class PointsOfInterest {

	private ArrayList<PointOfInterest> POI;
	private Graph<NavigationObject> graph;

	public PointsOfInterest(Graph<NavigationObject> graph) {
		this.graph = graph;
		POI = new ArrayList<PointOfInterest>();

		updatePOI();


	}
	
	private void updatePOI() {
		boolean found = false;
		for (Vertex<NavigationObject> v : graph.getVertexList()) {

			if (v.getData() instanceof Road) {
				Road road = (Road) v.getData();
				for (Vector2 vec : new Vector2[] { road.getPointA(), road.getPointB(), road.getPointC(),
													road.getPointD() }) {
					found = false;
					for (PointOfInterest poi : POI)
						if (vec.x == poi.position.x && vec.y == poi.position.y) {
							poi.vertices.add(v);
							found = true;
							break;
						}
						else {
						}
					if (!found) {
						PointOfInterest poi = new PointOfInterest(vec);
						poi.addGraphObject(v);
						POI.add(poi);
					}
				}
			}
		}
	}

	@Data
	public class PointOfInterest {

		private Vector2 position;
		private ArrayList<Vertex<NavigationObject>> vertices;
		private ArrayList<Edge<Road>> edges;

		public PointOfInterest(Vector2 position) {
			this.position = position;
			vertices = new ArrayList<Vertex<NavigationObject>>();
			edges = new ArrayList<Edge<Road>>();
		}

		public void addGraphObject(Edge<Road> e) {
			edges.add(e);
		}

		public void addGraphObject(Vertex<NavigationObject> v) {
			vertices.add(v);
		}

	}
	
	public PointOfInterest registerNewPOI(Vector2 position) {
		PointOfInterest newPOI = new PointOfInterest(position);
		POI.add(newPOI);
		return newPOI;
	}
	
	public Graph<NavigationObject> createGraphObject(PointOfInterest poi1, PointOfInterest poi2) {
		
		float angle;
		
		if(poi1.getVertices().size()==0) {
			angle = VectorUtils.getAngle(poi2.position, poi1.position);
			Vector2 endPoint = VectorUtils.getUnitVector(angle);
			endPoint.x*=TrafficSimConstants.LANE_WIDTH*2;
			endPoint.y*=TrafficSimConstants.LANE_WIDTH*2;
			endPoint.add(poi1.position);
			//TODO poi1.vertices.add(graph.addVertex(new Road(poi1.position, endPoint, 1, TrafficSimConstants.CITY_SPEED_LIMIT)));
		}
		if(poi2.getVertices().size()==0) {
			angle = VectorUtils.getAngle(poi1.position, poi2.position);
			Vector2 endPoint = VectorUtils.getUnitVector(angle);
			endPoint.x*=TrafficSimConstants.LANE_WIDTH*2;
			endPoint.y*=TrafficSimConstants.LANE_WIDTH*2;
			endPoint.add(poi2.position);
			//TODO poi2.vertices.add(graph.addVertex(new Road(endPoint, poi2.position, 1, TrafficSimConstants.CITY_SPEED_LIMIT)));
		}

		updatePOI();

		// graph.addEdge(new Road(poi1.position, poi2.position,1, Direction.BOTH,
		// TrafficSimConstants.CITY_SPEED_LIMIT),poi1.getVertices().get(0),poi2.getVertices().get(0),false);

		Vector2 roadVector = poi1.position.cpy().sub(poi2.position);
		Vector2 perpendicular = roadVector.cpy().rotate(90).nor().scl(LANE_WIDTH / 2);

		Vector2 pointA1 = poi1.position.cpy().add(perpendicular);
		Vector2 pointB1 = poi2.position.cpy().add(perpendicular);

		Vector2 pointA2 = poi1.position.cpy().sub(perpendicular);
		Vector2 pointB2 = poi2.position.cpy().sub(perpendicular);

		// TODO

		// graph.addEdge( new Road(pointA1, pointB1, 1, Direction.DOWNSTREAM,
		// TrafficSimConstants.CITY_SPEED_LIMIT), poi1.getVertices().get(0), poi2.getVertices()
		// .get(0), true);
		// graph.addEdge( new Road(pointB2, pointA2, 1, Direction.UPSTREAM,
		// TrafficSimConstants.CITY_SPEED_LIMIT), poi2.getVertices().get(0), poi1.getVertices()
		// .get(0), true);

		return graph;
	}
}
