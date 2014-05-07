package trafficsim.movement;

import com.badlogic.gdx.math.Vector2;

public class SeekBehavior
		extends FixedTargetBehavior {

	@Override
	public Vector2 steeringForce(Vector2 position) {
		return SteeringFunctions.seek(position, getTargetLocation());
	}
}
