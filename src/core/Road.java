package core;

public class Road {
	private RoadEndpoint start;
	private RoadEndpoint end;
	private double length;
	private double speed;
	
	public Road(RoadEndpoint endpoint1, RoadEndpoint endpoint2, double roadlength, double roadspeed){
		start = endpoint1;
		end = endpoint2;
		length = roadlength;
		speed = roadspeed;
	}
	
	public Road(RoadEndpoint endpoint1, RoadEndpoint endpoint2, double roadspeed){
		this(endpoint1, endpoint2, endpoint1.getDistance(endpoint2), roadspeed);
	}
	
	public double getWeight(){
		return length / speed;
	}
	
	public RoadEndpoint getStart(){
		return start;
	}
	
	public RoadEndpoint getEnd(){
		return end;
	}
	
	public Double getLength(){
		return length;
	}
	
	public Double getSpeed(){
		return speed;
	}
}
