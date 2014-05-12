package trafficsim.roads;

import lombok.Getter;
import paramatricCurves.ParametricCurve;

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

	public boolean isInProximity(Vector2 object_pos) {
		for (Double t : trajectory.getR_t().getDiscreteCover(100))
			if (euclid(object_pos, trajectory.getPoint(t)) < width)
				return true;
		return false;
	}

	private double euclid(Vector2 a, Vector2 b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}

}
