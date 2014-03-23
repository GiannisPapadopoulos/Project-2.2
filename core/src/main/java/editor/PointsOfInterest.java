package editor;

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
		boolean found = false;

		for (Vertex<Road> v : graph.getVertexList())
			for (Vector2 vec : new Vector2[] { v.getData().getPointA(),
					v.getData().getPointB() }) {
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
		newPOI.vertices.add(graph.addVertex(new Road(position, position, 1, Direction.BOTH, TrafficSimConstants.CITY_SPEED_LIMIT)));
		return newPOI;
	}
	
	public Graph<Road> createGraphObject(PointOfInterest poi1, PointOfInterest poi2) {
		graph.addEdge(new Road(poi1.position, poi2.position,1, Direction.BOTH, TrafficSimConstants.CITY_SPEED_LIMIT),poi1.getVertices().get(0),poi2.getVertices().get(0),false);
		return graph;
	}
}
