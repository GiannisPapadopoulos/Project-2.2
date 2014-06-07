package trafficsim.roads;

import graph.Vertex;

import java.util.HashMap;

import lombok.Getter;

import com.badlogic.gdx.math.Vector2;


public class CrossRoad extends NavigationObject {

	@Getter
	private float size;
	@Getter
	private Vertex vertex;
	@Getter
	private Vector2 position;
	
	private HashMap<RoadTransition,CrossRoadSubSystem> crSubSystems;

	public CrossRoad(Vertex v, float size, Vector2 position) {
		this.vertex = v;
		this.size = size;
		this.position = position;
	}
	
	public CrossRoadSubSystem requestTransitionPath(Road origin, Road destination) {
		for(RoadTransition rt : crSubSystems.keySet()) {
			if(rt.getOrigin() == origin && rt.getDestination() == destination)
				return crSubSystems.get(rt);
		}
		System.out.println("Something went wrong in Crossroad subsystems!");
		return null;
	}
}
