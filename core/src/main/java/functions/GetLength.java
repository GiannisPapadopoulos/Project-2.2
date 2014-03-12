package functions;

import trafficsim.roads.Road;

public class GetLength
		implements Function<Float, Road> {

	@Override
	public Float apply(Road road) {
		return road.getPointA().dst(road.getPointB());
	}

}
