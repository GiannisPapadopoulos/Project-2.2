package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.artemis.Component;

/**
 * Required for an entity to be rendererd by the renderSystem
 * 
 * @author Giannis Papadopoulos
 * 
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpriteComponent
		extends Component {

	/** The name of the texture file */
	private final String name;
	/** Scale on each axis */
	private float scaleX;
	private float scaleY;
	/** Rotation with respect to bottom left corner */
	private float rotation;

}
