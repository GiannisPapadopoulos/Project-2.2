package trafficsim.roads;

import java.util.ArrayList;
import java.util.HashMap;

import paramatricCurves.curveDefs.C_Circular;
import lombok.Getter;

import com.badlogic.gdx.math.Vector2;

public class CrossRoad extends NavigationObject {

	@Getter
	private float size;
	@Getter
	private ArrayList<Road> roadsIN;
	@Getter
	private ArrayList<Road> roadsOUT;

	private Vector2 position;
	@Getter
	private HashMap<RoadTransition, SubSystem> crSubSystems;

	@Getter
	private CR_TYPE crossRoadType;

	public CrossRoad(float size, Vector2 position, CR_TYPE crt) {
		this.roadsIN = new ArrayList<Road>();
		this.roadsOUT = new ArrayList<Road>();
		this.size = size;
		this.position = position;
		this.crSubSystems = new HashMap<RoadTransition, SubSystem>();
		this.crossRoadType = crt;
	}

	public SubSystem requestTransitionPath(NavigationObject origin,
			NavigationObject destination) {
		for (RoadTransition rt : crSubSystems.keySet()) {
			if (rt.getOrigin() == origin && rt.getDestination() == destination)
				return crSubSystems.get(rt);
		}
		System.out.println("Something went wrong in Crossroad subsystems!");
		return null;
	}

	public void addConnection(Road r, boolean in) {
		if (crossRoadType == CR_TYPE.CrossRoad) {
			if (in) {
				for (Road road : roadsOUT) {
					SubSystem ss = SubsystemFactory.createCrossRoadSubSystem(r,
							road, this);
					if (ss != null)
						crSubSystems.put(new RoadTransition(r, road), ss);
				}
			} else if (!in) {
				for (Road road : roadsIN) {
					SubSystem ss = SubsystemFactory.createCrossRoadSubSystem(
							road, r, this);
					if (ss != null)
						crSubSystems.put(new RoadTransition(road, r), ss);
				}
			}
		} else if (crossRoadType == CR_TYPE.Roundabout) {
			if (in) {
				for (Road road : roadsOUT) {
					SubSystem ss = SubsystemFactory.createRoundAboutSubSystem(
							r, road, this);
					if (ss != null)
						crSubSystems.put(new RoadTransition(r, road), ss);
				}
			} else if (!in) {
				for (Road road : roadsIN) {
					SubSystem ss = SubsystemFactory.createRoundAboutSubSystem(
							road, r, this);
					if (ss != null)
						crSubSystems.put(new RoadTransition(road, r), ss);
				}
			}
		} else if (crossRoadType == CR_TYPE.HighWay_Cross) {
			if (in) {

				for (Road road : roadsOUT) {
					SubSystem ss = SubsystemFactory.createHighWayCrossSS_TYPE1(
							r, road, this);
					if (ss != null)
						crSubSystems.put(new RoadTransition(r, road), ss);
				}

			} else if (!in) {
				for (Road road : roadsIN) {
					SubSystem ss = SubsystemFactory.createHighWayCrossSS_TYPE1(
							road, r, this);
					if (ss != null)
						crSubSystems.put(new RoadTransition(road, r), ss);
				}
			}
		}
		if (in)
			roadsIN.add(r);
		else if (!in)
			roadsOUT.add(r);
	}

	public Vector2 getPosition() {
		return position.cpy();
	}

	@Override
	public float getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getSpeedLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	public enum CR_TYPE {
		CrossRoad, Roundabout, SpawnPoint, HighWay_Cross, HighWay_T;
	}
}
