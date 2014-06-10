package paramatricCurves;

import java.util.ArrayList;

import lombok.Getter;

import com.badlogic.gdx.math.Vector2;

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
				public double getFt(double t) {
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
				public double getFt(double t) {

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
				public double getFt(double t) {

					return ((Vector2) (cd.getParams()[2])).x
							+ (Float) (cd.getParams()[3]) * Math.cos(t);
				}

			});

			v_t = new FunctionT(new Get_F_t() {

				@Override
				public double getFt(double t) {

					return ((Vector2) (cd.getParams()[2])).y
							+ (Float) (cd.getParams()[3]) * Math.sin(t);
				}

			});
			r_t = new RangeT((Float) (cd.getParams()[0]),
					(Float) (cd.getParams()[1]));

		} else {

		}
	}

	public Vector2 getPoint(double t) {
		return new Vector2((float) u_t.getFt(t), (float) v_t.getFt(t));
	}

	public Vector2 getStartDirection() {
		if (startDir == null) {
			ArrayList<Double> precise = r_t.getDiscreteCover(1000);
			Vector2 a = getPoint(precise.get(0));
			Vector2 b = getPoint(precise.get(1));
			a.x = a.x - b.x;
			a.y = a.y - b.y;
			startDir = a;
		}
		return startDir;
	}

	public Vector2 getEndDirection() {
		if (endDir == null) {
			ArrayList<Double> precise = r_t.getDiscreteCover(1000);
			Vector2 a = getPoint(precise.get(precise.size() - 1));
			Vector2 b = getPoint(precise.get(precise.size() - 2));
			a.x = a.x - b.x;
			a.y = a.y - b.y;
			startDir = a;
		}
		return endDir;
	}
}
