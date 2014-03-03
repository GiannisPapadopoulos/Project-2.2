package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The desired orientation of an entity, specified by an angle in radians
 * 
 * @author Giannis Papadopoulos
 */
@AllArgsConstructor
public class DesiredOrientation {

	@Getter
	float angle;
}
