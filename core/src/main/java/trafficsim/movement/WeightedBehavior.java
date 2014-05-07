package trafficsim.movement;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Class that allows following multiple behaviors at the same time, with the resulting steering
 * vector being their weighted sum
 * 
 * @author Giannis Papadopoulos
 */
public class WeightedBehavior
		extends Behavior {


	/** Map from behaviors to weights */
	private ObjectMap<Behavior, Float> behaviors = new ObjectMap<>();

	public void add(Behavior behavior, float weight) {
		behaviors.put(behavior, weight);
	}

	/** The steering force is the weighted sum of the vectors from each behavior */
	@Override
	public Vector2 steeringForce(Vector2 position) {
		Vector2 force = new Vector2(0, 0);
		for (Behavior behavior : behaviors.keys()) {
			force.add(behavior.steeringForce(position).scl(behaviors.get(behavior)));
		}
		return force;
	}

}
