package trafficsim.roads;

import java.util.ArrayList;

import lombok.Getter;
import lombok.ToString;
import paramatricCurves.ParametricCurve;

import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

/**
 * Class that holds data related to a road
 * 
 * @author Giannis Papadopoulos
 */
@Getter
@ToString
public class Road extends NavigationObject {

	/** The direction of a road, UPSTREAM is from pointA to pointB */
	public enum Direction {
		UPSTREAM, DOWNSTREAM, BOTH;
	}

	private ArrayList<Lane> AtoB;
	private ArrayList<Lane> BtoA;

	/** The left point */
	private Vector2 pointA;
	/** The right point */
	private Vector2 pointB;

	// @formatter:off
	/*
	 * Given a road, points A and B are: ########################
	 * -A#----------------------#-B ########################
	 */
	// @formatter:on

	/** Number of lanes in each direction */
	private int numLanes;
	/** The direction of the road */
	private Direction direction;

	private float speedLimit;

	public Road(Vector2 pointA, Vector2 pointB, int numLanes,
			Direction direction, float speedLimit) {

		this.pointA = pointA;
		this.pointB = pointB;
		this.numLanes = numLanes;
		this.direction = direction;
		this.speedLimit = speedLimit;
	}

	public Road(ParametricCurve roadDef, int AtoBnum, int BtoAnum,
			float speedLimit) {
		
		this.pointA = roadDef.getPoint(roadDef.getR_t().getLow());
		this.pointB = roadDef.getPoint(roadDef.getR_t().getHigh());
		this.speedLimit = speedLimit;
		
		AtoB = new ArrayList<>();
		BtoA = new ArrayList<>();
		
		for (int i = 0; i < AtoBnum; i++)
			AtoB.add(LaneFactory.createLane(roadDef, i, pointA, pointB, +1));
		for (int i = 0; i < BtoAnum; i++)
			BtoA.add(LaneFactory.createLane(roadDef, i, pointA, pointB, -1));


	}

	public Vector2 getPointC() {
		Vector2 v = VectorUtils.getMidPoint(pointA, pointB);

		float angle = VectorUtils.getAngle(pointA, pointB);
		angle += 90.0;

		Vector2 vAdd = VectorUtils.getUnitVector(angle);
		vAdd.x *= VectorUtils.getLength(pointA, pointB) / 2;
		vAdd.y *= VectorUtils.getLength(pointA, pointB) / 2;

		v.add(vAdd);

		return v;

	}

	public Vector2 getPointD() {
		Vector2 v = VectorUtils.getMidPoint(pointA, pointB);

		float angle = VectorUtils.getAngle(pointA, pointB);
		angle -= 90.0;

		Vector2 vAdd = VectorUtils.getUnitVector(angle);
		vAdd.x *= VectorUtils.getLength(pointA, pointB) / 2;
		vAdd.y *= VectorUtils.getLength(pointA, pointB) / 2;

		v.add(vAdd);

		return v;
	}

}
