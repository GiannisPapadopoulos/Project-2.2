package functions;

import graph.Element;
import trafficsim.roads.Road;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VectorUtils {

	public static float getLength(Road road) {
		return getLength(road.getPointA(), road.getPointB());
	}

	public static float getLength(Vector2 pointA, Vector2 pointB) {
		return pointA.dst(pointB);
	}

	public static float calculateExpectedTime(Road road) {
		return getLength(road) / road.getSpeedLimit();
	}

	public static Vector2 getVector(Element<Road> element) {
		return getVector(element.getData());
	}

	public static Vector2 getVector(Road road) {
		return road.getPointB().cpy().sub(road.getPointA());
	}

	public static Vector2 getVector(Vector2 pointA, Vector2 pointB) {
		return pointB.cpy().sub(pointA);
	}

	public static Vector2 getVector(Road roadA, Road roadB) {
		return getVector(getMidPoint(roadA), getMidPoint(roadB));
	}

	public static float getAngle(Road road) {
		return getVector(road).angle();
	}

	public static float getAngle(Vector2 pointA, Vector2 pointB) {
		return getVector(pointA, pointB).angle();
	}

	public static float getAngle(Road roadA, Road roadB) {
		return getAngle(getMidPoint(roadA), getMidPoint(roadB));
	}

	public static Vector2 getMidPoint(Road road) {
		return getMidPoint(road.getPointA(), road.getPointB());
	}

	public static Vector2 getMidPoint(Vector2 pointA, Vector2 pointB) {
		float xm = (pointA.x + pointB.x) / 2;
		float ym = (pointA.y + pointB.y) / 2;
		return new Vector2(xm, ym);
	}

	public static Vector2 getUnitVector(float angle) {
		return new Vector2(MathUtils.cos(angle * MathUtils.degreesToRadians),
				MathUtils.sin(angle * MathUtils.degreesToRadians));
	}

	// public static Vector2 getUnitPerpendicularVector(Vector2 vector) {
	// return new Vector2(vector).nor().rotate(90);
	// }

}
