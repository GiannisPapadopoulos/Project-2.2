package editor.traffic_objects;

public class Road {

	private RoadConnector start;
	private RoadConnector end;
	
	private Type type;
	private double capacity;
	
	public enum Type {
		OneLane,TwoLane,ThreeLane
	}
}
