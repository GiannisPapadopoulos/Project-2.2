package trafficsim.systems;

import trafficsim.components.SpawnComponent;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

public class SpawnSystem
		extends EntitySystem {

	@SuppressWarnings("unchecked")
	public SpawnSystem() {
		super(Aspect.getAspectForAll(SpawnComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
