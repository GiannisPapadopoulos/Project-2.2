package functions;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.degRad;
import static functions.VectorUtils.getAngle;
import graph.Edge;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.roads.Lane;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;
import trafficsim.roads.SubSystem;

import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MovementFunctions {

	public static Vector2 getPredictedPosition(Entity entity, float dT) {
		PhysicsBodyComponent physComp = entity.getComponent(PhysicsBodyComponent.class);
		assert physComp != null;
		return physComp.getPosition().cpy().add(physComp.getLinearVelocity().scl(dT));
	}

	// TODO fix
	public static boolean isLeftTurn(RouteComponent routeComp) {
		float angle = getDeltaAngle(routeComp) * degRad;
		// 0.001 is for floating point accuracy
		boolean leftTurn = angle < -0.001 || angle > PI + 0.001;
		return leftTurn;
	}

	public static Road getRoad(Edge<NavigationObject> edge) {
		return (Road) edge.getData();
	}

	public static float getDeltaAngle(RouteComponent routeComp) {
		if (routeComp.isLastEdge()) {
			return 0;
		}
		Edge<NavigationObject> nextEdge = routeComp.getPath().getRoute().get(routeComp.getEdgeIndex() + 1).getEdge();
		// float angle = VectorUtils.getAngle(nextEdge.getData())
		// - VectorUtils.getAngle(routeComp.getCurrentEdge().getData());
		Vertex<NavigationObject> vertex1 = routeComp.getNextVertex();
		Vertex<NavigationObject> vertex2 = routeComp.getNextVertex().getNeighbor(nextEdge);
		return getAngle(vertex1.getData().getPosition(), vertex2.getData().getPosition());
	}

	/** Build waypoints for the current edge */
	public static List<Vector2> buildWaypointsParametric(RouteComponent routeComp, Vector2 car_loc) {
		return buildWaypointsParametric(routeComp, 10, car_loc);
	}

	/** Build waypoints for the current edge */
	public static List<Vector2> buildWaypointsParametric(RouteComponent routeComp, int numPoints, Vector2 car_loc) {
		Road road = getRoad(routeComp.getCurrentEdge());
		SubSystem transitionPath = road.getSubSystem();
		return buildWaypointsParametric(transitionPath, numPoints, car_loc);

		// Vertex<NavigationObject> v1 = routeComp.getCurrentVertex();
		// Vertex<NavigationObject> v2 = v1.getNeighbor(routeComp.getCurrentEdge());
		// SubSystem transitionPath2 = road.requestTransitionPath(v2.getData(), v1.getData());
		// // System.out.println(transitionPath2 != null);
		// // System.out.println("tp ");
		// List<Vector2> waypoints = transitionPath.getLanes().get(0).get(0).getTrajectory().getSamplePoints(numPoints);
		// return waypoints;
	}

	public static List<Vector2> buildWaypointsParametric(SubSystem transitionPath, Vector2 carLoc) {
		return buildWaypointsParametric(transitionPath, 20, carLoc);
	}

	/**
	 * Builds waypoints for the given subsystem
	 * TODO Generalize for multiple lanes
	 */
	public static List<Vector2> buildWaypointsParametric(SubSystem transitionPath, int numPoints, Vector2 car_loc) {
		ArrayList<Lane> closest = null;
		float closest_dist = Float.MAX_VALUE;
		for(ArrayList<Lane> lanes : transitionPath.getLanes())  {
			if(closest_dist>VectorUtils.getLength(lanes.get(0).getStart(),car_loc)) {
				closest = lanes;
				closest_dist = VectorUtils.getLength(lanes.get(0).getStart(),car_loc);
			}
		}
		List<Vector2> waypoints = new ArrayList<Vector2>();
		for(Lane lane : closest) {
			waypoints = lane.getTrajectory().addSamplePoints(waypoints,numPoints);
		}
		return waypoints;
	}
	
	public static List<Vector2> buildWaypointsParametricInLane(SubSystem transitionPath, int numPoints, Vector2 car_loc) {
		return null;
	}
	
	public static int determineCurrentLane(SubSystem transitionPath, Vector2 car_loc) {
		float closest_dist = Float.MAX_VALUE;
		ArrayList<Lane> closest = null;
		for(ArrayList<Lane> lanes : transitionPath.getLanes())  {
			if(closest_dist>VectorUtils.getLength(lanes.get(0).getStart(),car_loc)) {
				closest = lanes;
				closest_dist = VectorUtils.getLength(lanes.get(0).getStart(),car_loc);
			}
		}
		return transitionPath.getLanes().indexOf(closest);
	}

	/** Constraint angle to [-pi, pi] */
	public static float constrainAngle(float angle) {
		int factor = (int) (angle / MathUtils.PI2);
		angle -= factor * MathUtils.PI2;
		if (Math.abs(angle) > MathUtils.PI) {
			angle -= Math.signum(angle) * MathUtils.PI2;
		}
		assert -MathUtils.PI <= angle && MathUtils.PI >= angle : angle;
		return angle;
	}
}
