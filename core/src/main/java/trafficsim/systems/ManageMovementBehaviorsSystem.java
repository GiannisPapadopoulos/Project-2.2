package trafficsim.systems;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;
import static functions.MovementFunctions.fromAtoB;
import static functions.MovementFunctions.isLeftTurn;
import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.DEFAULT_BRAKING_FACTOR;
import gnu.trove.list.TIntList;
import trafficsim.TrafficSimWorld;
import trafficsim.callbacks.TrafficRayCastCallBack;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.MovementComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;
import trafficsim.movement.Behavior;
import trafficsim.movement.BrakeBehavior;
import trafficsim.movement.SeekBehavior;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class ManageMovementBehaviorsSystem
		extends EntitySystem {

	@Mapper
	private ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	private ComponentMapper<RouteComponent> routeComponentMapper;
	@Mapper
	private ComponentMapper<MovementComponent> movementComponentMapper;
	@Mapper
	private ComponentMapper<AttachedLightsComponent> attachedLightsMapper;
	@Mapper
	private ComponentMapper<TrafficLightComponent> trafficLightsMapper;

	private float brakingThreshold = 3f;
	private float carInFrontThreshold = CAR_LENGTH + brakingThreshold + 1f;
	private float emergencyThreshold = CAR_LENGTH + 2f;

	@SuppressWarnings("unchecked")
	public ManageMovementBehaviorsSystem() {
		super(Aspect.getAspectForAll(RouteComponent.class, PhysicsBodyComponent.class, MovementComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity car = entities.get(i);
			RouteComponent routeComp = routeComponentMapper.get(car);
			PhysicsBodyComponent physComp = physicsBodyMapper.get(car);
			MovementComponent movementComp = movementComponentMapper.get(car);
			updateBehaviors(movementComp, routeComp, physComp);
		}

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	private void updateBehaviors(MovementComponent movementComp, RouteComponent routeComp, PhysicsBodyComponent physComp) {
		Entity trafficLight = getRelevantLight(routeComp);
		if (trafficLight != null) {
			Vector2 lightPos = physicsBodyMapper.get(trafficLight).getPosition();
			Vector2 carPos = physComp.getPosition();
			// Vector2 target = routeComp.getRoadEndPoint();
			Vector2 carFrontPosition = physComp.getWorldPoint(new Vector2(CAR_LENGTH / 2, 0));

			if (trafficLight != null && trafficLightsMapper.get(trafficLight).getStatus() != Status.GREEN
				&& carFrontPosition.dst(lightPos) < brakingThreshold && !pastTrafficLight(physComp, trafficLight)) {
				setBrakeBehavior(movementComp, DEFAULT_BRAKING_FACTOR);
				return;
			}
		}
		World box2dWorld = ((TrafficSimWorld) world).getBox2dWorld();
		Vector2 position = physComp.getPosition();
		Vector2 angleAdjustment = new Vector2(cos(physComp.getAngle()), sin(physComp.getAngle()));
		float rayLength = 3 * CAR_LENGTH;
		TrafficRayCastCallBack rayCallBack = new TrafficRayCastCallBack();
		box2dWorld.rayCast(rayCallBack, position, position.cpy().add(angleAdjustment.cpy().scl(rayLength)));

		if (rayCallBack.foundSomething()) {
			Entity otherCar = world.getEntity(rayCallBack.getClosestId());
			float distance = physicsBodyMapper.get(otherCar).getPosition().dst(position);
			// steeringComponentMapper.get(otherCar).getState() != State.DEFAULT &&
			// && routeComponentMapper.get(otherCar).getCurrentEdge() == routeComp.getCurrentEdge()
			if (distance < emergencyThreshold) {
				setBrakeBehavior(movementComp, 0.1f);
				return;
			}
			else if (distance < carInFrontThreshold) {
				setBrakeBehavior(movementComp, DEFAULT_BRAKING_FACTOR);
				return;
			}
		}
		setSeekBehavior(movementComp, routeComp);

	}

	private void setBrakeBehavior(MovementComponent movementComp, float factor) {
		for (Behavior behavior : movementComp.getBehaviors().keys()) {
			if (behavior instanceof BrakeBehavior) {
				movementComp.getBehaviors().put(behavior, 1f);
				((BrakeBehavior) behavior).setScalingFactor(factor);
			}
			else {
				movementComp.getBehaviors().put(behavior, 0f);
			}
		}
	}

	private void setSeekBehavior(MovementComponent movementComp, RouteComponent routeComp) {
		for (Behavior behavior : movementComp.getBehaviors().keys()) {
			if (behavior instanceof SeekBehavior && routeComp.getWayPoints() != null) {
				movementComp.getBehaviors().put(behavior, 1f);
				((SeekBehavior) behavior).setTargetLocation(routeComp.getNextWaypoint());
			}
			else {
				movementComp.getBehaviors().put(behavior, 0f);
			}
		}
	}

	// TODO
	private boolean pastTrafficLight(PhysicsBodyComponent physComp, Entity trafficLight) {
		Vector2 carBackPosition = physComp.getWorldPoint(new Vector2(-CAR_LENGTH / 2, 0));
		Vector2 lightPosition = physicsBodyMapper.get(trafficLight).getPosition();
		return carBackPosition.dst(lightPosition) < CAR_LENGTH;
	}

	// TODO this is not robust
	private Entity getRelevantLight(RouteComponent routeComp) {
		if (!routeComp.isSet()) {
			return null;
		}
		int roadId = ((TrafficSimWorld) world).getEdgeToEntityMap().get(routeComp.getCurrentEdge().getID());
		Entity road = world.getEntity(roadId);
		boolean fromAtoB = fromAtoB(routeComp);
		boolean leftTurn = isLeftTurn(routeComp);

		if (attachedLightsMapper.has(road)) {// && !routeComp.isLast()) {
			TIntList trafficLights = attachedLightsMapper.get(road).getTrafficLightIDs();
			for (int i = 0; i < trafficLights.size(); i++) {
				Entity light = world.getEntity(trafficLights.get(i));
				TrafficLightComponent lightComp = trafficLightsMapper.get(light);
				if (fromAtoB != lightComp.isOnPointA() && lightComp.isLeft() == leftTurn) {
					return light;
				}
			}
		}
		return null;
	}

}
