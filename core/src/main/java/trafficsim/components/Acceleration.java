package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.artemis.Component;

/**
 * The maximum acceleration of an entity
 * 
 * @author Giannis Papadopoulos
 * 
 */
@AllArgsConstructor
public class Acceleration
		extends Component {

	@Getter
	@Setter
	private float acceleration;

}
