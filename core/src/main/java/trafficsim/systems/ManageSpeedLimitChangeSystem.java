package trafficsim.systems;

import static trafficsim.TrafficSimConstants.CITY_SPEED_LIMIT;
import static trafficsim.TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT;
import graph.Edge;
import lombok.Getter;
import lombok.Setter;
import trafficsim.TrafficSimConstants;
import trafficsim.TrafficSimWorld;
import trafficsim.roads.NavigationObject;

import com.artemis.systems.VoidEntitySystem;


/**
 * This systems manages all changes that need to be made after the global speed limit changes
 * 
 * @author Giannis Papadopoulos
 */
public class ManageSpeedLimitChangeSystem
		extends VoidEntitySystem {
	
	@Getter
	@Setter
	private boolean speedLimitModified = false;

	public ManageSpeedLimitChangeSystem() {
	}

	@Override
	protected boolean checkProcessing() {
		return speedLimitModified;
	}

	@Override
	protected void processSystem() {
		TrafficSimConstants.SPEED_RATIO = CITY_SPEED_LIMIT / DEFAULT_CITY_SPEED_LIMIT;
		world.getSystem(ManageMovementBehaviorsSystem.class).setConstants();
		setRoadSpeedLimits();
		speedLimitModified = false;
	}

	private void setRoadSpeedLimits() {
		for (Edge<NavigationObject> edge : ((TrafficSimWorld) world).getGraph().getEdgeIterator()) {
			edge.getData().setSpeedLimit(CITY_SPEED_LIMIT);
		}
	}

}
