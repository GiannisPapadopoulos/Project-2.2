package functions;

import trafficsim.components.PhysicsBodyComponent;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * Computes the predicted position of an entity after time dT by adding the velocity multiplied by dT
 * to the current position
 * 
 * @author Giannis Papadopoulos
 */
public class GetPredictedPosition {

	public Vector2 apply(Entity entity, float dT) {
		PhysicsBodyComponent physComp = entity.getComponent(PhysicsBodyComponent.class);
		assert physComp != null;
		return physComp.getPosition().cpy().add(physComp.getLinearVelocity().scl(dT));
	}

}
