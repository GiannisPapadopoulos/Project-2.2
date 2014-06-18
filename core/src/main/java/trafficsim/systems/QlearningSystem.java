package trafficsim.systems;

import static trafficsim.TrafficSimConstants.RANDOM;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import trafficsim.components.GroupedTrafficLightComponent;
import trafficsim.components.GroupedTrafficLightComponent.GroupedTrafficLightData;
import trafficsim.components.IntersectionThroughputComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class QlearningSystem
		extends EntitySystem {

	@Mapper
	private ComponentMapper<GroupedTrafficLightComponent> groupedTrafficLightMapper;
	@Mapper
	private ComponentMapper<IntersectionThroughputComponent> throughputMapper;

	private List<Action> actions = new ArrayList<>();
	private List<State> states = new ArrayList<>();

	private State state;

	private double[] values;

	float[] timers = { 4, 8 };

	float accumTime;

	float evaluationTimer = 10;

	double alpha = 0.5;

	@SuppressWarnings("unchecked")
	public QlearningSystem() {
		super(Aspect.getAspectForAll(GroupedTrafficLightComponent.class));
		buildActions();
	}

	private State getState() {
		return null;
	}

	private int index(float greenTimer) {
		for (int i = 0; i < timers.length; i++) {
			if (Math.abs(greenTimer - timers[i]) < 0.001) {
				return i;
			}
		}
		return -1;
	}

	private void buildActions() {

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < timers.length; j++) {
				actions.add(new Action(i, timers[j]));
				states.add(new State(i, timers[j]));
			}
		}
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		accumTime += world.getDelta();
		for (int i = 0; i < entities.size(); i++) {
			Entity vertexEntity = entities.get(i);
			GroupedTrafficLightComponent groupComp = groupedTrafficLightMapper.get(vertexEntity);
			IntersectionThroughputComponent throughComp = throughputMapper.get(vertexEntity);
			if (accumTime > evaluationTimer) {
				throughComp.reset();
				int randIndex = RANDOM.nextInt(actions.size());
				accumTime = 0;
				actions.get(randIndex).perform(groupComp);
			}
		}
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@AllArgsConstructor
	class State {

		private int ID;
		private float greenTimer;

	}

	@AllArgsConstructor
	class Action {

		private int ID;
		private float greenTimer;

		public void perform(GroupedTrafficLightComponent groupComp) {
			List<GroupedTrafficLightData> list = groupComp.getGroupedLightsData().get(ID);
			for (GroupedTrafficLightData groupData : list) {
				groupData.setGreenTimer(greenTimer);
			}
		}
	}

}
