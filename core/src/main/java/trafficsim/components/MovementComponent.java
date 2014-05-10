package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;
import trafficsim.movement.WeightedBehavior;

import com.artemis.Component;

/**
 * The movement behavior of a car
 * 
 * @author Giannis Papadopoulos
 */
@AllArgsConstructor
@Getter
@Setter
public class MovementComponent
		extends Component {

	@Delegate
	private WeightedBehavior behavior;
}
