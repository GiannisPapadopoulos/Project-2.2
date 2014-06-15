package trafficsim.systems;

import trafficsim.components.GroupedTrafficLightComponent;

/** Strategy for toggling traffic lights, determines which light will be green next */
public abstract class AbstractToggleStrategy {

	public abstract void toggle(GroupedTrafficLightSystem lightSystem, GroupedTrafficLightComponent groupComp);

	/** Basic timer strategy */
	public static final AbstractToggleStrategy basicToggleStrategy = new BasicToggleStrategy();

	/** Strategy that gives priority to lane with most cars */
	public static final AbstractToggleStrategy priorityToggleStrategy = new PriorityToggleStrategy();
}
