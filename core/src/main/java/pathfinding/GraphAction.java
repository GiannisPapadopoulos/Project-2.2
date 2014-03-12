package pathfinding;

import graph.Edge;
import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;
import search.IAction;
import trafficsim.roads.Road;

@Getter
@AllArgsConstructor
public class GraphAction
		implements IAction<GraphState> {

	@Delegate
	private Edge<Road> edge;

}
