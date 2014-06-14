package trafficsim.systems;


import static com.badlogic.gdx.math.MathUtils.degRad;
import static functions.MovementFunctions.constrainAngle;
import static trafficsim.TrafficSimConstants.SPEED_SCALING_FACTOR;
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
import trafficsim.components.SteeringComponent.State;

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
				processArrived(car, steeringComp, physComp, routeComp);

				// Vector2 target = routeComp.getNextWaypoint();
				Vector2 steeringForce = movementComp.getBehavior().steeringForce(physComp);
				// TODO Define maxForce in relation to mass
				// steeringForce.clamp(0, steeringComp.getMaxForce());
				// steeringForce.nor().scl(steeringComp.getMaxForce());
				steeringForce.clamp(0, steeringComp.getMaxForce());
				float maxAllowedSpeed = maxSpeedMapper.get(car).getSpeed();
				float maxSpeed = Math.min(routeComp.getCurrentEdge().getData().getSpeedLimit(), maxAllowedSpeed);

//				Vector2 newVel = physComp.getLinearVelocity().cpy().add(steeringForce);
				Vector2 newVel = physComp.getLinearVelocity()
											.cpy()
											.scl(1f)
											.add(steeringForce.scl(SPEED_SCALING_FACTOR));
				steer(physComp, routeComp, steeringComp, newVel);
				newVel.clamp(0, maxSpeed);
				physComp.setLinearVelocity(newVel);

				correctionVectors(routeComp, physComp);
			}
		}
	}

	// TODO change to arrival behavior,state-based
	private void processArrived(Entity car, SteeringComponent steeringComp, PhysicsBodyComponent physComp,
			RouteComponent routeComp) {
		if (steeringComp.getState() == State.ARRIVED) {
			if (physComp.getPosition().dst(routeComp.getNextWaypoint()) < 1f) {
				expiryMapper.get(car).setExpired(true);
			}
		}
	}

	private void steer(PhysicsBodyComponent physComp, RouteComponent routeComp, SteeringComponent steeringComp,
			Vector2 newVel) {
//		float deltaA = getAngleOfCurrentEdgeInRads(routeComp) - physComp.getAngle();
		float deltaA = constrainAngle(physComp.getLinearVelocity().angle() * degRad)
						- constrainAngle(physComp.getAngle());
		// float deltaA =
		// physComp.getLinearVelocity().cpy().sub(getUnitVectorRads(physComp.getBody().getAngle())).angle();
		// float deltaA =
		// getUnitVectorRads(physComp.getBody().getAngle()).cpy().sub(physComp.getLinearVelocity()).angle();
		// float deltaA = getDeltaAngle(routeComp) - physComp.getAngle();
		// float deltaA = physComp.getAngle();
		// if (routeComp.getWayPointIndex() < routeComp.getWayPoints().size() - 1) {
		// Vector2 v1 = routeComp.getWayPoints().get(routeComp.getWayPointIndex() + 1);
		// Vector2 v2 = routeComp.getWayPoints().get(routeComp.getWayPointIndex());
		// Vector2 vector = routeComp.getWayPoints()
		// .get(routeComp.getWayPointIndex() + 1)
		// .cpy()
		// .sub(routeComp.getNextWaypoint().cpy());
		// deltaA = vector.angle() * degRad - physComp.getAngle();
		// // System.out.println("da" + deltaA + "constr " + constrainAngle(deltaA) + "vecA " + vector.angle() * degRad
		// // + " v1 " + v1 + " v2 " + v2 + " vector " + vector);
		// }
		deltaA = constrainAngle(deltaA);

		// physComp.getBody().setTransform(physComp.getPosition(), physComp.getLinearVelocity().angle() * degRad);
		// System.out.println(constrainAngle(physComp.getLinearVelocity().angle() * degRad));

		// TODO extract constants, refactor
		float scalingFactor = 0.5f;
		float angularThreshold = 2;
		if (Math.abs(deltaA) > 0.05) {
			if (Math.abs(physComp.getAngularVelocity()) < angularThreshold) {
				// deltaA < 0 &&
				float turningSpeed = 8;
				if (newVel.len() > turningSpeed) {
					newVel.scl(0.9f);
				}
				physComp.applyTorque(steeringComp.getMaxTorque() * deltaA * scalingFactor, true);

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

	public void carRemoved() {
		totalCars--;
		assert totalCars >= 0;
	}

	// UNUSED
	private void correctionVectors(RouteComponent routeComp, PhysicsBodyComponent physComp) {
		// Vector2 roadVector = getVector(routeComp.getCurrentEdge());
		// // The vector in world coordinates
		// Vector2 worldRoadVector = routeComp.getCurrentEdge().getData().getPointA().cpy().add(roadVector);
		//
		// Vector2 roadLeft = worldRoadVector.cpy().add(roadVector.cpy().rotate(90).nor().scl(LANE_WIDTH));
		// Vector2 roadRight = worldRoadVector.cpy().add(roadVector.cpy().rotate(-90).nor().scl(LANE_WIDTH));
		// Vector2 adjust = roadVector.cpy().rotate(-90).nor().scl(LANE_WIDTH);
		// float length = physComp.getPosition().cpy().dot(roadLeft) / roadLeft.len2();
		// Vector2 projection = roadLeft.cpy().scl(length);

		// System.out.println("v " + roadVector + " w  " + worldRoadVector + " r " + roadRight + "l " + roadLeft
		// + " p " + projection);
		// System.out.println(projection);
		// System.out.println(newVel);
	}

}
