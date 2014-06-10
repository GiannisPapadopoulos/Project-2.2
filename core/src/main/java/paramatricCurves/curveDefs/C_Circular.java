package paramatricCurves.curveDefs;

import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;
import paramatricCurves.CurveDefinition;
import paramatricCurves.CurveType;
import paramatricCurves.ParametricCurve;

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
	
	public C_Circular(Vector2 mid, float radius, float low, float high) {

		params = new Object[5];
		// P0: t_low
		// P1: t_high
		// P2: Vector mid
		// P3: radius
		params[0] = low;
		params[1] = high;
		params[2] = mid;
		params[3] = radius;
	}
	
	public C_Circular(Vector2 mid, float radius, Vector2 start,
			Vector2 end,boolean clockwise) {
		
		ParametricCurve dummy = new ParametricCurve(new C_Circular(mid,radius));
		double low_t = -10000000;
		double dist = Double.MAX_VALUE;
		
		for(Double d: dummy.getR_t().getDiscreteCover(1000))
			if(VectorUtils.getLength(dummy.getPoint(d),start)<dist) {
				low_t = d;
				dist = VectorUtils.getLength(dummy.getPoint(d),start); 
			}
		
	 double high_t = -1000000;
	 dist = Double.MAX_VALUE;
		
		for(Double d: dummy.getR_t().getDiscreteCover(1000))
			if(VectorUtils.getLength(dummy.getPoint(d),end)<dist) {
				high_t = d;
				dist = VectorUtils.getLength(dummy.getPoint(d),end); 
			}
		
		if(clockwise && high_t < low_t )
			high_t += 2*Math.PI;
		if(!clockwise && high_t > low_t) 
			high_t -= 2*Math.PI;
		
		params[0] = low_t;
		params[1] = high_t;
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
