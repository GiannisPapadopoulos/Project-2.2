package trafficsim.systems;

import static functions.MovementFunctions.buildWaypointsParametric;
import graph.Edge;

import java.util.List;

import lombok.val;
import trafficsim.TrafficSimWorld;
import trafficsim.components.MovementComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.components.VehiclesOnRoadComponent;
import trafficsim.roads.NavigationObject;

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
	@Mapper
	private ComponentMapper<VehiclesOnRoadComponent> vehiclesOnRoadComponentMapper;

	// Start turning left
	private float leftTurnThreshold = 0.2f;
	private float rightTurnThreshold = 2.2f;
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
					updatePath(routeComp, steeringComp, car.getId());
					// System.out.println(routeComp.getEdgeIndex() + " w " + routeComp.getWayPointIndex() + " "
					// + routeComp.getNextWaypoint());
				}
			}
		}
	}

	private void updatePath(RouteComponent routeComp, SteeringComponent steeringComp, int carID) {
		if (routeComp.getWayPointIndex() < routeComp.getWayPoints().size() - 1) {
			routeComp.incrementWaypointIndex();
		}
		else {
			updateRoadReference(routeComp, carID, true);
			if (!routeComp.isLastEdge()) {
				NavigationObject nextEdge;
				Edge<NavigationObject> currentEdge = routeComp.getCurrentEdge();
				routeComp.setCurrentVertex(routeComp.getNextVertex());
				routeComp.incrementEdgeIndex();
				val trans = routeComp.getCurrentVertex()
							.getData()
							.requestTransitionPath(currentEdge.getData(), routeComp.getCurrentEdge().getData());

				// assert trans != null;
				// List<Vector2> waypoints = buildWaypoints(routeComp);
				List<Vector2> waypoints = buildWaypointsParametric(routeComp);
				routeComp.setWayPoints(waypoints);
				routeComp.setWayPointIndex(0);
				updateRoadReference(routeComp, carID, false);
				System.out.println(routeComp.getCurrentVertex());
			}
			else {
				// TODO arrival behavior
				steeringComp.setState(State.ARRIVED);
			}
		}
	}

	private void updateRoadReference(RouteComponent routeComp, int carID, boolean remove) {
		int edgeIndex = routeComp.getCurrentEdge().getID();
		int edgeEntityID = ((TrafficSimWorld) world).getEdgeToEntityMap().get(edgeIndex);
		VehiclesOnRoadComponent vehiclesOnRoad = vehiclesOnRoadComponentMapper.get(world.getEntity(edgeEntityID));
		if (remove) {
			boolean b = vehiclesOnRoad.getVehiclesOnLaneIDs().remove(carID);
			// System.out.println("edge " + edgeIndex + " " + b + " v " + " c " + carID + " "
			// + vehiclesOnRoad.getVehiclesOnLaneIDs());
			assert b;
		}
		else {
			// System.out.println("adding " + "edge " + edgeIndex + " id " + carID);
			vehiclesOnRoad.getVehiclesOnLaneIDs().add(carID);
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}