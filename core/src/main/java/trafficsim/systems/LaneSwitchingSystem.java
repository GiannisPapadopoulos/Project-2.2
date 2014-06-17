package trafficsim.systems;

import java.util.Random;

import trafficsim.TrafficSimConstants;
import trafficsim.components.LaneSwitchingComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.roads.CrossRoad;
import trafficsim.roads.CrossRoad.CR_TYPE;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

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

			if (routeComp.isFollowingEdge() && ((Road)(routeComp.getCurrentEdge().getData())).getNumLanes()>1) {

				if (lsComp.getCurrentEdge() == null
						|| lsComp.getCurrentEdge() != routeComp
								.getCurrentEdge()) {
					createLaneSwitchingComponent(routeComp, carBody);
				} else {

					getProperStrategy(VectorUtils.getLength(
							routeComp.getAllVertices().get(
									routeComp.getAllVertices().size() - 1),
							carBody.getPosition()),
							lsComp.getCurrentLaneIndex(),
							lsComp.getMinLaneIndex(), lsComp.getMaxLaneIndex(),
							lsComp.getExitLaneIndex());

				}
			}
		}
	}

	private void createLaneSwitchingComponent(RouteComponent routeComp,
			PhysicsBodyComponent carBody) {
		LaneSwitchingComponent result = new LaneSwitchingComponent();

		Edge<NavigationObject> current = routeComp.getCurrentEdge();
		Road road = (Road) current.getData();
		
		result.setCurrentEdge(current);
		result.setCurrentLaneIndex(MovementFunctions.determineCurrentLane(
				road.getAllSubSystems().get(0), carBody.getPosition()));
		result.setMinLaneIndex(0);
		result.setMaxLaneIndex(road.getAllSubSystems().get(0).getLanes()
				.size() - 1);
		
		if (routeComp.isLastEdge()) {			
			result.setExitLaneIndex(-1);
		} else {
			Edge<NavigationObject> next = routeComp.getNextEdge();
			CrossRoad nextC = (CrossRoad)(routeComp.getNextVertex().getData());
			if(nextC.getCrossRoadType()==CR_TYPE.HighWay_Cross) {
				
				double angleIN = VectorUtils.getAngle(nextC.getPosition(),current.getData().getPosition());
				double angleOUT = VectorUtils.getAngle(nextC.getPosition(), next.getData().getPosition());

				// TODO IS THIS RIGHT?
				if(Math.abs(Math.abs(angleIN-angleOUT)-270.0f)<SAME_DIR_DIF)
					result.setExitLaneIndex(result.getMinLaneIndex());
				else if(Math.abs(Math.abs(angleIN-angleOUT)-90.0f)<SAME_DIR_DIF)
					result.setExitLaneIndex(result.getMaxLaneIndex());
				else
					result.setExitLaneIndex(-1);
				
			} if(nextC.getCrossRoadType()==CR_TYPE.CrossRoad) {
				//TODO
			}
		}

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	private static final float MUST_SWITCH = TrafficSimConstants.CAR_LENGTH * 4;
	private static final float SHOULD_SWITCH = TrafficSimConstants.CAR_LENGTH * 10;

	private int getProperStrategy(float distance, int current, int min,
			int max, int exit) {

		if (distance < MUST_SWITCH) {
			return exit;
		} else if (distance < SHOULD_SWITCH) {
			if (current > exit)
				return current - 1;
			else if (current < exit)
				return current + 1;
			else
				return exit;
		} else {
			if (RANDOM.nextInt(1000) < 1) {
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
