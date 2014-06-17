package trafficsim.systems;

import static functions.MovementFunctions.buildWaypointsParametric;
import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.SPEED_RATIO;

import java.util.ArrayList;
import java.util.List;

import trafficsim.TrafficSimWorld;
import trafficsim.components.MovementComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.components.VehiclesOnRoadComponent;
import trafficsim.roads.Lane;
import trafficsim.roads.Road;
import trafficsim.roads.SubSystem;

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

	/** Threshold for changing to the next waypoint */
	private float waypointThreshold = 1.5f * SPEED_RATIO;

	/** Threshold for switching to the next parametric curve */
	private float switchThreshold = CAR_LENGTH * SPEED_RATIO;

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
			if (routeComp.isSet()) {
				Vector2 target = routeComp.getNextWaypoint();
				float distanceToTarget = target.dst(physComp.getPosition());
				if (distanceToTarget < waypointThreshold) {
					updatePath(routeComp, steeringComp, physComp, car.getId());
					// System.out.println(routeComp.getEdgeIndex() + " w " + routeComp.getWayPointIndex() + " "
					// + routeComp.getNextWaypoint());
				}
			}
		}
	}

	/**
	 * Updates the path, by setting the next waypoint or building new waypoints if necessary
	 * The assumption is that the path is always vertex, edge, vertex etc
	 */
	private void updatePath(RouteComponent routeComp, SteeringComponent steeringComp, PhysicsBodyComponent physComp, int carID) {
		if (routeComp.isLastEdge()) {
			if (routeComp.isLastWaypoint()) {
				// TODO arrival behavior
				steeringComp.setState(State.ARRIVED);
			}
			else {
				updateWayPointIndex(routeComp, physComp);
			}
		}
		else {
			SubSystem transition = getNextSubsystem(routeComp);
			if (transition == null) {
				System.out.println(routeComp.getCurrentVertex() + " " + routeComp.getCurrentEdge());
			}
			ArrayList<Vector2> nextTransitionPoints = getNextTransitionPoints(transition);

			for (Vector2 nextTransitionPoint : nextTransitionPoints) {
				if (physComp.getPosition().dst(nextTransitionPoint) < switchThreshold
						|| reachedLastWaypoint(routeComp, physComp)) {
					List<Vector2> waypoints = buildWaypointsParametric(
							transition, physComp.getPosition());
					routeComp.setWayPoints(waypoints);
					routeComp.setWayPointIndex(0);
					if (routeComp.isFollowingEdge()) {
						// If we are leaving an edge, remove the car from the
						// list
						updateRoadReference(routeComp, carID, true);

					} else {
						// If we are entering an edge, add the car to the list
						routeComp.setCurrentVertex(routeComp.getNextVertex());
						routeComp.incrementEdgeIndex();
						updateRoadReference(routeComp, carID, false);
					}
					routeComp.setFollowingEdge(!routeComp.isFollowingEdge());
				return;
				} else {
					
				}
			}
			updateWayPointIndex(routeComp, physComp);
		}
	}

	private boolean reachedLastWaypoint(RouteComponent routeComp, PhysicsBodyComponent physComp) {
		Vector2 target = routeComp.getNextWaypoint();
		float distanceToTarget = target.dst(physComp.getPosition());
		return distanceToTarget < waypointThreshold && routeComp.isLastWaypoint();
	}

	private void updateWayPointIndex(RouteComponent routeComp, PhysicsBodyComponent physComp) {
		Vector2 target = routeComp.getNextWaypoint();
		float distanceToTarget = target.dst(physComp.getPosition());
		if (distanceToTarget < waypointThreshold && !routeComp.isLastWaypoint()) {
			routeComp.incrementWaypointIndex();
		}
	}

	private ArrayList<Vector2> getNextTransitionPoints(SubSystem transition) {
		ArrayList<Vector2> result = new ArrayList<Vector2>();
		for(ArrayList<Lane> lane: transition.getLanes()){
			result.add(lane.get(0).getStart());
		}
		return result;
	}

	/** Returns the next edge or crossroad subsystem */
	private SubSystem getNextSubsystem(RouteComponent routeComp) {
		SubSystem transition;
		if (routeComp.isFollowingEdge()) {
			transition = routeComp.getNextVertex()
											.getData()
											.requestTransitionPath(routeComp.getCurrentEdge().getData(),
																	routeComp.getNextEdge().getData());
		}
		else {
			transition = ((Road) routeComp.getNextEdge().getData()).getSubSystem();
		}
		return transition;
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