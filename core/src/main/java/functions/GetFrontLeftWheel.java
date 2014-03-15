package functions;

import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.CAR_WIDTH;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class GetFrontLeftWheel
		implements Function<Vector2, Body> {

	@Override
	public Vector2 apply(Body body) {
		return body.getWorldPoint(new Vector2(CAR_LENGTH / 2, CAR_WIDTH / 2));
	}

}
