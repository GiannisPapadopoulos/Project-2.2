package trafficsim.systems;

import java.util.List;

import lombok.val;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.GroupedTrafficLightComponent;
import trafficsim.components.GroupedTrafficLightComponent.GroupedTrafficLightData;
import trafficsim.components.LightToRoadMappingComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

/**
 * System that manages the traffic lights per intersection
 * 
 * @author Giannis Papadopoulos
 */
public class GroupedTrafficLightSystem
		extends EntitySystem {

	@Mapper
	private ComponentMapper<GroupedTrafficLightComponent> groupedTrafficLightMapper;
	@Mapper
	private ComponentMapper<TrafficLightComponent> trafficLightMapper;
	@Mapper
	private ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	private ComponentMapper<SpriteComponent> spriteMapper;

	@Mapper
	private ComponentMapper<LightToRoadMappingComponent> lightToRoadMapper;
	@Mapper
	private ComponentMapper<AttachedLightsComponent> attachedLightsMapper;


	@SuppressWarnings("unchecked")
	public GroupedTrafficLightSystem() {
		super(Aspect.getAspectForAll(GroupedTrafficLightComponent.class, PhysicsBodyComponent.class));
	}
	
	protected void initialize(GroupedTrafficLightComponent groupComp) {
		for (List<GroupedTrafficLightData> list : groupComp.getGroupedLightsData()) {
			for (GroupedTrafficLightData groupedData : list) {
				Entity trafficLight = world.getEntity(groupedData.getID());
				initialize(trafficLight);
			}
		}
		groupComp.setMapped(true);
	}

	protected void initialize(Entity trafficLight) {
		if (lightToRoadMapper.has(trafficLight)) {
			Entity road = world.getEntity(lightToRoadMapper.get(trafficLight).getRoadId());
			if (attachedLightsMapper.has(road)) {
				attachedLightsMapper.get(road).getTrafficLightIDs().add(trafficLight.getId());
				trafficLight.removeComponent(LightToRoadMappingComponent.class);
			}
		}
	}

	/** Sets the lights at the current index to green, rest to red */
	private void setGreen(GroupedTrafficLightComponent groupComp) {
		// Set first light to green
		for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(groupComp.getIndex())) {
			setLight(lightData, Status.GREEN);
		}
		// Set rest to red
		for (int i = 0; i < groupComp.getGroupedLightsData().size(); i++) {
			if (i != groupComp.getIndex()) {
				for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(i)) {
					setLight(lightData, Status.RED);
				}
			}
		}
		groupComp.setTimeElapsed(0);
	}

	/** Sets the lights at the current index to green, rest to red */
	private void toggle(GroupedTrafficLightComponent groupComp) {
		// Set first light to green
		if (groupComp.isGreen()) {
			for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(groupComp.getIndex())) {
				setLight(lightData, Status.ORANGE);
			}
			groupComp.setGreen(false);
		}
		else {
			groupComp.setIndex((groupComp.getIndex() + 1) % groupComp.getGroupedLightsData().size());
			// Set rest to red
			setGreen(groupComp);
			groupComp.setGreen(true);
		}
		groupComp.setTimeElapsed(0);
	}

	private void setLight(GroupedTrafficLightData lightData, Status status) {
		Entity trafficLight = world.getEntity(lightData.getID());
		TrafficLightComponent lightComp = trafficLightMapper.get(trafficLight);
		lightComp.setStatus(status);
		SpriteComponent spriteComp = spriteMapper.get(trafficLight);
		spriteComp.setName(lightComp.getTextureName());
		spriteComp.setSet(false);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity vertexEntity = entities.get(i);
			GroupedTrafficLightComponent groupComp = groupedTrafficLightMapper.get(vertexEntity);
			if (!groupComp.isMapped()) {
				initialize(groupComp);
			}
			if (!groupComp.isSet()) {
				setGreen(groupComp);
				groupComp.setSet(true);
			}
			groupComp.setTimeElapsed(groupComp.getTimeElapsed() + world.getDelta());
			if (groupComp.getTimeElapsed() > groupComp.getTimer()) {
				// increment index and toggle

				toggle(groupComp);
				
			}
		}
	}

	// tests whether the lights were grouped correctly if called
	@SuppressWarnings("unused")
	private void testGrouping(Entity vertexEntity, GroupedTrafficLightComponent groupComp) {
		if (groupComp.getGroupedLightsData().size() == 4) {
			Vector2 vertexPosition = physicsBodyMapper.get(vertexEntity).getPosition();
			for (val list : groupComp.getGroupedLightsData()) {
				for (int j = 0; j < list.size(); j++) {
					Entity light = world.getEntity(list.get(j).getID());
					assert trafficLightMapper.has(light);
					Vector2 position = physicsBodyMapper.get(light).getPosition();
					assert vertexPosition.dst(position) < 5;
				}
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
