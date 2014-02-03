package core;

public class RoadEndpoint {
	private double x;
	private double y;
	private String label;
	public RoadEndpoint(double posx, double posy, String pointLabel){
		x  = posx;
		y = posy;
		label = pointLabel;
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public String getLabel(){
		return  label;
	}
	public double getDistance(RoadEndpoint other){
		double xdist = Math.abs(x - other.getX());
		double ydist = Math.abs(y - other.getY());
		return xdist + ydist;
	}
}
