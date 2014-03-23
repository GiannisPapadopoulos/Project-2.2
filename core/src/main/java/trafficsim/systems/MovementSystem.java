package trafficsim.systems;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.degRad;
import static functions.VectorUtils.getAngle;
import static functions.VectorUtils.getVector;
import functions.VectorUtils;
import gnu.trove.list.TIntList;
import graph.Edge;
import graph.Vertex;
import trafficsim.TrafficSimWorld;
import trafficsim.components.AccelerationComponent;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;
import trafficsim.roads.Road;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

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
	@Mapper
	ComponentMapper<AttachedLightsComponent> attachedLightsMapper;
	@Mapper
	ComponentMapper<TrafficLightComponent> trafficLightsMapper;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(SteeringComponent.class, RouteComponent.class, PhysicsBodyComponent.class));
	}

	float maxAng = 0;

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
						car.deleteFromWorld();
						physComp.setActive(false);
					}
					continue;
				}

				Entity trafficLight = getRelevantLight(routeComp);
				float brakingThreshold = 5f;

				float leftTurnThreshold = 0.5f;
				float rightTurnThreshold = 1.0f;
				float arrivalThreshold = 0.5f;
				if (trafficLight != null && trafficLightsMapper.get(trafficLight).getStatus() != Status.GREEN
					&& distance(car, trafficLight) < brakingThreshold) {
					Vector2 force = physComp.getLinearVelocity().cpy().scl(-1).clamp(0, steeringComp.getMaxForce());
					Vector2 newVel = physComp.getLinearVelocity().cpy().add(force);
					physComp.setLinearVelocity(newVel);
					steeringComp.setState(State.STOPPING);
					if (newVel.len() < 0.1) {
						steeringComp.setState(State.STOPPED);
					}
				}
				else {
					steeringComp.setState(State.DEFAULT);
				}

				if (steeringComp.getState() != State.DEFAULT) {
					continue;
				}

				Vector2 target = getTarget(routeComp);
				float dst = target.dst(physComp.getPosition());

				if (routeComp.isLastEdge() && steeringComp.getState() == State.DEFAULT && dst < arrivalThreshold) {
					steeringComp.setState(State.ARRIVED);
				}
				else {
					float thresHold = isRightTurn(routeComp) ? rightTurnThreshold : leftTurnThreshold;
					if (dst < thresHold) {
						routeComp.setCurrentVertex(routeComp.getNextVertex());
						routeComp.setEdgeIndex(routeComp.getEdgeIndex() + 1);
					}
				}

				Vector2 force = SeekBehavior.getForce(physComp.getPosition(), target);
				// TODO Define maxForce in relation to mass
				force.clamp(0, steeringComp.getMaxForce());
				Vector2 newVel = physComp.getLinearVelocity().cpy().add(force);
				float maxSpeed = Math.min(	routeComp.getCurrentEdge().getData().getSpeedLimit(),
											maxSpeedMapper.get(car).getSpeed());
				newVel.clamp(0, maxSpeed);
				float deltaA = getAngleOfCurrentEdgeInRads(routeComp) - physComp.getAngle();
				deltaA = constrainAngle(deltaA);
				// TODO extract constants, refactor
				float angularThreshold = 6;
				if (Math.abs(deltaA) > 0.01) {
					if (Math.abs(physComp.getAngularVelocity()) < angularThreshold) {
						if (deltaA < 0) {
							newVel.scl(0.9f);
						}
						physComp.applyTorque(steeringComp.getMaxTorque() * deltaA, true);
					}
				}
				else {
					physComp.setAngularVelocity(0);
				}

				physComp.setLinearVelocity(newVel);

			}
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

	/** Returns true if the next turn is a right one */
	private boolean isRightTurn(RouteComponent routeComp) {
		if (routeComp.isLastEdge()) {
			return false;
		}
		return getDeltaAngle(routeComp) < 0;
	}

	private float getDeltaAngle(RouteComponent routeComp) {
		if (routeComp.isLastEdge()) {
			return 0;
		}
		Edge<Road> nextEdge = routeComp.getPath().getRoute().get(routeComp.getEdgeIndex() + 1).getEdge();
		// float angle = VectorUtils.getAngle(nextEdge.getData())
		// - VectorUtils.getAngle(routeComp.getCurrentEdge().getData());
		Vertex<Road> vertex1 = routeComp.getNextVertex();
		Vertex<Road> vertex2 = routeComp.getNextVertex().getNeighbor(nextEdge);
		return getAngle(vertex1.getData(), vertex2.getData());
	}

	private float distance(Entity entityA, Entity entityB) {
		assert physicsBodyMapper.has(entityA) && physicsBodyMapper.has(entityB);
		return physicsBodyMapper.get(entityA).getPosition().dst(physicsBodyMapper.get(entityB).getPosition());
	}

	private static float getAngleOfCurrentEdgeInRads(RouteComponent routeComp) {
		Road road = routeComp.getCurrentEdge().getData();
		Vector2 roadVector = VectorUtils.getVector(road);
		if (!fromAtoB(routeComp)) {
			roadVector.scl(-1);
		}
		return roadVector.angle() * MathUtils.degRad;
	}

	private static boolean fromAtoB(RouteComponent routeComp) {
		return routeComp.getCurrentVertex() == routeComp.getCurrentEdge().getAdjacentVertexIterator().next();
	}

	public static Vector2 getTarget(RouteComponent routeComp) {
		// Vector2 target = VectorUtils.getMidPoint(routeComp.getNextVertex().getData());
		Vector2 target = fromAtoB(routeComp) ? routeComp.getCurrentEdge().getData().getPointB().cpy()
											: routeComp.getCurrentEdge().getData().getPointA().cpy();
		Vector2 laneCorrection = getVector(routeComp.getCurrentEdge().getData()).cpy().nor().rotate(90);
		// To reduce the chance of stepping on the lane
		laneCorrection.scl(1.1f);

		if (fromAtoB(routeComp)) {
			laneCorrection.scl(-1);
		}
		target.add(laneCorrection);
		return target;
	}

	public static float constrainAngle(float angle) {
		int factor = (int) (angle / MathUtils.PI2);
		angle -= factor * MathUtils.PI2;
		if (Math.abs(angle) > MathUtils.PI) {
			angle -= Math.signum(angle) * MathUtils.PI2;
		}
		return angle;
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	class TrafficRayCastCallback
			implements RayCastCallback {

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
