package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.artemis.Component;

/**
 * The maximum desired speed
 * 
 * @author Giannis Papadopoulos
 */
@AllArgsConstructor
public class MaxSpeedComponent
		extends Component {

	@Getter
	@Setter
	private float speed;

}
