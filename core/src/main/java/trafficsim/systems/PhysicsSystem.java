package trafficsim.systems;

import static trafficsim.TrafficSimConstants.*;
import trafficsim.TrafficSimWorld;
import trafficsim.components.PhysicsBodyComponent;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The box2d physics system. At the moment it just calls world.step() every frame, eventually we might want
 * to overwrite how collisions are handled
 * 
 * @author Giannis Papadopoulos
 */
public class PhysicsSystem
		extends EntitySystem {

	@SuppressWarnings("unchecked")
	public PhysicsSystem() {
		super(Aspect.getAspectForAll(PhysicsBodyComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void end() {
		World world2D = ((TrafficSimWorld) world).getBox2dWorld();
		world2D.step(DELTA_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}

}
