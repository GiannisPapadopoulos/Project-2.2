package pathfinding;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

public class AssignDestinationSystem
		extends EntitySystem {

	public AssignDestinationSystem(Aspect aspect) {
		super(aspect);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
