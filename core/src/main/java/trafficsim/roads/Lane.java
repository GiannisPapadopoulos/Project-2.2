package trafficsim.roads;

import lombok.Getter;
import paramatricCurves.ParametricCurve;
import trafficsim.TrafficSimConstants;

import com.badlogic.gdx.math.Vector2;

public class Lane {

	@Getter
	private ParametricCurve trajectory;
	@Getter
	private float width;

	public Lane(ParametricCurve trajectory, float width) {
		this.trajectory = trajectory;
		this.width = width;
	}
	
	public Lane(ParametricCurve trajectory) {
		this.trajectory = trajectory;
		this.width = TrafficSimConstants.LANE_WIDTH;
	}

	public boolean isInProximity(Vector2 object_pos) {
		for (Double t : trajectory.getR_t().getDiscreteCover(
				TrafficSimConstants.SAMPLING_DENSITY))
			if (euclid(object_pos, trajectory.getPoint(t)) < width)
				return true;
		return false;
	}

	public double getDistance(Vector2 object_pos) {
		double min_dist = Double.MAX_VALUE;
		for (Double t : trajectory.getR_t().getDiscreteCover(
				TrafficSimConstants.SAMPLING_DENSITY))
			if (euclid(object_pos, trajectory.getPoint(t)) < min_dist)
				min_dist = euclid(object_pos, trajectory.getPoint(t));
		return min_dist;
	}

	public Vector2 getMinDistanceLocation(Vector2 object_pos) {
		Vector2 min_dist_loc = null;
		for (Double t : trajectory.getR_t().getDiscreteCover(
				TrafficSimConstants.SAMPLING_DENSITY))
			if (euclid(object_pos, trajectory.getPoint(t)) < euclid(object_pos,
					min_dist_loc))
				min_dist_loc = trajectory.getPoint(t);
		return min_dist_loc;
	}
	
	public Vector2 getStart() {
		return trajectory.getPoint(trajectory.getR_t().getLow());
	}
	
	public Vector2 getEnd() {
		return trajectory.getPoint(trajectory.getR_t().getHigh());
	}

	private double euclid(Vector2 a, Vector2 b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}

}
