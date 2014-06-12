package functions;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.degRad;
import static functions.VectorUtils.getAngle;
import static functions.VectorUtils.getVector;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import graph.Edge;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

import trafficsim.components.RouteComponent;
import trafficsim.roads.Road;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MovementFunctions {

	// TODO fix
	public static boolean isLeftTurn(RouteComponent routeComp) {
		float angle = getDeltaAngle(routeComp) * degRad;
		// 0.001 is for floating point accuracy
		boolean leftTurn = angle < -0.001 || angle > PI + 0.001;
		return leftTurn;
	}

	public static float getDeltaAngle(RouteComponent routeComp) {
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

	public static float getAngleOfCurrentEdgeInRads(RouteComponent routeComp) {
		Road road = routeComp.getCurrentEdge().getData();
		Vector2 roadVector = VectorUtils.getVector(road);
		if (!fromAtoB(routeComp)) {
			roadVector.scl(-1);
		}
		return roadVector.angle() * MathUtils.degRad;
	}

	// TODO make this robust
	public static boolean fromAtoB(RouteComponent routeComp) {
		return routeComp.getCurrentVertex() == routeComp.getCurrentEdge().getAdjacentVertexIterator().next();
	}

	/** Builds waypoints for the current edge */
	public static List<Vector2> buildWaypoints(RouteComponent routeComp) {
		return buildWaypoints(routeComp, 5);
	}

	/** Builds waypoints for the current edge */
	public static List<Vector2> buildWaypoints(RouteComponent routeComp, int number) {
		List<Vector2> waypoints = new ArrayList<Vector2>();
		boolean fromAtoB = fromAtoB(routeComp);
		Road road = routeComp.getCurrentEdge().getData();
		Vector2 start = fromAtoB ? road.getPointA().cpy() : road.getPointB().cpy();
		Vector2 finish = fromAtoB ? road.getPointB().cpy() : road.getPointA().cpy();
		// Vector2 laneCorrection = getVector(road).cpy().nor().rotate(90).scl(LANE_WIDTH / 2.0f);
		// if (fromAtoB) {
		// laneCorrection.scl(-1);
		// }
		// start.add(laneCorrection);
		// finish.add(laneCorrection);
		waypoints.add(start);
		Vector2 lane = finish.cpy().sub(start);
		Vector2 step = lane.cpy().scl(1.0f / (number - 1));
		for (int i = 1; i < number - 1; i++) {
			Vector2 waypoint = start.cpy().add(step.cpy().scl(i));
			waypoints.add(waypoint);
		}
		// To make left turns less wide
		if (isLeftTurn(routeComp)) {
			finish.add(lane.cpy().nor().scl(LANE_WIDTH));
		}
		waypoints.add(finish);
		// System.out.println(start + " f " + finish + " " + waypoints);
		return waypoints;
	}

	public static Vector2 getTarget(RouteComponent routeComp) {
		boolean fromAtoB = fromAtoB(routeComp);
		// Vector2 target = VectorUtils.getMidPoint(routeComp.getNextVertex().getData());
		Vector2 target = fromAtoB ? routeComp.getCurrentEdge().getData().getPointB().cpy() : routeComp.getCurrentEdge()
																										.getData()
																										.getPointA()
																										.cpy();
		// Vector2 corr = getVector(routeComp.getCurrentEdge().getData()).cpy().nor().scl(3);
		// if (!fromAtoB) {
		// corr.scl(-1);
		// }
		// target.add(corr);
		Vector2 laneCorrection = getVector(routeComp.getCurrentEdge().getData()).cpy()
																				.nor()
																				.rotate(90)
																				.scl(LANE_WIDTH / 2.0f);
		// To reduce the chance of stepping on the lane
		// laneCorrection.scl(1.1f);

		if (fromAtoB) {
			laneCorrection.scl(-1);
		}
		target.add(laneCorrection);
		return target;
	}
		// float deltaAngle = getDeltaAngle(routeComp) * degRad;
		// boolean leftTurn = deltaAngle > 0 && deltaAngle < PI;
		// // System.out.println(deltaAngle + " " + leftTurn);
		// if (leftTurn) {
		// Vector2 roadVector = getVector(routeComp.getCurrentEdge().getData());
		// if (!fromAtoB) {
		// roadVector.scl(-1);
		// }
		// // System.out.print("bef " + target + " ");
		// target.add(roadVector).nor().scl(1);
		// // System.out.println("after " + target);
		// }

	// private Vector2 vector(Entity entityA, Entity entityB) {
	// assert physicsBodyMapper.has(entityA) && physicsBodyMapper.has(entityB);
	// return physicsBodyMapper.get(entityB).getPosition().cpy().sub(physicsBodyMapper.get(entityA).getPosition());
	// }


	/** Constraint angle to [-pi, pi] */
	public static float constrainAngle(float angle) {
		int factor = (int) (angle / MathUtils.PI2);
		angle -= factor * MathUtils.PI2;
		if (Math.abs(angle) > MathUtils.PI) {
			angle -= Math.signum(angle) * MathUtils.PI2;
		}
		return angle;
	}
}
