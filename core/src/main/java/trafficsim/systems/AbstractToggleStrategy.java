package trafficsim.systems;

import trafficsim.components.GroupedTrafficLightComponent;

/** Strategy for toggling traffic lights, determines which light will be green next */
public abstract class AbstractToggleStrategy {

	public abstract void toggle(GroupedTrafficLightSystem lightSystem, GroupedTrafficLightComponent groupComp);
}
