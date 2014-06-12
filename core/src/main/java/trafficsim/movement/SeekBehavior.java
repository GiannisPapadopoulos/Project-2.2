package trafficsim.movement;

import trafficsim.components.PhysicsBodyComponent;

import com.badlogic.gdx.math.Vector2;

/**
 * Behavior for seeking a fixed target
 * 
 * @author Giannis Papadopoulos
 */
public class SeekBehavior
		extends FixedTargetBehavior {

	@Override
	public Vector2 steeringForce(PhysicsBodyComponent physComp) {
		if (getTargetLocation() == null)
			return new Vector2(0, 0);
		return SteeringFunctions.seek(physComp.getPosition(), getTargetLocation());
	}
}
