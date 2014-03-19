package trafficsim.components;

import graph.Edge;
import graph.Vertex;
import lombok.Delegate;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pathfinding.GraphAction;
import pathfinding.GraphState;
import search.Path;
import trafficsim.roads.Road;

import com.artemis.Component;

@Getter
@Setter
@RequiredArgsConstructor
public class RouteComponent
		extends Component {

	@NonNull
	private Vertex<Road> source;

	@NonNull
	private Vertex<Road> target;

	@Delegate
	private Path<GraphState, GraphAction> path;

	/** */
	private Vertex<Road> currentVertex;
	/** */
	private int edgeIndex;

	/** If false, path will be recomputed */
	private boolean set;

	public Edge<Road> getCurrentEdge() {
		return path.getRoute().get(edgeIndex).getEdge();
	}

	public Vertex<Road> getNextVertex() {
		return currentVertex == target ? currentVertex : currentVertex.getNeighbor(getCurrentEdge());
	}

}
