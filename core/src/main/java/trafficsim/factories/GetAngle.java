package trafficsim.factories;

import trafficsim.roads.Road;
import functions.Function;
import functions.GetVector;

public class GetAngle
		implements Function<Float, Road> {

	@Override
	public Float apply(Road road) {
		return new GetVector().apply(road).angle();
	}

}
