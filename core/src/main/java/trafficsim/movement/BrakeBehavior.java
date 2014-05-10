package trafficsim.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import trafficsim.components.PhysicsBodyComponent;

import com.badlogic.gdx.math.Vector2;


/**
 * Used to decelerate or stop
 * 
 * @author Giannis Papadopoulos
 */
@AllArgsConstructor
public class BrakeBehavior
		extends Behavior {

	@Getter
	@Setter
	/** Velocity is multiplied by this factor, should be in (0, 1) */
	private float scalingFactor;

	@Getter
	@Setter
	/** Maximum desired speed, defaults to 0 */
	private float maxSpeed;

	public BrakeBehavior(float scalingFactor) {
		this.scalingFactor = scalingFactor;
	}

	@Override
	public Vector2 steeringForce(PhysicsBodyComponent physComp) {
		Vector2 velocity = physComp.getLinearVelocity().cpy();
		if (velocity.len() > maxSpeed) {
			return velocity.scl(-scalingFactor);
		}
		return new Vector2(0, 0);
	}

}
