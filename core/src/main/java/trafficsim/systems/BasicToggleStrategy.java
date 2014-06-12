package trafficsim.systems;

import trafficsim.components.GroupedTrafficLightComponent;
import trafficsim.components.GroupedTrafficLightComponent.GroupedTrafficLightData;
import trafficsim.components.TrafficLightComponent.Status;

/** This strategy simply sets the light at the next index to green */
public class BasicToggleStrategy
		extends AbstractToggleStrategy {

	@Override
	public void toggle(GroupedTrafficLightSystem lightSystem, GroupedTrafficLightComponent groupComp) {
		// If the current index is green, change it to orange
		if (groupComp.isGreen()) {
			for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(groupComp.getIndex())) {
				lightSystem.setLight(lightData, Status.ORANGE);
			}
			groupComp.setGreen(false);
		}
		// Set the light at next index to green
		else {
			groupComp.setIndex((groupComp.getIndex() + 1) % groupComp.getGroupedLightsData().size());
			lightSystem.setGreen(groupComp);
			groupComp.setGreen(true);
		}
		groupComp.setTimeElapsed(0);
	}

}
