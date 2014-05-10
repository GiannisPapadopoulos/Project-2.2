package trafficsim.systems;

import static com.badlogic.gdx.math.MathUtils.*;
import static functions.MovementFunctions.*;
import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import gnu.trove.list.TIntList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import trafficsim.TrafficSimWorld;
import trafficsim.callbacks.TrafficRayCastCallBack;
import trafficsim.components.AccelerationComponent;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.ExpiryComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;
import trafficsim.movement.SteeringFunctions;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The movement system is responsible for updating the velocity of all entities
 * 
 * @author Giannis Papadopoulos
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OldMovementSystem
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
	@Mapper
	ComponentMapper<AttachedLightsComponent> attachedLightsMapper;
	@Mapper
	ComponentMapper<TrafficLightComponent> trafficLightsMapper;
	@Mapper
	ComponentMapper<ExpiryComponent> expiryMapper;

	@Getter
	private int totalCars = 0;

	@SuppressWarnings("unchecked")
	public OldMovementSystem() {
		super(Aspect.getAspectForAll(SteeringComponent.class, RouteComponent.class, PhysicsBodyComponent.class));
	}

	// Steering constants, abstract this at some point

	// Distance to light
	float brakingThreshold = 5f;

	// // Start turning left
	// float leftTurnThreshold = 0.2f;
	// float rightTurnThreshold = 1.2f;
	// // Arrived at destination
	// float arrivalThreshold = 2.5f;

	float carInFrontThreshold = CAR_LENGTH * 1.6f;

	// TODO clean this up
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity car = entities.get(i);
			if (routeComponentMapper.has(car) && steeringComponentMapper.has(car)
				&& routeComponentMapper.get(car).isSet()) {
				RouteComponent routeComp = routeComponentMapper.get(car);
				SteeringComponent steeringComp = steeringComponentMapper.get(car);
				PhysicsBodyComponent physComp = physicsBodyMapper.get(car);

				if (steeringComp.getState() == State.ARRIVED) {
					Vector2 force = physComp.getLinearVelocity().cpy().scl(-1).clamp(0, steeringComp.getMaxForce());
					Vector2 newVel = physComp.getLinearVelocity().cpy().add(force);
					physComp.setLinearVelocity(newVel);
					if (newVel.len() < 0.1) {
						expiryMapper.get(car).setExpired(true);
					}
					continue;
				}

				World box2dWorld = ((TrafficSimWorld) world).getBox2dWorld();
				Vector2 position = physComp.getPosition();
				Vector2 angleAdjustment = new Vector2(cos(physComp.getAngle()), sin(physComp.getAngle()));
				float rayLength = 3 * CAR_LENGTH;
				TrafficRayCastCallBack rayCallBack = new TrafficRayCastCallBack();
				box2dWorld.rayCast(rayCallBack, position,
									position.cpy().add(angleAdjustment.cpy().scl(rayLength)));

				if (rayCallBack.foundSomething()) {
					Entity otherCar = world.getEntity(rayCallBack.getClosestId());
					float distance = physicsBodyMapper.get(otherCar).getPosition().dst(position);
					// steeringComponentMapper.get(otherCar).getState() != State.DEFAULT &&
					// && routeComponentMapper.get(otherCar).getCurrentEdge() == routeComp.getCurrentEdge()
					if (distance < carInFrontThreshold) {
						slowDown(steeringComp, physComp);
						continue;
					}
				}

				Vector2 target = getTarget(routeComp);
				// float distanceToTarget = target.dst(physComp.getPosition());

				Entity trafficLight = getRelevantLight(routeComp);

				if (trafficLight != null && trafficLightsMapper.get(trafficLight).getStatus() != Status.GREEN
					&& distance(car, trafficLight) < brakingThreshold && !pastTrafficLight(car, trafficLight, target)) {
					slowDown(steeringComp, physComp);
					// continue;
				}
				else {
					steeringComp.setState(State.DEFAULT);
					execute(routeComp, physComp, steeringComp, maxSpeedMapper.get(car));
				}
			}
		}
	}

	private boolean pastTrafficLight(Entity car, Entity trafficLight, Vector2 target) {
		Vector2 carFrontPosition = physicsBodyMapper.get(car).getWorldPoint(new Vector2(CAR_LENGTH / 2, 0));
		// System.out.println(carFrontPosition + " " + getPosition(car));
		Vector2 lightPosition = physicsBodyMapper.get(trafficLight).getPosition();
		float distanceToTarget = target.dst(carFrontPosition);
		return distance(car, trafficLight) < distanceToTarget || distanceToTarget < target.dst(lightPosition);
	}

	public void execute(RouteComponent routeComp, PhysicsBodyComponent physComp, SteeringComponent steeringComp,
			MaxSpeedComponent maxSpeedComp) {
		Vector2 target = getTarget(routeComp);

		// Vector2 force = SeekBehavior.getForce(physComp.getPosition(), target);
		Vector2 force = SteeringFunctions.seek(physComp.getPosition(), target);
		// TODO Define maxForce in relation to mass
		force.clamp(0, steeringComp.getMaxForce());
		Vector2 newVel = physComp.getLinearVelocity().cpy().add(force);
		float maxSpeed = Math.min(routeComp.getCurrentEdge().getData().getSpeedLimit(), maxSpeedComp.getSpeed());
		newVel.clamp(0, maxSpeed);
		// # steering
		float deltaA = getAngleOfCurrentEdgeInRads(routeComp) - physComp.getAngle();
		// float deltaA = getDeltaAngle(routeComp) - physComp.getAngle();
		deltaA = constrainAngle(deltaA);
		// TODO extract constants, refactor
		float angularThreshold = 7;
		float turningSpeed = 8;
		if (Math.abs(deltaA) > 0.05) {
			if (Math.abs(physComp.getAngularVelocity()) < angularThreshold) {
				// deltaA < 0 &&
				if (deltaA < 0 && newVel.len() > turningSpeed) {
					newVel.scl(0.9f);
				}
				physComp.applyTorque(steeringComp.getMaxTorque() * deltaA, true);
			}
		}
		else {
			physComp.setAngularVelocity(0);
		}
		// # end steering

		physComp.setLinearVelocity(newVel);
	}

	private void slowDown(SteeringComponent steeringComp, PhysicsBodyComponent physComp) {
		Vector2 force = physComp.getLinearVelocity().cpy().scl(-1).clamp(0, steeringComp.getMaxForce());
		Vector2 newVel = physComp.getLinearVelocity().cpy().add(force);
		physComp.setLinearVelocity(newVel);
		steeringComp.setState(State.STOPPING);
		if (newVel.len() < 0.1) {
			steeringComp.setState(State.STOPPED);
		}
	}

	private Entity getRelevantLight(RouteComponent routeComp) {
		int roadId = ((TrafficSimWorld) world).getEdgeToEntityMap().get(routeComp.getCurrentEdge().getID());
		Entity road = world.getEntity(roadId);
		boolean fromAtoB = fromAtoB(routeComp);
		float angle = getDeltaAngle(routeComp) * degRad;
		// 0.001 is for floating point accuracy
		boolean leftTurn = angle < -0.001 || angle > PI + 0.001;

		if (attachedLightsMapper.has(road)) {// && !routeComp.isLast()) {
			TIntList trafficLights = attachedLightsMapper.get(road).getTrafficLightIDs();
			for (int i = 0; i < trafficLights.size(); i++) {
				Entity light = world.getEntity(trafficLights.get(i));
				TrafficLightComponent lightComp = trafficLightsMapper.get(light);
				if (fromAtoB != lightComp.isOnPointA() && lightComp.isLeft() == leftTurn) {
					// System.out.println("angle  " + angle + " " + routeComp.getEdgeIndex() + " light "
					// + physicsBodyMapper.get(light).getPosition() + " n "
					// + getMidPoint(routeComp.getNextVertex().getData()) + " s " + lightComp.isLeft()
					// + " turn " + leftTurn);
					return light;
				}
			}
		}
		return null;
	}

	private float distance(Entity entityA, Entity entityB) {
		assert physicsBodyMapper.has(entityA) && physicsBodyMapper.has(entityB);
		return physicsBodyMapper.get(entityA).getPosition().dst(physicsBodyMapper.get(entityB).getPosition());
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
