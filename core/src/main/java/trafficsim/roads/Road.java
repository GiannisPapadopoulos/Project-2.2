package trafficsim.roads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import com.badlogic.gdx.math.Vector2;

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
