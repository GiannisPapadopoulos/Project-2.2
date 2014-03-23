package trafficsim.roads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

/**
 * Class that holds data related to a road
 * 
 * @author Giannis Papadopoulos
 */
@AllArgsConstructor
@Getter
@ToString
public class Road {

	/** The direction of a road, UPSTREAM is from pointA to pointB */
	public enum Direction {
		UPSTREAM,
		DOWNSTREAM,
		BOTH;
	}

	/** The left point */
	private Vector2 pointA;
	/** The right point */
	private Vector2 pointB;
	
	public Vector2 getPointC() {
		Vector2 v = VectorUtils.getMidPoint(pointA, pointB);
		
		float angle = VectorUtils.getAngle(pointA, pointB);
		angle+= 90.0;
		
		Vector2 vAdd = VectorUtils.getUnitVector(angle);
		vAdd.x *= VectorUtils.getLength(pointA, pointB)/2;
		vAdd.y *= VectorUtils.getLength(pointA, pointB)/2;
		
		v.add(vAdd);
		
		return v;
		
		
		}
	
	public Vector2 getPointD() {
		Vector2 v = VectorUtils.getMidPoint(pointA, pointB);
		
		float angle = VectorUtils.getAngle(pointA, pointB);
		angle-= 90.0;
		
		Vector2 vAdd = VectorUtils.getUnitVector(angle);
		vAdd.x *= VectorUtils.getLength(pointA, pointB)/2;
		vAdd.y *= VectorUtils.getLength(pointA, pointB)/2;
		
		v.add(vAdd);
		
		return v;
	}

	//@formatter:off
	/*
	 * Given a road, points A and B are:
	 *   ########################
	 * -A#----------------------#-B
	 *   ########################
	 */
	//@formatter:on

	/** Number of lanes in each direction */
	private int numLanes;
	/** The direction of the road */
	private Direction direction;

	private float speedLimit;

}
