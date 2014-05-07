package trafficsim.systems;

import static functions.MovementFunctions.getTarget;
import static functions.MovementFunctions.isRightTurn;
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

	// Start turning left
	float leftTurnThreshold = 0.2f;
	float rightTurnThreshold = 1.2f;
	// Arrived at destination
	float arrivalThreshold = 2.5f;

	@SuppressWarnings("unchecked")
	public RoutingSystem() {
		super(Aspect.getAspectForAll(SteeringComponent.class, RouteComponent.class, PhysicsBodyComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity car = entities.get(i);
			RouteComponent routeComp = routeComponentMapper.get(car);
			SteeringComponent steeringComp = steeringComponentMapper.get(car);
			PhysicsBodyComponent physComp = physicsBodyMapper.get(car);
			if (routeComp.isSet()) {
				Vector2 target = getTarget(routeComp);
				float distanceToTarget = target.dst(physComp.getPosition());
				updatePath(routeComp, steeringComp, distanceToTarget);
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
				// routeComp.setCurrentVertex(routeComp.getNextVertex());
				// routeComp.setEdgeIndex(routeComp.getEdgeIndex() + 1);
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
