package trafficsim.components;

import graph.Edge;
import graph.Vertex;
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

	private Vector2 wayPoint;

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

	public void update() {
		assert !isLastEdge();
		// if (first) {
		// first = false;
		// }
		// else {
		setCurrentVertex(getNextVertex());
		setEdgeIndex(getEdgeIndex() + 1);
		// first = true;
		// }
	}

	public boolean isLastEdge() {
		return edgeIndex >= path.getRoute().size() - 1;
	}

}
