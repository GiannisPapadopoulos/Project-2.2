package trafficsim.systems;

import com.badlogic.gdx.math.Vector2;

public class SeekBehavior {

	public static Vector2 getForce(Vector2 position, Vector2 target) {
		return target.cpy().sub(position);
	}
}
