package trafficsim.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataComponent
		extends Component {

	Vector2 lastPosition;
	float deltaTime;
	
	float totalDistance;
	float totalTime;
	float distanceLeft;
	
	float timeSpentOnTrafficLights;

	public float getAverageSpeed() {
		return totalDistance / totalTime;
	}

	/** The fraction of time spent waiting on traffic lights */
	public float getPercentageStopped() {
		return timeSpentOnTrafficLights / totalTime;
	}

}
