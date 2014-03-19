package pathfinding;

import graph.Edge;
import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;
import lombok.ToString;
import search.IAction;
import trafficsim.roads.Road;

@Getter
@AllArgsConstructor
@ToString
public class GraphAction
		implements IAction<GraphState> {

	@Delegate
	private Edge<Road> edge;

}
