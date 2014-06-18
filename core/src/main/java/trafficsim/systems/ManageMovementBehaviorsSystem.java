package trafficsim.systems;

import static com.badlogic.gdx.math.MathUtils.*;
import static functions.MovementFunctions.getRoad;
import static trafficsim.TrafficSimConstants.*;
import graph.Vertex;

import java.util.List;

import trafficsim.TrafficSimWorld;
import trafficsim.callbacks.FrontRayCastCallBack;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.GroupedTrafficLightComponent;
import trafficsim.components.GroupedTrafficLightComponent.GroupedTrafficLightData;
import trafficsim.components.MovementComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;
import trafficsim.movement.Behavior;
import trafficsim.movement.BrakeBehavior;
import trafficsim.movement.SeekBehavior;
import trafficsim.roads.NavigationObject;

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
	private ComponentMapper<SteeringComponent> steeringComponentMapper;
	@Mapper
	private ComponentMapper<AttachedLightsComponent> attachedLightsMapper;
	@Mapper
	private ComponentMapper<TrafficLightComponent> trafficLightsMapper;
	@Mapper
	private ComponentMapper<GroupedTrafficLightComponent> groupedTrafficLightsMapper;

	private float brakingThreshold = 3f * SPEED_RATIO;
	private float carInFrontThreshold = (CAR_LENGTH + brakingThreshold + 1f) * SPEED_RATIO;
	private float emergencyThreshold = (CAR_LENGTH + 2f) * SPEED_RATIO;

	@SuppressWarnings("unchecked")
	public ManageMovementBehaviorsSystem() {
		super(Aspect.getAspectForAll(RouteComponent.class, PhysicsBodyComponent.class, MovementComponent.class));
	}

	public void setConstants() {
		brakingThreshold = 3f * SPEED_RATIO;
		carInFrontThreshold = (CAR_LENGTH + brakingThreshold + 1f) * SPEED_RATIO;
		emergencyThreshold = (CAR_LENGTH + 2f) * SPEED_RATIO;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity car = entities.get(i);
			RouteComponent routeComp = routeComponentMapper.get(car);
			PhysicsBodyComponent physComp = physicsBodyMapper.get(car);
			MovementComponent movementComp = movementComponentMapper.get(car);
			SteeringComponent steeringComp = steeringComponentMapper.get(car);
			updateBehaviors(movementComp, routeComp, physComp, steeringComp);
		}

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	public TrafficSimWorld getWorld() {
		return (TrafficSimWorld) world;
	}

	private void updateBehaviors(MovementComponent movementComp, RouteComponent routeComp,
			PhysicsBodyComponent physComp, SteeringComponent steeringComp) {
		World box2dWorld = ((TrafficSimWorld) world).getBox2dWorld();
		Vector2 position = physComp.getPosition();
		float angle = physComp.getAngle();
		Vector2 angleAdjustment = new Vector2(cos(angle), sin(angle));
		int deltaAngle = 30;
		Vector2 angle45 = new Vector2(cos(angle + deltaAngle * degRad), sin(angle + deltaAngle * degRad));
		Vector2 angleMinus45 = new Vector2(cos(angle - deltaAngle * degRad), sin(angle - deltaAngle * degRad));
		float rayLength = 3 * CAR_LENGTH;
		FrontRayCastCallBack rayCallBack = new FrontRayCastCallBack();
		box2dWorld.rayCast(rayCallBack, position, position.cpy().add(angleAdjustment.cpy().scl(rayLength)));
		// box2dWorld.rayCast(rayCallBack, position, position.cpy().add(angle45.cpy().scl(rayLength)));
		// box2dWorld.rayCast(rayCallBack, position, position.cpy().add(angleMinus45.cpy().scl(rayLength)));

		if (rayCallBack.foundSomething()) {
			Entity otherCar = world.getEntity(rayCallBack.getClosestId());
			if (otherCar == null) {
				// System.out.println("Could not retrieve entity with ID " + rayCallBack.getClosestId() + " loc "
				// + position);
			}
			else if (otherCar != null && isOnSameEdge(routeComp, otherCar)) {
				float distance = physicsBodyMapper.get(otherCar).getPosition().dst(position);
				// steeringComponentMapper.get(otherCar).getState() != State.DEFAULT &&
				// && routeComponentMapper.get(otherCar).getCurrentEdge() == routeComp.getCurrentEdge()
				if (distance < emergencyThreshold) {
					setBrakeBehavior(movementComp, 0.1f);
					physComp.setLinearVelocity(new Vector2(0, 0));
					steeringComp.setState(State.STOPPED);
					return;
				}
				else if (distance < carInFrontThreshold) {
					setBrakeBehavior(movementComp, DEFAULT_BRAKING_FACTOR);
					steeringComp.setState(State.STOPPING);
					return;
				}
			}
		}
		Entity trafficLight = getRelevantLight(routeComp);
		if (trafficLight != null) {
			Vector2 lightPos = physicsBodyMapper.get(trafficLight).getPosition();
			Vector2 carPos = physComp.getPosition();
			// Vector2 target = routeComp.getRoadEndPoint();
			Vector2 carFrontPosition = physComp.getWorldPoint(new Vector2(CAR_LENGTH / 2, 0));

			if (trafficLight != null && trafficLightsMapper.get(trafficLight).getStatus() != Status.GREEN
				&& carFrontPosition.dst(lightPos) < brakingThreshold && !pastTrafficLight(physComp, trafficLight)) {
				setBrakeBehavior(movementComp, DEFAULT_BRAKING_FACTOR);
				steeringComp.setState(State.STOPPED);
				return;
			}
		}
		steeringComp.setState(State.DEFAULT);
		setSeekBehavior(movementComp, routeComp);
	}

	private boolean isOnSameEdge(RouteComponent routeComp, Entity otherCar) {
		assert otherCar != null && routeComp != null;
		RouteComponent otherRouteComponent = routeComponentMapper.get(otherCar);
		SteeringComponent otherSteeringComp = steeringComponentMapper.get(otherCar);
		if (!otherRouteComponent.isSet() || !routeComp.isSet()) {
			return false;
		}
		boolean bothOnIntersection = !routeComp.isFollowingEdge() && !otherRouteComponent.isFollowingEdge();
		// if (otherSteeringComp.getState() == State.STOPPED) {
		// return false;
		// }
		if (!routeComp.isFollowingEdge() && routeComp.getWayPointIndex() > 5) {
			return false;
		}
		return getRoad(otherRouteComponent.getCurrentEdge()).getDirection() == getRoad(routeComp.getCurrentEdge()).getDirection();
		// return otherRouteComponent.getCurrentEdge().getID() == routeComp.getCurrentEdge().getID();
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
				// System.out.println(routeComp.getNextWaypoint());
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

	private Entity getRelevantLight(RouteComponent routeComp) {
		if (!routeComp.isFollowingEdge()) {
			// Don't stop for traffic lights when on the crossroad
			return null;
		}
		Vertex<NavigationObject> nextIntersection = routeComp.getNextVertex();
		int vertexEntityID = getWorld().getVertexToEntityMap().get(nextIntersection.getID());
		Entity vertexEntity = world.getEntity(vertexEntityID);
		if (!groupedTrafficLightsMapper.has(vertexEntity)) {
			return null;
		}
		GroupedTrafficLightComponent lightComp = groupedTrafficLightsMapper.get(vertexEntity);
		List<GroupedTrafficLightData> tfLightList = lightComp.getLightsOnEdge(routeComp.getCurrentEdge().getID());
		if (tfLightList == null) {
			//System.out.println(lightComp);
		}
		else {
			int lightID = tfLightList.get(0).getID();
			Entity trafficLight = getWorld().getEntity(lightID);
			assert trafficLight != null;
			return trafficLight;
		}
		return null;
	}

	// // TODO this is not robust
	// private Entity getRelevantLight(RouteComponent routeComp) {
	// if (!routeComp.isSet()) {
	// return null;
	// }
	// int roadId = ((TrafficSimWorld) world).getEdgeToEntityMap().get(routeComp.getCurrentEdge().getID());
	// Entity road = world.getEntity(roadId);
	// // TODO This needs to change
	// boolean fromAtoB = true;
	// boolean leftTurn = false;
	// // boolean leftTurn = isLeftTurn(routeComp);
	//
	// if (attachedLightsMapper.has(road)) {// && !routeComp.isLast()) {
	// TIntList trafficLights = attachedLightsMapper.get(road).getTrafficLightIDs();
	// for (int i = 0; i < trafficLights.size(); i++) {
	// Entity light = world.getEntity(trafficLights.get(i));
	// TrafficLightComponent lightComp = trafficLightsMapper.get(light);
	// if (fromAtoB != lightComp.isOnPointA() && lightComp.isLeft() == leftTurn) {
	// return light;
	// }
	// }
	// }
	// return null;
	// }

}
