package functions;

import trafficsim.roads.Road;

public class CalculateExpectedTime
		implements Function<Float, Road> {

	@Override
	public Float apply(Road road) {
		return new GetLength().apply(road) / road.getSpeedLimit();
	}


}
