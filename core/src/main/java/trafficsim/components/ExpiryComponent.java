package trafficsim.components;

import lombok.Getter;
import lombok.Setter;

import com.artemis.Component;

/**
 * Entities possessing this component can expire and be removed from the world
 * 
 * @author Giannis Papadopoulos
 */
public class ExpiryComponent
		extends Component {

	@Getter
	@Setter
	/** When this is set to true the entity will be removed from the world */
	private boolean expired;

}
