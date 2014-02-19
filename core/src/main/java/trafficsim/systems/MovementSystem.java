package trafficsim.systems;

import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.CAR_WIDTH;
import trafficsim.components.AccelerationComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;

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
	ComponentMapper<AccelerationComponent> accelerationMapper;
	@Mapper
	ComponentMapper<MaxSpeedComponent> maxSpeedMapper;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(AccelerationComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		// TODO
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			PhysicsBodyComponent physComp = physicsBodyMapper.get(e);
			if (e.getId() == 6) {
				// System.out.println(physComp.getAngle());
				if (physComp.getAngle() < Math.PI / 2) {
					physComp.applyForce(new Vector2(0, 1.3f * accelerationMapper.get(e).getAcceleration() * physComp.getMass()),
											physComp.getWorldPoint(new Vector2(CAR_LENGTH / 2, CAR_WIDTH / 2)), true);
					physComp.applyForce(new Vector2(0, 1.3f * accelerationMapper.get(e).getAcceleration() * physComp.getMass()),
											physComp.getWorldPoint(new Vector2(CAR_LENGTH / 2, -CAR_WIDTH / 2)), true);
				}
				else {
					Vector2 vel = physComp.getLinearVelocity();
					vel.x = 0;
					physComp.setLinearVelocity(vel);
					physComp.setAngularVelocity(0);
					physComp.applyForceToCenter(new Vector2(accelerationMapper.get(e).getAcceleration() * physComp.getMass(), 0), true);
				}

				e.getComponent(SpriteComponent.class).setRotation((float) (physComp.getAngle() * 180 / Math.PI));
				System.out.println(physComp.getLinearVelocity() + " " + physComp.getAngle());
				continue;
			}
			if (!maxSpeedMapper.has(e) || physComp.getLinearVelocity().len() < maxSpeedMapper.get(e).getSpeed())
				physComp.applyForceToCenter(new Vector2(accelerationMapper.get(e).getAcceleration() * physComp.getMass(), 0), true);
			else
				physComp.applyForceToCenter(new Vector2(-accelerationMapper.get(e).getAcceleration() * physComp.getMass(), 0), true);

		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
