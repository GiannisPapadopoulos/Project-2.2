package trafficsim.systems;

import trafficsim.components.AccelerationComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.factories.GetAngle;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import functions.GetMidPoint;

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
	@Mapper
	ComponentMapper<RouteComponent> routeComponentMapper;
	@Mapper
	ComponentMapper<SteeringComponent> steeringComponentMapper;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(AccelerationComponent.class, RouteComponent.class, PhysicsBodyComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		// TODO
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (routeComponentMapper.has(entity) && steeringComponentMapper.has(entity)
				&& routeComponentMapper.get(entity).isSet()) {
				RouteComponent routeComp = routeComponentMapper.get(entity);
				SteeringComponent steeringComp = steeringComponentMapper.get(entity);
				PhysicsBodyComponent physComp = physicsBodyMapper.get(entity);
				Vector2 target = new GetMidPoint().apply(routeComp.getNextVertex().getData());

				float dst = target.dst(physComp.getPosition());
				float threshold = 3;
				if (dst < threshold && routeComp.getEdgeIndex() < routeComp.getRoute().size() - 1) {
					routeComp.setCurrentVertex(routeComp.getNextVertex());
					routeComp.setEdgeIndex(routeComp.getEdgeIndex() + 1);
				}

				Vector2 force = SeekBehavior.getForce(physComp.getPosition(), target);
				force.clamp(0, 1f);
				Vector2 newVel = physComp.getLinearVelocity().cpy().add(force);
				newVel.clamp(0, routeComp.getCurrentEdge().getData().getSpeedLimit());
				float deltaA = physComp.getAngle() - newVel.angle();
				// System.out.println(new GetAngle().apply(routeComp.getCurrentEdge().getData()));
				deltaA = new GetAngle().apply(routeComp.getCurrentEdge().getData()) * MathUtils.degRad
							- physComp.getAngle();
				deltaA = constraintAngle(deltaA);
				if (Math.abs(deltaA) > 0.05) {
					newVel.scl(0.9f);
					physComp.applyTorque(350 * deltaA, true);
				}
				else {
					physComp.setAngularVelocity(0);
				}

				physComp.setLinearVelocity(newVel);
			}
		}
	}

	public static float constraintAngle(float angle) {
		int factor = (int) (angle / MathUtils.PI2);
		angle -= factor * MathUtils.PI2;
		return angle;
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
