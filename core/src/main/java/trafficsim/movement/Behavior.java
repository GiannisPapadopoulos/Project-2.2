package trafficsim.movement;

import trafficsim.components.PhysicsBodyComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.badlogic.gdx.math.Vector2;

/**
 * Steering behavior interface
 * 
 * @author Giannis Papadopoulos
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// @AllArgsConstructor
public abstract class Behavior {

	public abstract Vector2 steeringForce(PhysicsBodyComponent physComp);
}
