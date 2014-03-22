package trafficsim.components;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.artemis.Component;

/**
 * Saves the id of traffic light entities belonging to that road
 * 
 * @author Giannis Papadopoulos
 */
@NoArgsConstructor
public class AttachedLightsComponent
		extends Component {

	@Getter
	private TIntList trafficLightIDs = new TIntArrayList();
}
