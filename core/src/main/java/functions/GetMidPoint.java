package functions;

import trafficsim.roads.Road;

import com.badlogic.gdx.math.Vector2;

public class GetMidPoint
		implements Function<Vector2, Road> {

	@Override
	public Vector2 apply(Road road) {
		float xm = (road.getPointA().x + road.getPointB().x) / 2;
		float ym = (road.getPointA().y + road.getPointB().y) / 2;
		return new Vector2(xm, ym);
	}

}
