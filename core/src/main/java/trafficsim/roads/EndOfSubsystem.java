package trafficsim.roads;

import paramatricCurves.ParametricCurve;

public class EndOfSubsystem extends Lane {

	private static Lane end;
	
	private EndOfSubsystem(ParametricCurve trajectory, float width) {
		super(trajectory, width);
	}
	
	public static Lane getEndOfSubsystem() {
		if(end ==null)
			end = new EndOfSubsystem(null,0);
		return end;
	}

}
