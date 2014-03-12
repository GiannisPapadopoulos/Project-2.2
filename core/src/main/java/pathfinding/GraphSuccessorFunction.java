package pathfinding;

import functions.CalculateExpectedTime;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import search.SuccessorFunction;
import trafficsim.roads.Road;

public class GraphSuccessorFunction
		extends SuccessorFunction<GraphState, GraphAction> {

	public GraphSuccessorFunction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Iterable<ImmutableTriple<GraphState, GraphAction, Double>> apply(GraphState s) {
		List<ImmutableTriple<GraphState, GraphAction, Double>> successors = new ArrayList<>();
		Vertex<Road> v = s.getVertex();
		Graph<Road> graph = v.getParent();
		for (int i = 0; i < v.getAdjacentVertices().size(); i++) {
			Vertex<Road> adj = graph.getVertex(v.getAdjacentVertices().get(i));
			Edge<Road> edge = graph.getEdge(v.getAdjacentEdges().get(i));
			double cost = new CalculateExpectedTime().apply(edge.getData());
			successors.add(ImmutableTriple.of(new GraphState(adj), new GraphAction(edge), cost));
		}
		return successors;
	}

}
