package trafficsim.roads;

import com.badlogic.gdx.math.Vector2;

public abstract class NavigationObject {

	public abstract Vector2 getPosition();

	public abstract float getLength();

	public abstract float getSpeedLimit();
}
