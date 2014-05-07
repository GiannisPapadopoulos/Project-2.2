package functions;

import static com.badlogic.gdx.math.MathUtils.PI;
import static functions.VectorUtils.getAngle;
import static functions.VectorUtils.getVector;
import graph.Edge;
import graph.Vertex;
import trafficsim.components.RouteComponent;
import trafficsim.roads.Road;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MovementFunctions {

	/** Returns true if the next turn is a right one */
	public static boolean isRightTurn(RouteComponent routeComp) {
		if (routeComp.isLastEdge()) {
			return false;
		}
		float deltaAngle = getDeltaAngle(routeComp);
		return deltaAngle < 0 || deltaAngle > PI;
	}



	// private Vector2 vector(Entity entityA, Entity entityB) {
	// assert physicsBodyMapper.has(entityA) && physicsBodyMapper.has(entityB);
	// return physicsBodyMapper.get(entityB).getPosition().cpy().sub(physicsBodyMapper.get(entityA).getPosition());
	// }

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

	public static boolean fromAtoB(RouteComponent routeComp) {
		return routeComp.getCurrentVertex() == routeComp.getCurrentEdge().getAdjacentVertexIterator().next();
	}

	public static Vector2 getTarget(RouteComponent routeComp) {
		boolean fromAtoB = fromAtoB(routeComp);
		// Vector2 target = VectorUtils.getMidPoint(routeComp.getNextVertex().getData());
		Vector2 target = fromAtoB ? routeComp.getCurrentEdge().getData().getPointB().cpy() : routeComp.getCurrentEdge()
																										.getData()
																										.getPointA()
																										.cpy();
		Vector2 laneCorrection = getVector(routeComp.getCurrentEdge().getData()).cpy().nor().rotate(90);
		// To reduce the chance of stepping on the lane
		laneCorrection.scl(1.1f);

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


	public static float constrainAngle(float angle) {
		int factor = (int) (angle / MathUtils.PI2);
		angle -= factor * MathUtils.PI2;
		if (Math.abs(angle) > MathUtils.PI) {
			angle -= Math.signum(angle) * MathUtils.PI2;
		}
		return angle;
	}
}
