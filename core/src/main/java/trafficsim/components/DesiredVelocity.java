package trafficsim.components;

import lombok.Delegate;
import lombok.Getter;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * The desired velocity vector of an entity. It is defined by a vector
 * 
 * @author Giannis Papadopoulos
 */
public class DesiredVelocity
		extends Component {

	@Delegate
	@Getter
	private Vector2 velocity = new Vector2(0, 0);

}
