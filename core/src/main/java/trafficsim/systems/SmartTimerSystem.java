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
import trafficsim.components.VehiclesOnRoadComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

public class SmartTimerSystem extends EntitySystem {
	
	@Mapper
	private ComponentMapper<GroupedTrafficLightComponent> groupedTrafficLightMapper;
	@Mapper
	private ComponentMapper<VehiclesOnRoadComponent> vehiclesOnRoadComponent;
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

	public SmartTimerSystem() {
		super(Aspect.getAspectForAll(GroupedTrafficLightComponent.class));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean checkProcessing() {
		// TODO Auto-generated method stub
		return true;
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

		private void setLight(GroupedTrafficLightData lightData, Status status) {
			Entity trafficLight = world.getEntity(lightData.getID());
			TrafficLightComponent lightComp = trafficLightMapper.get(trafficLight);
			lightComp.setStatus(status);
			SpriteComponent spriteComp = spriteMapper.get(trafficLight);
			spriteComp.setName(lightComp.getTextureName());
			spriteComp.setSet(false);
		}
		
	
	private void toggle(GroupedTrafficLightComponent groupComp) {
		// Set first light to green
		if (groupComp.isGreen()) {
			for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(groupComp.getIndex())) {
				setLight(lightData, Status.ORANGE);
			}
			groupComp.setGreen(false);
		}
		else {
			//find the road with the most cars on it
			int vertexID = groupComp.getVertexID();
			
		
			// Set rest to red
			setGreen(groupComp);
			groupComp.setGreen(true);
		}
		groupComp.setTimeElapsed(0);
	}
	
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
	
	protected void initialize(Entity trafficLight) {
		if (lightToRoadMapper.has(trafficLight)) {
			Entity road = world.getEntity(lightToRoadMapper.get(trafficLight).getRoadId());
			if (attachedLightsMapper.has(road)) {
				attachedLightsMapper.get(road).getTrafficLightIDs().add(trafficLight.getId());
				trafficLight.removeComponent(LightToRoadMappingComponent.class);
			}
		}
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

}
