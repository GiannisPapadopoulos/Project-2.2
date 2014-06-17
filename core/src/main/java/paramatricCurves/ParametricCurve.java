package paramatricCurves;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import paramatricCurves.curveDefs.C_Linear;

import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

public class ParametricCurve {

	private FunctionT u_t;
	private FunctionT v_t;

	@Getter
	private RangeT r_t;

	private Vector2 startDir;
	private Vector2 endDir;

	public ParametricCurve(final CurveDefinition cd) {

		if (cd.getCurveType() == CurveType.LINE) {
			u_t = new FunctionT(new Get_F_t() {

				@Override
				public float getFt(float t) {
					if (cd.getParams()[2] instanceof String)
						return t + (Float) cd.getParams()[5];
					else if (cd.getParams()[2] instanceof Float)
						return (Float) cd.getParams()[2];
					else {
						System.out.println("SOMETHING WENT WRONG 01");
						return 0;
					}
				}
			});

			v_t = new FunctionT(new Get_F_t() {

				@Override
				public float getFt(float t) {

					if (cd.getParams()[2] instanceof String)
						return (Float) (cd.getParams()[3])
								+ ((Float) (cd.getParams()[4])) * t;
					else if (cd.getParams()[2] instanceof Float)
						return t;
					else {
						System.out.println("SOMETHING WENT WRONG 01");
						return 0;
					}

				}
			});
			r_t = new RangeT((Float) (cd.getParams()[0]),
					(Float) (cd.getParams()[1]));

		} else if (cd.getCurveType() == CurveType.CIRCULAR) {
			u_t = new FunctionT(new Get_F_t() {

				@Override
				public float getFt(float t) {

					return (float) (((Vector2) (cd.getParams()[2])).x + (Float) (cd
							.getParams()[3]) * Math.cos(t));
				}

			});

			v_t = new FunctionT(new Get_F_t() {

				@Override
				public float getFt(float t) {

					return (float) (((Vector2) (cd.getParams()[2])).y + (Float) (cd
							.getParams()[3]) * Math.sin(t));
				}

			});
			r_t = new RangeT((Float) (cd.getParams()[0]),
					(Float) (cd.getParams()[1]));

		} else {

		}
	}

	public Vector2 getPoint(float t) {
		return new Vector2((float) u_t.getFt(t), (float) v_t.getFt(t));
	}

	public List<Vector2> addSamplePoints(List<Vector2> samplePoints,
			int numPoints) {
		List<Float> range = r_t.getDiscreteCover(numPoints);
		for (Float value : range) {
			samplePoints.add(getPoint(value));
		}
		return samplePoints;
	}

	// Vector directing out of pc <-- |----------|
	public Vector2 getStartDirection() {
		if (startDir == null) {
			ArrayList<Float> precise = r_t.getDiscreteCover(1000);
			Vector2 a = getPoint(precise.get(0));
			Vector2 b = getPoint(precise.get(1));
			a.x = a.x - b.x;
			a.y = a.y - b.y;
			startDir = a;
		}

		Vector2 result = new Vector2();
		result.x = startDir.x;
		result.y = startDir.y;
		return result;
	}

	// Vector directing out of pc |----------| -->
	public Vector2 getEndDirection() {
		if (endDir == null) {
			ArrayList<Float> precise = r_t.getDiscreteCover(1000);
			Vector2 a = getPoint(precise.get(precise.size() - 1));
			Vector2 b = getPoint(precise.get(precise.size() - 2));
			a.x = a.x - b.x;
			a.y = a.y - b.y;
			endDir = a;
		}
		Vector2 result = new Vector2();
		result.x = endDir.x;
		result.y = endDir.y;
		return result;
	}

	public List<Vector2> addSampleFrom(List<Vector2> samplePoints,
			int numPoints, Vector2 v2) {

		ParametricCurve dummy = new ParametricCurve(new C_Linear(
				getPoint(r_t.getLow()), getPoint(r_t.getHigh())));
		float low_t = -10000000;
		float dist = Float.MAX_VALUE;

		for (Float d : dummy.getR_t().getDiscreteCover(1000))
			if (VectorUtils.getLength(dummy.getPoint(d), v2) < dist) {
				low_t = d;
				dist = VectorUtils.getLength(dummy.getPoint(d), v2);
			}
		dummy.r_t.setLow(low_t);

		dummy.addSamplePoints(samplePoints, 50);

		return samplePoints;
	}

	@Override
	public String toString() {
		return "ParametricCurve [u_t=" + u_t + ", v_t=" + v_t + ", r_t=" + r_t
				+ ", startDir=" + startDir + ", endDir=" + endDir + "]";
	}
}
