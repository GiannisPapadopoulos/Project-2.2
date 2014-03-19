package pathfinding;

import lombok.AllArgsConstructor;
import search.HeuristicFunction;

import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

@AllArgsConstructor
public class GraphEuclideanHeuristic
		extends HeuristicFunction<GraphState> {

	private GraphState target;

	@Override
	public double evaluate(GraphState s) {
		Vector2 currentPos = VectorUtils.getMidPoint(s.getVertex().getData());
		Vector2 targetPos = VectorUtils.getMidPoint(target.getVertex().getData());
		// TODO calculate expected speed maybe ?
		return currentPos.dst(targetPos);
	}


}
