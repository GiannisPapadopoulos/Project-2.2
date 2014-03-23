package editor;

import functions.VectorUtils;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

import lombok.Data;
import trafficsim.TrafficSimConstants;
import trafficsim.roads.Road;
import trafficsim.roads.Road.Direction;

import com.badlogic.gdx.math.Vector2;

@Data
public class PointsOfInterest {

	private ArrayList<PointOfInterest> POI;
	private Graph<Road> graph;

	public PointsOfInterest(Graph<Road> graph) {
		this.graph = graph;
		POI = new ArrayList<PointOfInterest>();

		updatePOI();


	}
	
	private void updatePOI() {
		boolean found = false;
		for (Vertex<Road> v : graph.getVertexList())
			for (Vector2 vec : new Vector2[] { v.getData().getPointA(),
					v.getData().getPointB(), v.getData().getPointC(), v.getData().getPointD() }) {
				found = false;
				for (PointOfInterest poi : POI)
					if (vec.x == poi.position.x && vec.y == poi.position.y) {
						poi.vertices.add(v);
						found = true;
						break;
					} else {
					}
				if (!found) {
					PointOfInterest poi = new PointOfInterest(vec);
					poi.addGraphObject(v);
					POI.add(poi);
				}
			}
	}

	@Data
	public class PointOfInterest {

		private Vector2 position;
		private ArrayList<Vertex<Road>> vertices;
		private ArrayList<Edge<Road>> edges;

		public PointOfInterest(Vector2 position) {
			this.position = position;
			vertices = new ArrayList<Vertex<Road>>();
			edges = new ArrayList<Edge<Road>>();
		}

		public void addGraphObject(Edge<Road> e) {
			edges.add(e);
		}

		public void addGraphObject(Vertex<Road> v) {
			vertices.add(v);
		}

	}
	
	public PointOfInterest registerNewPOI(Vector2 position) {
		PointOfInterest newPOI = new PointOfInterest(position);
		POI.add(newPOI);
		return newPOI;
	}
	
	public Graph<Road> createGraphObject(PointOfInterest poi1, PointOfInterest poi2) {
		
		float angle;
		
		if(poi1.getVertices().size()==0) {
			angle = VectorUtils.getAngle(poi2.position, poi1.position);
			Vector2 endPoint = VectorUtils.getUnitVector(angle);
			endPoint.x*=TrafficSimConstants.LANE_WIDTH*2;
			endPoint.y*=TrafficSimConstants.LANE_WIDTH*2;
			endPoint.add(poi1.position);
			poi1.vertices.add(graph.addVertex(new Road(poi1.position, endPoint, 1, Direction.BOTH, TrafficSimConstants.CITY_SPEED_LIMIT)));
		}
		if(poi2.getVertices().size()==0) {
			angle = VectorUtils.getAngle(poi1.position, poi2.position);
			Vector2 endPoint = VectorUtils.getUnitVector(angle);
			endPoint.x*=TrafficSimConstants.LANE_WIDTH*2;
			endPoint.y*=TrafficSimConstants.LANE_WIDTH*2;
			endPoint.add(poi2.position);
			poi2.vertices.add(graph.addVertex(new Road(endPoint, poi2.position, 1, Direction.BOTH, TrafficSimConstants.CITY_SPEED_LIMIT)));
		}

		updatePOI();

		graph.addEdge(new Road(poi1.position, poi2.position,1, Direction.BOTH, TrafficSimConstants.CITY_SPEED_LIMIT),poi1.getVertices().get(0),poi2.getVertices().get(0),false);
		return graph;
	}
}
