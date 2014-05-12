package paramatricCurves.curveDefs;

import paramatricCurves.CurveDefinition;
import paramatricCurves.CurveType;

import com.badlogic.gdx.math.Vector2;

public class C_Linear implements CurveDefinition {

	private Object[] params;

	public C_Linear(Vector2 v1, Vector2 v2) {
		params = new Object[5];
		// P0: t_low
		// P1: t_high
		// P2: Constant or t variable for u_t
		// P3: b_shift
		// P4: a parameter for v_t
		if (v1.x != v2.x) {
			params[0] = (Float) 0.0f;
			params[1] = (Float) v2.x-v1.x;
			params[2] = "t";
			params[3] = (Float) v1.y;
			params[4] = (Float) ((v1.y - v2.y) / (v1.x - v2.x));

		} else if (v1.y != v2.y) {

			params[0] = (Float) v1.y;
			params[1] = (Float) v2.y;
			params[2] = (Float) v1.x;
			params[3] = 0.0f;
			params[4] = 1.0f;
		} else {
			System.out.println("ERROR... line with 0 distance!");
			System.exit(0);
		}
	}

	@Override
	public CurveType getCurveType() {
		return CurveType.LINE;
	}

	@Override
	public Object[] getParams() {
		return params;
	}

}
