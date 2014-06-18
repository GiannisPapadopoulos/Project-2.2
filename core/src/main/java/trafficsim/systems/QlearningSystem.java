package trafficsim.systems;

import trafficsim.components.GroupedTrafficLightComponent;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

public class QlearningSystem
		extends EntitySystem {

	@SuppressWarnings("unchecked")
	public QlearningSystem() {
		super(Aspect.getAspectForAll(GroupedTrafficLightComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
