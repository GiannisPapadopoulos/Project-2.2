package pathfinding;

import lombok.AllArgsConstructor;
import search.GoalTest;

@AllArgsConstructor
public class GraphGoalTest
		extends GoalTest<GraphState> {

	private GraphState target;

	@Override
	public boolean apply(GraphState s) {
		return s.equals(target);
	}

}
