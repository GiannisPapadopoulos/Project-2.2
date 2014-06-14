package trafficsim.components;

import functions.VectorUtils;
import graph.Edge;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

import lombok.Delegate;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pathfinding.GraphAction;
import pathfinding.GraphState;
import search.Path;
import trafficsim.roads.Road;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Component that holds information about the path
 * 
 * @author Giannis Papadopoulos
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class RouteComponent
		extends Component {


	@NonNull
	/** The start of the route */
	private Vertex<Road> source;

	/** The last vertex of the route */
	private Vertex<Road> target;

	@Delegate
	private Path<GraphState, GraphAction> path;

	/** The current vertex along the path */
	private Vertex<Road> currentVertex;
	/** Index of the current edge in the route */
	private int edgeIndex;

	/** Waypoints along the current edge */
	private List<Vector2> wayPoints;
	/** Index of the current waypoint */
	private int wayPointIndex;

	/** If false, path will be recomputed */
	private boolean set;

	// /** */
	// private boolean first = true;

	public RouteComponent(Vertex<Road> source, Vertex<Road> target) {
		super();
		this.source = source;
		this.target = target;
	}

	public Edge<Road> getCurrentEdge() {
		return path.getRoute().get(edgeIndex).getEdge();
	}

	public Vertex<Road> getNextVertex() {
		return currentVertex == target ? currentVertex : currentVertex.getNeighbor(getCurrentEdge());
	}

	public Vector2 getNextWaypoint() {
		return wayPoints.get(wayPointIndex);
	}

	public Vector2 getRoadEndPoint() {
		return wayPoints.get(wayPoints.size() - 1);
	}

	public void incrementWaypointIndex() {
		wayPointIndex++;
	}

	public void incrementEdgeIndex() {
		edgeIndex++;
	}

	// public void update() {
	// assert !isLastEdge();
	// // if (first) {
	// // first = false;
	// // }
	// // else {
	// setCurrentVertex(getNextVertex());
	// setEdgeIndex(getEdgeIndex() + 1);
	// // first = true;
	// // }
	// }

	public boolean isLastEdge() {
		return edgeIndex >= path.getRoute().size() - 1;
	}
	
	public List<Vector2> getAllVertices() {
		List<Vector2> remainingVertices = new ArrayList<Vector2>();
		Vertex<Road> v = currentVertex;
		for (int i = edgeIndex; i < path.getRoute().size(); i++) {
			Edge<Road> edge = path.getRoute().get(i).getEdge();
			remainingVertices.add(VectorUtils.getMidPoint(v.getData()));
			v = v.getNeighbor(edge);
		}
		remainingVertices.add(VectorUtils.getMidPoint(v.getData()));
		return remainingVertices;
	}

}
