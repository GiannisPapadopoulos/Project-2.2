package trafficsim.callbacks;

import graph.EntityIdentificationData;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

/**
 * CallBack class for AABB querry used to find the entity at mouse click position
 * 
 * @author Giannis Papadopoulos
 */
@RequiredArgsConstructor
@Getter
public class FindBodyQueryCallback
		implements QueryCallback {

	@NonNull
	/** The center of the querrying rectangle */
	private Vector2 origin;

	/** The ID of the closest entity that was found */
	private EntityIdentificationData idData;

	/** The position of the entity found for comparison */
	private Vector2 closestPosition;

	@Override
	public boolean reportFixture(Fixture fixture) {
		Object userData = fixture.getBody().getUserData();
		if (userData != null && userData.getClass() == EntityIdentificationData.class) {
//			if (userData.getClass() == Integer.class) {
//				System.out.println("car");
//				float distance = fixture.getBody().getPosition().dst(origin);
//				if (closestId == -1 || distance < origin.cpy().dst(closestPosition)) {
//					closestId = (Integer) fixture.getBody().getUserData();
//					closestPosition = fixture.getBody().getPosition();
//				}
//			}
			EntityIdentificationData data = (EntityIdentificationData) fixture.getBody().getUserData();
			float distance = fixture.getBody().getPosition().dst(origin);
			if (idData == null || distance < origin.cpy().dst(closestPosition)) {
				this.idData = data;
				closestPosition = fixture.getBody().getPosition();
				assert closestPosition != null;
			}
		}
		return true;
	}

	public boolean foundSomething() {
		return idData != null;
	}

}
