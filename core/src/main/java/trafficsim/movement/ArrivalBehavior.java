package trafficsim.movement;

import trafficsim.components.PhysicsBodyComponent;

import com.badlogic.gdx.math.Vector2;

/**
 * Arrival behavior, to slow down when reaching the target
 * 
 * @author Giannis Papadopoulos
 */
public class ArrivalBehavior
		extends FixedTargetBehavior {

	@Override
	public Vector2 steeringForce(PhysicsBodyComponent physComp) {
		return SteeringFunctions.arrive(physComp.getPosition(), getTargetLocation());
	}

}
