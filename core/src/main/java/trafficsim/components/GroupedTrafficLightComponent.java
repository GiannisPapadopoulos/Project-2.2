package trafficsim.components;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.artemis.Component;

/**
 * Component for grouping traffic lights at an intersection, makes it easier to guarantee
 * that 2 lights won't be incorrectly green at the same time when messing with the timers
 * 
 * @author Giannis Papadopoulos
 */
@Getter
@Setter
public class GroupedTrafficLightComponent
		extends Component {

	/**
	 * List of traffic light IDs and timers at an intersection, the inner list contains the straight and left light if
	 * both exist. So for our typical intersection it is a 4x2 matrix.
	 * Each light at an intersection can have its own green timer, they don't have to be equal (might be useful later
	 * with highways)
	 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private List<List<GroupedTrafficLightData>> groupedLightsData;
	/** Time since last change */
	private float timeElapsed;
	/** index of currently green light */
	private int index;
	/** If the active light is currently green or orange */
	private boolean green;
	
	int vertexID;
	/**
	 * If false, GroupedTrafficLightSystem.initialize() will be called on this intersection
	 * If we modify the timers before the simulation starts (i.e. green wave) it should be set to true to avoid
	 * resetting
	 */
	private boolean set = false;

	/** If each traffic light belonging to the intersection has been mapped to the correct road */
	private boolean mapped = false;

	public GroupedTrafficLightComponent(List<List<GroupedTrafficLightData>> groupedLightsData, int vertexID) {
		this.groupedLightsData = groupedLightsData;
		this.vertexID = vertexID;
	}

	/** Assume that straight and left light are toggled at the same time */
	public float getTimer() {
		GroupedTrafficLightData activeTrafficLight = groupedLightsData.get(index).get(0);
		return green ? activeTrafficLight.greenTimer : activeTrafficLight.orangeTimer;
	}

	public float getRedTimer(int lightIndex) {
		float redTimer = 0;
		for (int i = 0; i < groupedLightsData.size(); i++) {
			if (i != lightIndex) {
				redTimer += groupedLightsData.get(lightIndex).get(0).getGreenTimer();
				redTimer += groupedLightsData.get(lightIndex).get(0).getOrangeTimer();
			}
		}
		return redTimer;
	}

	/**
	 * Information for a specific traffic light
	 */
	@AllArgsConstructor
	@Getter
	public static class GroupedTrafficLightData {
		/** The entity ID, used for world.getEntity(ID) */
		private int ID;
		@Setter
		/** The green timer */
		private float greenTimer;
		@Setter
		private float orangeTimer;
		
		/** The graph id of the edge the light is on */
		private int edgeID;
		
		
		// //TODO
		// public float getRedTimer(){
		// return 0;
		// }
	}

}
