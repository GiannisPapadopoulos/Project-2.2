package paramatricCurves.curveDefs;

import com.badlogic.gdx.math.Vector2;

import paramatricCurves.CurveDefinition;
import paramatricCurves.CurveType;

public class C_Circular implements CurveDefinition {

	private Object[] params;
	
	public C_Circular(Vector2 mid, float radius) {

		params = new Object[5];
		// P0: t_low
		// P1: t_high
		// P2: Vector mid
		// P3: radius
		params[0] = 0.0f;
		params[1] = (float)(2*Math.PI);
		params[2] = mid;
		params[3] = radius;
	}
	
	@Override
	public CurveType getCurveType() {

		return CurveType.CIRCULAR;
	}

	@Override
	public Object[] getParams() {
		return params;
	}

}
