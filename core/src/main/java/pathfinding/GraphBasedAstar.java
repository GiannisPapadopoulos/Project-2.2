package pathfinding;

import graph.Vertex;
import lombok.NoArgsConstructor;
import search.AstarSearch;
import search.Path;
import trafficsim.roads.NavigationObject;

@NoArgsConstructor
public class GraphBasedAstar {

	private AstarSearch<GraphState, GraphAction, GraphSuccessorFunction, GraphEuclideanHeuristic, GraphGoalTest> aStarInstance;

	private void init(GraphState targetState) {
		aStarInstance = new AstarSearch<>(new GraphSuccessorFunction(), new GraphEuclideanHeuristic(targetState),
											new GraphGoalTest(targetState));
	}

	public Path<GraphState, GraphAction> findRoute(Vertex<NavigationObject> source, Vertex<NavigationObject> target) {
		init(new GraphState(target));
		return aStarInstance.findPath(new GraphState(source));
	}


}
