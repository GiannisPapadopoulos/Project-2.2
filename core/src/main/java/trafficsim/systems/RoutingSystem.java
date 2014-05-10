package trafficsim.systems;

import static functions.MovementFunctions.buildWaypoints;
import static functions.MovementFunctions.isRightTurn;
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
 * System responsible for updating the waypoints for each car, i.e. the next target position
 * 
 * @author Giannis Papadopoulos
 */
public class RoutingSystem
		extends EntitySystem {

	@Mapper
	private ComponentMapper<RouteComponent> routeComponentMapper;
	@Mapper
	private ComponentMapper<SteeringComponent> steeringComponentMapper;
	@Mapper
	private ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	private ComponentMapper<MovementComponent> movementComponentMapper;

	// Start turning left
	private float leftTurnThreshold = 0.2f;
	private float rightTurnThreshold = 1.2f;
	// Arrived at destination
	private float arrivalThreshold = 2.5f;

	private float threshold = 1.0f;

	@SuppressWarnings("unchecked")
	public RoutingSystem() {
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
				Vector2 target = routeComp.getNextWaypoint();
				float distanceToTarget = target.dst(physComp.getPosition());
				if (distanceToTarget < threshold) {
					updatePath(routeComp);
					System.out.println(routeComp.getEdgeIndex() + " w " + routeComp.getWayPointIndex() + " "
										+ routeComp.getNextWaypoint());
				}
			}
		}
	}

	private void updatePath(RouteComponent routeComp) {
		if (routeComp.getWayPointIndex() < routeComp.getWayPoints().size() - 1) {
			routeComp.incrementWaypointIndex();
		}
		else {
			if (!routeComp.isLastEdge()) {
				routeComp.setCurrentVertex(routeComp.getNextVertex());
				routeComp.incrementEdgeIndex();
				routeComp.setWayPoints(buildWaypoints(routeComp));
				routeComp.setWayPointIndex(0);
			}
			else {
				// TODO set arrival behavior
			}
		}
	}

	public void updatePath(RouteComponent routeComp, SteeringComponent steeringComp, float distanceToTarget) {
		if (routeComp.isLastEdge() && steeringComp.getState() == State.DEFAULT && distanceToTarget < arrivalThreshold) {
			steeringComp.setState(State.ARRIVED);
		}
		else {
			float thresHold = isRightTurn(routeComp) ? rightTurnThreshold : leftTurnThreshold;
			if (distanceToTarget < thresHold) {
				// routeComp.update();
				routeComp.setCurrentVertex(routeComp.getNextVertex());
				routeComp.setEdgeIndex(routeComp.getEdgeIndex() + 1);
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
