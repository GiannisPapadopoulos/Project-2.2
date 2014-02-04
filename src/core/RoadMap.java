package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RoadMap {
	
	private ArrayList<Road> roads = new ArrayList<Road>();
	private HashSet<RoadEndpoint> endpoints = new HashSet<RoadEndpoint>();
	
	public RoadMap(Road[] initialRoads){
		for (Road r: initialRoads){
			addRoad(r);
		}
	}
	public RoadMap(){
		this(new Road[0]);
	}
	public void addRoad(Road road){
		//The fact that these are HashSets makes sure that roads aren't added more than once.
		endpoints.add(road.getStart());
		endpoints.add(road.getEnd());
		roads.add(road);
	}
	
	public List<RoadEndpoint> getEndpoints() {
		return new ArrayList<RoadEndpoint>(endpoints); // I'm cloning because we don't want outsiders modifying our roads list
														// If this method is called frequently, we won't want to clone
														// every time - Giannis
	}
	
	public List<Road> getRoads() {
		return new ArrayList<Road>(roads);
	}
	
	public int getNumEndpoints(){
		return endpoints.size();
	}
	
	public int getNumRoads(){
		return roads.size();
	}
}
