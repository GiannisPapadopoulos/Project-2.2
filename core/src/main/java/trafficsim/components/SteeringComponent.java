package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.artemis.Component;

@AllArgsConstructor
@Getter
@Setter
public class SteeringComponent
		extends Component {

	/** The desired angle, same as the angle of the target road */
	private float desiredAngle;
	/** If the entity is currently taking a turn */
	private boolean turning;

}
