package functions;

import trafficsim.roads.Road;

import com.badlogic.gdx.math.Vector2;

public class GetVector
		implements Function<Vector2, Road> {

	@Override
	public Vector2 apply(Road road) {
		return road.getPointB().cpy().sub(road.getPointA());
	}


}
