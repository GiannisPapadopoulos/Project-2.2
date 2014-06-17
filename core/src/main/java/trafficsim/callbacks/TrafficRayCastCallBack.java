package trafficsim.callbacks;

import graph.EntityIdentificationData;
import graph.EntityIdentificationData.EntityType;
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
		if (userData != null && userData.getClass() == EntityIdentificationData.class) {
			EntityIdentificationData idData = (EntityIdentificationData) userData;
			if (idData.getType() == EntityType.CAR) {
				closestId = idData.getID();
				return fraction;
			}
		}
		return -1;
	}

	public boolean foundSomething() {
		return closestId != -1;
	}

}