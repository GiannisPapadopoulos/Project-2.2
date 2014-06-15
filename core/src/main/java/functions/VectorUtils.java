package functions;

import graph.Element;
import trafficsim.roads.NavigationObject;
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

	public static float calculateExpectedTime(NavigationObject road) {
		return road.getLength() / road.getSpeedLimit();
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

	public static Vector2 getVector(Element<Road> element) {
		return getVector(element.getData());
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

	public static Vector2 getMidPoint(NavigationObject navObj) {
		
		return new Vector2(navObj.getPosition().x,navObj.getPosition().y);
	}

	public static Vector2 getMidPoint(Vector2 pointA, Vector2 pointB) {
		float xm = (pointA.x + pointB.x) / 2;
		float ym = (pointA.y + pointB.y) / 2;
		return new Vector2(xm, ym);
	}

	public static Vector2 getUnitVectorDegrees(float angleInDegrees) {
		return new Vector2(MathUtils.cos(angleInDegrees * MathUtils.degreesToRadians),
							MathUtils.sin(angleInDegrees * MathUtils.degreesToRadians));
	}

	public static Vector2 getUnitVectorRads(float angleInRads) {
		return new Vector2(MathUtils.cos(angleInRads * MathUtils.degreesToRadians),
							MathUtils.sin(angleInRads * MathUtils.degreesToRadians));
	}

	public static double getAbsAngleDifference(Vector2 v1, Vector2 v2) {
		return Math.abs(v1.angle() - v2.angle());
	}
	
	public static double getAngleDifference(Vector2 v1, Vector2 v2) {
		return v1.angle() - v2.angle();
	}

	public static Vector2 multiplyVector(Vector2 v, float a) {
		v.x *= a;
		v.y *= a;
		return v;
	}

	public static Vector2 add2Vectors(Vector2 v1, Vector2 v2) {
		Vector2 result = new Vector2();
		result.x = v1.x + v2.x;
		result.y = v1.y + v2.y;
		return result;
	}

	// public static Vector2 getUnitPerpendicularVector(Vector2 vector) {
	// return new Vector2(vector).nor().rotate(90);
	// }

}
