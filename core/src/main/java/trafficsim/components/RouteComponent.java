package trafficsim.components;

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
import trafficsim.roads.NavigationObject;

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
	private Vertex<NavigationObject> source;

	// @NonNull
	private Vertex<NavigationObject> target;

	@Delegate
	private Path<GraphState, GraphAction> path;

	/** */
	private Vertex<NavigationObject> currentVertex;
	/** */
	// @Setter(AccessLevel.NONE)
	private int edgeIndex;

	/** Waypoints along the current edge */
	private List<Vector2> wayPoints;
	/** Index of the current waypoint */
	private int wayPointIndex;

	/** If false, path will be recomputed */
	private boolean set;

	// /** */
	// private boolean first = true;

	public RouteComponent(Vertex<NavigationObject> source,
			Vertex<NavigationObject> target) {
		super();
		this.source = source;
		this.target = target;
	}

	public Edge<NavigationObject> getCurrentEdge() {
		return path.getRoute().get(edgeIndex).getEdge();
	}

	public Vertex<NavigationObject> getNextVertex() {
		return currentVertex == target ? currentVertex : currentVertex
				.getNeighbor(getCurrentEdge());
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

	public List<Vector2> getAllVertices() {
		List<Vector2> remainingVertices = new ArrayList<Vector2>();
		Vertex<NavigationObject> v = currentVertex;
		for (int i = edgeIndex; i < path.getRoute().size(); i++) {
			Edge<NavigationObject> edge = path.getRoute().get(i).getEdge();
			remainingVertices.add(v.getData().getPosition());
			v = v.getNeighbor(edge);
		}
		return remainingVertices;
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

}
