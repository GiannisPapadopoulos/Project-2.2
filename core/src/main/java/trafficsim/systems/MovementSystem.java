package trafficsim.systems;

import trafficsim.components.Acceleration;
import trafficsim.components.PhysicsBodyComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

/**
 * The movement system is responsible for updating the velocity of all entities
 * 
 * @author Giannis Papadopoulos
 */
public class MovementSystem
		extends EntitySystem {

	@Mapper
	ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	ComponentMapper<Acceleration> accelerationMapper;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(Acceleration.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		// TODO
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			physicsBodyMapper.get(e).applyForceToCenter(new Vector2(-accelerationMapper.get(e).getAcceleration()
																	* physicsBodyMapper.get(e).getMass(), 0), true);
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
