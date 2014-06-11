package trafficsim.roads;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;

import com.badlogic.gdx.math.Vector2;

public class CrossRoad extends NavigationObject {

	@Getter
	private float size;
	@Getter
	private ArrayList<Road> roadsIN;
	@Getter
	private ArrayList<Road> roadsOUT;
	@Getter
	private Vector2 position;
	@Getter
	private HashMap<RoadTransition, SubSystem> crSubSystems;

	public CrossRoad(float size, Vector2 position) {
		this.roadsIN = new ArrayList<Road>();
		this.roadsOUT = new ArrayList<Road>();
		this.size = size;
		this.position = position;
		this.crSubSystems = new HashMap<RoadTransition, SubSystem>();
	}

	public SubSystem requestTransitionPath(Road origin, Road destination) {
		for (RoadTransition rt : crSubSystems.keySet()) {
			if (rt.getOrigin() == origin && rt.getDestination() == destination)
				return crSubSystems.get(rt);
		}
		System.out.println("Something went wrong in Crossroad subsystems!");
		return null;
	}

	public void addConnection(Road r, boolean in) {
		if (in) {
			for (Road road : roadsOUT) {
				SubSystem ss = SubsystemFactory.createCrossRoadSubSystem(r,
						road, this);
				if (ss != null)
					crSubSystems.put(new RoadTransition(r, road), ss);
			}
			roadsIN.add(r);
		} else if (!in) {
			for (Road road : roadsIN) {
				SubSystem ss = SubsystemFactory.createCrossRoadSubSystem(road,
						r, this);
				if (ss != null)
					crSubSystems.put(new RoadTransition(road, r), ss);
			}
			roadsOUT.add(r);
		}
	}
}
