package trafficsim.systems;

import static functions.MovementFunctions.constrainAngle;
import static functions.MovementFunctions.getAngleOfCurrentEdgeInRads;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import trafficsim.components.AccelerationComponent;
import trafficsim.components.ExpiryComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.MovementComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.movement.Behavior;
import trafficsim.movement.SeekBehavior;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovementSystem
		extends EntitySystem {

	@Getter
	private int totalCars = 0;

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
	@Mapper
	ComponentMapper<MovementComponent> movementComponentMapper;
	// @Mapper
	// ComponentMapper<AttachedLightsComponent> attachedLightsMapper;
	// @Mapper
	// ComponentMapper<TrafficLightComponent> trafficLightsMapper;
	@Mapper
	ComponentMapper<ExpiryComponent> expiryMapper;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(	SteeringComponent.class, RouteComponent.class, PhysicsBodyComponent.class,
										MovementComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity car = entities.get(i);
			RouteComponent routeComp = routeComponentMapper.get(car);
			SteeringComponent steeringComp = steeringComponentMapper.get(car);
			PhysicsBodyComponent physComp = physicsBodyMapper.get(car);
			MovementComponent movementComp = movementComponentMapper.get(car);
			if (routeComp.isSet()) {
				updateBehaviors(movementComp, routeComp);
				// Vector2 target = routeComp.getNextWaypoint();
				Vector2 steeringForce = movementComp.getBehavior().steeringForce(physComp);
				// TODO Define maxForce in relation to mass
				steeringForce.clamp(0, steeringComp.getMaxForce());
				float maxAllowedSpeed = maxSpeedMapper.get(car).getSpeed();
				float maxSpeed = Math.min(routeComp.getCurrentEdge().getData().getSpeedLimit(), maxAllowedSpeed);

				Vector2 newVel = physComp.getLinearVelocity().cpy().add(steeringForce);
				steer(physComp, routeComp, steeringComp, newVel);
				newVel.clamp(0, maxSpeed);
				physComp.setLinearVelocity(newVel);
				// System.out.println(newVel);
			}
		}
	}

	private void updateBehaviors(MovementComponent movementComp, RouteComponent routeComp) {
		for (Behavior behavior : movementComp.getBehaviors().keys()) {
			if (behavior instanceof SeekBehavior && routeComp.getWayPoints() != null) {
				((SeekBehavior) behavior).setTargetLocation(routeComp.getNextWaypoint());
			}
		}
	}

	private void steer(PhysicsBodyComponent physComp, RouteComponent routeComp, SteeringComponent steeringComp,
			Vector2 newVel) {
		float deltaA = getAngleOfCurrentEdgeInRads(routeComp) - physComp.getAngle();
		// float deltaA = getDeltaAngle(routeComp) - physComp.getAngle();
		deltaA = constrainAngle(deltaA);
		// TODO extract constants, refactor
		float angularThreshold = 2;
		if (Math.abs(deltaA) > 0.05) {
			if (Math.abs(physComp.getAngularVelocity()) < angularThreshold) {
				// deltaA < 0 &&
				float turningSpeed = 8;
				if (newVel.len() > turningSpeed) {
					newVel.scl(0.9f);
				}
				physComp.applyTorque(steeringComp.getMaxTorque() * deltaA, true);
			}
		}
		else {
			physComp.setAngularVelocity(0);
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void inserted(Entity e) {
		totalCars++;
	}

}
