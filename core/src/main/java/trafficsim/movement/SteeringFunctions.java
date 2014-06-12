package trafficsim.movement;

import com.badlogic.gdx.math.Vector2;

public class SteeringFunctions {

	/**
	 * Return a steering vector that seeks the desired location
	 */
	public static Vector2 seek(Vector2 currentPosition, Vector2 desired) {
		return desired.cpy().sub(currentPosition);
	}

	/**
	 * Return a steering vector that flees the given location
	 */
	public static Vector2 flee(Vector2 currentPosition, Vector2 undesired) {
		return seek(currentPosition, undesired).scl(-1);
	}

	/**
	 * Return a steering vector that seeks the future location of the given quarry
	 */
	public static Vector2 pursue(Vector2 currentPosition, Vector2 targetPosition, Vector2 targetVelocity) {
		return seek(currentPosition, targetPosition.cpy().add(targetVelocity));
	}

	/**
	 * Return a steering vector fleeing from the given predator
	 */
	public static Vector2 evade(Vector2 currentPosition, Vector2 targetPosition, Vector2 targetVelocity) {
		return flee(currentPosition, targetPosition.cpy().add(targetVelocity));
	}

	/**
	 * Return a steering vector that reduces the seeker's velocity as it approaches
	 * the given destination
	 */
	public static Vector2 arrive(Vector2 currentPosition, Vector2 destination) {
		return arrive(currentPosition, destination, 1.0f);
	}

	/**
	 * Return a steering vector that reduces the seeker's velocity as it approaches
	 * the given destination
	 * 
	 * @param speedCoefficient
	 *        multiplies the speed at which the seeker approaches the destination
	 */
	public static Vector2 arrive(Vector2 currentPosition, Vector2 destination, float speedCoefficient) {
		// desired speed is directly proportional to distance from destination
		float desiredSpeed = speedCoefficient * destination.cpy().sub(currentPosition).len();
		return seek(currentPosition, destination).nor().scl(desiredSpeed);
	}
}
