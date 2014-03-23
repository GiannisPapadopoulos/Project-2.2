package trafficsim.callbacks;

import lombok.Getter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

@Getter
public class TrafficRayCastCallBack
		implements RayCastCallback {

	private int closestId = -1;

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		Object userData = fixture.getBody().getUserData();
		if (userData != null && userData.getClass() == Integer.class) {
			// System.out.println(fraction + " " + fixture.getBody().getUserData() + " " +
			// fixture.getBody().getType());
			closestId = (Integer) userData;
			return fraction;
		}
		else
			return -1;
	}

}