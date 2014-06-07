package trafficsim.roads;

import paramatricCurves.ParametricCurve;
import paramatricCurves.curveDefs.C_Linear;
import trafficsim.TrafficSimConstants;

import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

public class LaneFactory {

	public static Lane createLane(ParametricCurve roadDef,int shift, Vector2 A, Vector2 B, int shiftDir) {
		System.out.println("********");
		System.out.println(A);
		System.out.println(B);

		return createShiftLinear(roadDef, shift, A, B, shiftDir);		
	}

	private static Lane createShiftLinear(ParametricCurve roadDef,int shift, Vector2 A, Vector2 B, int shiftDir) {
		
		float angle = VectorUtils.getAngle(A, B);
		angle += 90.0*shiftDir;
		System.out.println(angle);
		Vector2 vShift = VectorUtils.getUnitVector(angle);
		vShift.x *= (shift*TrafficSimConstants.LANE_WIDTH+TrafficSimConstants.LANE_WIDTH/2);
		vShift.y *= (shift*TrafficSimConstants.LANE_WIDTH+TrafficSimConstants.LANE_WIDTH/2);
		
		Vector2 Ashift = new Vector2();
		Ashift.x = A.x + vShift.x;
		Ashift.y = A.y + vShift.y;
		System.out.println(Ashift);
		
		Vector2 Bshift = new Vector2();
		Bshift.x = B.x + vShift.x;
		Bshift.y = B.y + vShift.y;
		System.out.println(Bshift);
				
		return new Lane(new ParametricCurve(new C_Linear(Ashift,Bshift)),TrafficSimConstants.LANE_WIDTH);
	}
	
}
