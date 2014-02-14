package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The physics body of an entity. Entities possessing it will be processed by box2D
 * 
 * @author Giannis Papadopoulos
 * 
 */
@AllArgsConstructor
public class PhysicsBodyComponent
		extends Component {

	/** Actual Box 2D Body For Physical Representation */
	@Getter
	@Delegate
	protected Body body;

}
