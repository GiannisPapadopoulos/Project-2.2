package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
	
	public Collection<RoadEndpoint> getEndpoints(){
		return (Collection<RoadEndpoint>)endpoints.clone(); //I'm cloning because we don't want outsiders modifying our roads list
	}
	
	public Collection<Road> getRoads(){
		return (Collection<Road>) roads.clone(); 
	}
	
	public int getNumEndpoints(){
		return endpoints.size();
	}
	
	public int getNumRoads(){
		return roads.size();
	}
}
