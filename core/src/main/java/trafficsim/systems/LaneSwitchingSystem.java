package trafficsim.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import paramatricCurves.ParametricCurve;
import paramatricCurves.curveDefs.C_Linear;
import trafficsim.TrafficSimConstants;
import trafficsim.components.LaneSwitchingComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.roads.CrossRoad;
import trafficsim.roads.CrossRoad.CR_TYPE;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;
import trafficsim.roads.SubSystem;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import editor.EditorData;
import functions.MovementFunctions;
import functions.VectorUtils;
import graph.Edge;

public class LaneSwitchingSystem extends EntitySystem {

	@Mapper
	ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	ComponentMapper<RouteComponent> routeComponentMapper;
	@Mapper
	ComponentMapper<LaneSwitchingComponent> laneSwitchingComponentMapper;

	private Random RANDOM = TrafficSimConstants.RANDOM;
	private float SAME_DIR_DIF = 10.0f;

	@SuppressWarnings("unchecked")
	public LaneSwitchingSystem() {
		super(Aspect.getAspectForAll(LaneSwitchingComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {

		for (int i = 0; i < entities.size(); i++) {

			Entity car = entities.get(i);
			RouteComponent routeComp = routeComponentMapper.get(car);
			LaneSwitchingComponent lsComp = laneSwitchingComponentMapper
					.get(car);
			PhysicsBodyComponent carBody = physicsBodyMapper.get(car);

			/*
			 * System.out.println("DEBUG: " + lsComp.getMinLaneIndex() + " : " +
			 * lsComp.getMaxLaneIndex() + " ; " + lsComp.getCurrentLaneIndex() +
			 * " ; " + lsComp.getExitLaneIndex());
			 */
			if (!routeComp.isSet())
				continue;
			if (lsComp.getSwitchPoint() != null)
				if (VectorUtils.getLength(carBody.getPosition(),
						lsComp.getSwitchPoint()) > TrafficSimConstants.LANE_WIDTH / 10)
					continue;
				else
					lsComp.setSwitchPoint(null);

			if (routeComp.isFollowingEdge()
					&& ((Road) (routeComp.getCurrentEdge().getData()))
							.getNumLanes() > 1) {

				if (lsComp.getCurrentEdge() == null
						|| lsComp.getCurrentEdge() != routeComp
								.getCurrentEdge()) {
					createLaneSwitchingComponent(routeComp, carBody, lsComp);
				}

				int properStrategy = getProperStrategy(VectorUtils.getLength(
						routeComp.getRoadEndPoint(), carBody.getPosition()),
						lsComp.getCurrentLaneIndex(), lsComp.getMinLaneIndex(),
						lsComp.getMaxLaneIndex(), lsComp.getExitLaneIndex());

				if (properStrategy != lsComp.getCurrentLaneIndex()) {
					Vector2 vStart = carBody.getPosition();
					Vector2 angle = null;
					if (properStrategy > lsComp.getCurrentLaneIndex())
						angle = VectorUtils.getUnitVectorDegrees(carBody
								.getLinearVelocity().angle()
								- TrafficSimConstants.LANE_SWITCH_ANGLE);
					else
						angle = VectorUtils.getUnitVectorDegrees(carBody
								.getLinearVelocity().angle()
								+ TrafficSimConstants.LANE_SWITCH_ANGLE);
					float dist = (float) (Math
							.abs(TrafficSimConstants.LANE_WIDTH
									* (lsComp.getCurrentLaneIndex() - properStrategy)) / Math
							.abs(Math.sin(TrafficSimConstants.LANE_SWITCH_ANGLE
									* Math.PI / 180.0f)));
					// System.out.println(dist+" ang: "+angle.angle()+" sin: "+Math.sin(angle.angle()*Math.PI/180.0f));
					Vector2 vEnd = VectorUtils.add2Vectors(vStart,
							VectorUtils.multiplyVector(angle, dist));

					List<Vector2> newSampling = new ArrayList<Vector2>();
					ParametricCurve laneSwitch = new ParametricCurve(
							new C_Linear(vStart, vEnd));
					int densitySample = 20;
					laneSwitch.addSamplePoints(newSampling, densitySample);

					lsComp.setCurrentLaneIndex(properStrategy);

					// MovementFunctions.determineCurrentLane(((Road)(routeComp.getCurrentEdge().getData())).getAllSubSystems().get(0),
					// carBody.getPosition());
					SubSystem ss = ((Road) routeComp.getCurrentEdge().getData())
							.getAllSubSystems().get(0);

					newSampling = ss
							.getLanes()
							.get(properStrategy)
							.get(0)
							.getTrajectory()
							.addSampleFrom(newSampling, densitySample * 3, vEnd);
					lsComp.setSwitchPoint(newSampling.get(densitySample + 10));
					// EditorData.debugPoints2.add(newSampling.get(densitySample
					// + 10));

					routeComp.setWayPointsLaneSwitch(newSampling);

				}

			}
		}

	}

	private void createLaneSwitchingComponent(RouteComponent routeComp,
			PhysicsBodyComponent carBody, LaneSwitchingComponent lsComp) {

		Edge<NavigationObject> current = routeComp.getCurrentEdge();
		Road road = (Road) current.getData();

		lsComp.setCurrentEdge(current);
		lsComp.setCurrentLaneIndex(MovementFunctions.determineCurrentLane(road
				.getAllSubSystems().get(0), carBody.getPosition()));
		lsComp.setMinLaneIndex(0);
		lsComp.setMaxLaneIndex(road.getAllSubSystems().get(0).getLanes().size() - 1);
		lsComp.setSwitchPoint(null);

		if (routeComp.isLastEdge()) {
			lsComp.setExitLaneIndex(-1);
		} else {
			Edge<NavigationObject> next = routeComp.getNextEdge();
			CrossRoad nextC = (CrossRoad) (routeComp.getNextVertex().getData());

			double angleIN = VectorUtils.getAngle(nextC.getPosition(), current
					.getData().getPosition());
			double angleOUT = VectorUtils.getAngle(nextC.getPosition(), next
					.getData().getPosition());

			if (nextC.getCrossRoadType() == CR_TYPE.HighWay_Cross) {

				float angleDiff = (float) (angleIN - angleOUT);
				if (angleDiff < 0)
					angleDiff += 360.0f;
				if (angleDiff > 360.0f)
					angleDiff -= 360.0f;

				if (Math.abs(angleDiff - 270.0f) < SAME_DIR_DIF) {
					lsComp.setExitLaneIndex(lsComp.getMaxLaneIndex());
				} else if (Math.abs(angleDiff - 90.0f) < SAME_DIR_DIF) {

					lsComp.setExitLaneIndex(lsComp.getMaxLaneIndex());
				} else
					lsComp.setExitLaneIndex(-1);
			}
			if (nextC.getCrossRoadType() == CR_TYPE.CrossRoad) {

				float angleDiff = (float) (angleIN - angleOUT);
				if (angleDiff < 0)
					angleDiff += 360.0f;
				if (angleDiff > 360.0f)
					angleDiff -= 360.0f;

				if (Math.abs(angleDiff - 270.0f) < SAME_DIR_DIF) {
					lsComp.setExitLaneIndex(lsComp.getMaxLaneIndex());
				} else if (Math.abs(angleDiff - 90.0f) < SAME_DIR_DIF) {
					lsComp.setExitLaneIndex(lsComp.getMinLaneIndex());
				} else
					lsComp.setExitLaneIndex(-1);
			}

		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	private static final float MUST_SWITCH = TrafficSimConstants.CAR_LENGTH * 6;
	private static final float SHOULD_SWITCH = TrafficSimConstants.CAR_LENGTH * 12;

	private int getProperStrategy(float distance, int current, int min,
			int max, int exit) {

		if (exit != -1) {
			if (distance < MUST_SWITCH) {
				return exit;
			} else if (distance < SHOULD_SWITCH) {
				if (current > exit) {
					return current - 1;
				} else if (current < exit) {
					return current + 1;
				} else {
					return exit;
				}
			} else {
				if (RANDOM.nextInt(1500) < 1) {
					int randInt012 = RANDOM.nextInt(3);
					if (randInt012 == 0)
						return current;
					else if (randInt012 == 1)
						if (current + 1 > max)
							return current;
						else
							return current + 1;
					else if (randInt012 == 2)
						if (current - 1 < min)
							return min;
						else
							return current - 1;
				} else
					return current;
			}
		} else {
			if (RANDOM.nextInt(1500) < 1) {
				int randInt012 = RANDOM.nextInt(3);
				if (randInt012 == 0)
					return current;
				else if (randInt012 == 1)
					if (current + 1 > max)
						return current;
					else
						return current + 1;
				else if (randInt012 == 2)
					if (current - 1 < min)
						return min;
					else
						return current - 1;
			} else
				return current;
		}
		System.out.println("Something with lane switching fucked up");
		return current;

	}
}
