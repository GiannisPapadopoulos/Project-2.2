package trafficsim.roads;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.ToString;
import paramatricCurves.ParametricCurve;

import com.badlogic.gdx.math.Vector2;

import functions.VectorUtils;

/**
 * Class that holds data related to a road
 * 
 * @author Giannis Papadopoulos
 */
@Getter
@ToString
public class Road extends NavigationObject {

	private HashMap<CrossRoadTransition, SubSystem> rSubSystems;

	private SubSystem subSystem;

	/** The left point */
	private Vector2 pointA;
	/** The right point */
	private Vector2 pointB;

	/** Number of lanes on this road */
	private int numLanes;

	private float speedLimit;

	public Road(ParametricCurve roadDef, int numLanes, float speedLimit,
			CrossRoad origin, CrossRoad destination) {
		ArrayList<ParametricCurve> roadDefAL = new ArrayList<ParametricCurve>();
		roadDefAL.add(roadDef);
		this.numLanes = numLanes;
		create(roadDefAL, numLanes, speedLimit, origin, destination);
		if (origin != null)
			origin.addConnection(this, false);
		if (destination != null)
			destination.addConnection(this, true);
		
	}

	// // UNUSED
	// public Road(ArrayList<ParametricCurve> roadDef, int numLanes,
	// float speedLimit, CrossRoad origin, CrossRoad destination) {
	// this.numLanes = numLanes;
	// create(roadDef, numLanes, speedLimit, origin, destination);
	// origin.addConnection(this, false);
	// destination.addConnection(this, true);
	//
	// }

	@Override
	public SubSystem requestTransitionPath(NavigationObject origin,
			NavigationObject destination) {
		assert rSubSystems.keySet().size() > 0;
		for (CrossRoadTransition rt : rSubSystems.keySet()) {
			if (rt.getOrigin() == origin && rt.getDestination() == destination)
				return rSubSystems.get(rt);
		}
		System.out.println("Something went wrong in Crossroad subsystems!");
		return null;
	}

	private void create(ArrayList<ParametricCurve> roadDef, int numLanes,
			float speedLimit, CrossRoad origin, CrossRoad destination) {
		this.pointA = roadDef.get(0).getPoint(roadDef.get(0).getR_t().getLow());
		this.pointB = roadDef.get(roadDef.size() - 1).getPoint(
				roadDef.get(roadDef.size() - 1).getR_t().getHigh());
		this.speedLimit = speedLimit;

		CrossRoadTransition crt = new CrossRoadTransition(origin, destination);
		rSubSystems = new HashMap<CrossRoadTransition, SubSystem>();
		rSubSystems.put(crt, new SubSystem());

		for (int i = 0; i < numLanes; i++)
			rSubSystems.get(crt).addSubsystem(
					SubsystemFactory.createRoadSubsystem(roadDef, i));

		subSystem = rSubSystems.get(crt);
	}

	@Override
	public Vector2 getPosition() {
		return VectorUtils.getMidPoint(pointA, pointB).cpy();
	}

	/*
	 * public Rectangle2D getRectangle(){
	 * 
	 * new Rectangle2D.Float(pointA.x-LANE_WIDTH*Math.cos(getAngle(this)),
	 * pointA.y-LANE_WIDTH*Math.sin(getAngle(this)), LANE_WIDTH*numLanes,) }
	 */

	// UNUSED ////////
	public Vector2 getPointC() {
		Vector2 v = VectorUtils.getMidPoint(pointA, pointB);

		float angle = VectorUtils.getAngle(pointA, pointB);
		angle += 90.0;

		Vector2 vAdd = VectorUtils.getUnitVectorDegrees(angle);
		vAdd.x *= VectorUtils.getLength(pointA, pointB) / 2;
		vAdd.y *= VectorUtils.getLength(pointA, pointB) / 2;

		v.add(vAdd);

		return v;

	}

	public Vector2 getPointD() {
		Vector2 v = VectorUtils.getMidPoint(pointA, pointB);

		float angle = VectorUtils.getAngle(pointA, pointB);
		angle -= 90.0;

		Vector2 vAdd = VectorUtils.getUnitVectorDegrees(angle);
		vAdd.x *= VectorUtils.getLength(pointA, pointB) / 2;
		vAdd.y *= VectorUtils.getLength(pointA, pointB) / 2;

		v.add(vAdd);

		return v;
	}

	@Override
	public float getLength() {
		return VectorUtils.getLength(this);
	}

}
