package trafficsim.systems;

import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import trafficsim.components.AccelerationComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.roads.Road;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

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
		super(Aspect.getAspectForAll(SteeringComponent.class, RouteComponent.class, PhysicsBodyComponent.class));
	}

	// TODO clean this up
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (routeComponentMapper.has(entity) && steeringComponentMapper.has(entity)
				&& routeComponentMapper.get(entity).isSet()) {
				RouteComponent routeComp = routeComponentMapper.get(entity);
				SteeringComponent steeringComp = steeringComponentMapper.get(entity);
				PhysicsBodyComponent physComp = physicsBodyMapper.get(entity);

				if (steeringComp.getState() == State.ARRIVED) {
					Vector2 force = physComp.getLinearVelocity().cpy().scl(-1).clamp(0, steeringComp.getMaxForce());
					Vector2 newVel = physComp.getLinearVelocity().cpy().add(force);
					physComp.setLinearVelocity(newVel);
					continue;
				}

				Vector2 target = getTarget(routeComp);

				float dst = target.dst(physComp.getPosition());
				float threshold = 3;
				if (dst < threshold) {

					if (routeComp.getEdgeIndex() < routeComp.getRoute().size() - 1) {
						routeComp.setCurrentVertex(routeComp.getNextVertex());
						routeComp.setEdgeIndex(routeComp.getEdgeIndex() + 1);
					}
					else {
						steeringComp.setState(State.ARRIVED);
					}
				}

				Vector2 force = SeekBehavior.getForce(physComp.getPosition(), target);
				// TODO Define maxForce in relation to mass
				force.clamp(0, steeringComp.getMaxForce());
				Vector2 newVel = physComp.getLinearVelocity().cpy().add(force);
				float maxSpeed = Math.min(	routeComp.getCurrentEdge().getData().getSpeedLimit(),
											maxSpeedMapper.get(entity).getSpeed());
				newVel.clamp(0, maxSpeed);
				float deltaA = getAngleInRads(routeComp) - physComp.getAngle();
				deltaA = constraintAngle(deltaA);
				// TODO extract constant
				if (Math.abs(deltaA) > 0.05) {
					newVel.scl(0.9f);
					physComp.applyTorque(steeringComp.getMaxTorque() * deltaA, true);
				}
				else {
					physComp.setAngularVelocity(0);
				}

				physComp.setLinearVelocity(newVel);
			}
		}
	}

	private float getAngleInRads(RouteComponent routeComp) {
		boolean fromAtoB = routeComp.getCurrentVertex() == routeComp.getCurrentEdge()
																	.getAdjacentVertexIterator()
																	.next();
		Road road = routeComp.getCurrentEdge().getData();
		Vector2 roadVector = VectorUtils.getVector(road);
		if (!fromAtoB) {
			roadVector.scl(-1);
		}
		return roadVector.angle() * MathUtils.degRad;
	}

	public static Vector2 getTarget(RouteComponent routeComp) {
		Vector2 target = VectorUtils.getMidPoint(routeComp.getNextVertex().getData());
		Vector2 laneCorrection = VectorUtils.getUnitPerpendicularVector(target).scl(LANE_WIDTH / 2.0f);
		// To reduce the chance of stepping on the lane
		laneCorrection.scl(1.2f);

		boolean fromAtoB = routeComp.getCurrentVertex() == routeComp.getCurrentEdge()
																	.getAdjacentVertexIterator()
																	.next();
		if (fromAtoB) {
			laneCorrection.scl(-1);
		}
		target.add(laneCorrection);
		return target;
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
