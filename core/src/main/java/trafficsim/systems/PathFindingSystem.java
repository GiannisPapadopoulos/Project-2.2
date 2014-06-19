package trafficsim.systems;

import static functions.MovementFunctions.buildWaypointsParametric;

import java.util.List;

import pathfinding.GraphAction;
import pathfinding.GraphBasedAstar;
import pathfinding.GraphState;
import search.Path;
import trafficsim.TrafficSimWorld;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.VehiclesOnRoadComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

/**
 * System responsible for computing(by calling A*) a path between the assigned
 * source and target vertices The source vertex(assumed to be the current
 * position) is assigned at creation by the Spawning system and the target is
 * assigned by the DestinationSystem
 * 
 * @author Giannis Papadopoulos
 */
public class PathFindingSystem extends EntitySystem {

	@Mapper
	private ComponentMapper<RouteComponent> routeComponentMapper;
	@Mapper
	private ComponentMapper<VehiclesOnRoadComponent> vehiclesOnRoadComponentMapper;

	@SuppressWarnings("unchecked")
	public PathFindingSystem() {
		super(Aspect.getAspectForAll(RouteComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity car = entities.get(i);
			if (routeComponentMapper.has(car)) {
				RouteComponent routeComp = routeComponentMapper.get(car);
				if (!routeComp.isSet() && routeComp.getSource() != null && routeComp.getTarget() != null) {
					Path<GraphState, GraphAction> path = new GraphBasedAstar().findRoute(	routeComp.getSource(),
																							routeComp.getTarget());
					assert path.isValidPath() : " No valid path found!" + car + " " + routeComp;
					routeComp.setPath(path);
					routeComp.setSet(true);
					routeComp.setCurrentVertex(routeComp.getSource());
					routeComp.setEdgeIndex(0);
					// TODO not tested
					List<Vector2> waypoints = buildWaypointsParametric(	routeComp,
																		((PhysicsBodyComponent) (car.getComponent(PhysicsBodyComponent.class))).getPosition());
					routeComp.setFollowingEdge(true);
					routeComp.setWayPoints(waypoints);
					updateRoadReference(routeComp, car.getId());
				}
			}
		}
	}

	private void updateRoadReference(RouteComponent routeComp, int carID) {
		int edgeIndex = routeComp.getCurrentEdge().getID();
		int edgeEntityID = ((TrafficSimWorld) world).getEdgeToEntityMap().get(
				edgeIndex);
		VehiclesOnRoadComponent vehiclesOnRoad = vehiclesOnRoadComponentMapper
				.get(world.getEntity(edgeEntityID));
		vehiclesOnRoad.getVehiclesOnLaneIDs().add(carID);
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
