package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.artemis.Component;

/**
 * Component that temporarily saves which road a traffic light belongs to
 * 
 * @author Giannis Papadopoulos
 */
@AllArgsConstructor
@Getter
public class LightToRoadMappingComponent
		extends Component {

	private int lightId;
	private int roadId;
}
