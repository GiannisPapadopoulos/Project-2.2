package trafficsim.components;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.artemis.Component;

/**
 * Component that stores the vehicles on each lane(2 directions) for each road
 * 
 * @author Giannis Papadopoulos
 */
@Getter
@NoArgsConstructor
public class VehiclesOnRoadComponent
		extends Component {

	/** Vehicles going from pointA to pointB */
	private TIntList vehiclesOnLaneIDs = new TIntArrayList();
	// /** Vehicles going from pointB to pointA */
	// private TIntList vehiclesOnLeftLaneIDs = new TIntArrayList();
}
