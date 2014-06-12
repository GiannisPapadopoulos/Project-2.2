package trafficsim.systems;

import graph.Vertex;

import java.util.List;

import lombok.val;
import trafficsim.TrafficSimWorld;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.GroupedTrafficLightComponent;
import trafficsim.components.GroupedTrafficLightComponent.GroupedTrafficLightData;
import trafficsim.components.LightToRoadMappingComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;
import trafficsim.components.VehiclesOnRoadComponent;
import trafficsim.roads.NavigationObject;

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

	@SuppressWarnings("unchecked")
	public SmartTimerSystem() {
		super(Aspect.getAspectForAll(GroupedTrafficLightComponent.class));
	}

	@Override
	protected boolean checkProcessing() {
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

				toggleDirection(groupComp);
				
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
		
	
	private void toggleDirection(GroupedTrafficLightComponent groupComp) {
		// Set first light to green
		if (groupComp.isGreen()) {
			for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(groupComp.getIndex())) {
				setLight(lightData, Status.ORANGE);
			}
			groupComp.setGreen(false);
		}
		else {
			// find the road with the most cars on it
			int vertexID = groupComp.getVertexID();
			TrafficSimWorld simWorld = (TrafficSimWorld) world;
			Vertex<NavigationObject> vertex = simWorld.getGraph().getVertex(vertexID);
			int busiestRoadCarCount = 0;
			int busiestIndex = 0;
			int index = 0;
			// for (Edge<Road> edge : vertex.getIncomingEdgeIterator()) {
			// int edgeID = simWorld.getEdgeToEntityMap().get(edge.getID());
			// VehiclesOnRoadComponent vehiclesComp = vehiclesOnRoadComponent.get(simWorld.getEntity(edgeID));
			// int amountOfCars = vehiclesComp.getVehiclesOnLaneIDs().size();
			// if (amountOfCars > busiestRoadCarCount) {
			// busiestEdge = i;
			// busiestRoadCarCount = amountOfCars;
			// }
			// i++;
			// }

			for (List<GroupedTrafficLightData> lighListData : groupComp.getGroupedLightsData()) {
				int edgeID = lighListData.get(0).getEdgeID();
				int edgeEntityID = simWorld.getEdgeToEntityMap().get(edgeID);
				VehiclesOnRoadComponent vehiclesComp = vehiclesOnRoadComponent.get(simWorld.getEntity(edgeEntityID));
				int amountOfCars = vehiclesComp.getVehiclesOnLaneIDs().size();
				if (amountOfCars > busiestRoadCarCount) {
					busiestIndex = index;
					busiestRoadCarCount = amountOfCars;
				}
				index++;
			}

			groupComp.setIndex(busiestIndex);

			// Set rest to red
			setGreen(groupComp);
			groupComp.setGreen(true);
		}
		groupComp.setTimeElapsed(0);
	}
	
	private void toggleTime(GroupedTrafficLightComponent groupComp) {
		
		// The green timer will speed up by this amount if the road with the currently green light is empty
		float timeToAdd = 2f;
		
		// Set first light to green
		if (groupComp.isGreen()) {
			for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(groupComp.getIndex())) {
				setLight(lightData, Status.ORANGE);
			}
			groupComp.setGreen(false);
		}
		else {
			// find the road with the most cars on it
			int vertexID = groupComp.getVertexID();
			TrafficSimWorld simWorld = (TrafficSimWorld) world;
			Vertex<NavigationObject> vertex = simWorld.getGraph().getVertex(vertexID);
			// TODO get the current green edge
			// Edge <Road> edge = vertex.
			int edgeID = groupComp.getGroupedLightsData().get(groupComp.getIndex()).get(0).getEdgeID();
			int edgeEntityID = simWorld.getEdgeToEntityMap().get(edgeID);
			VehiclesOnRoadComponent vehiclesComp = vehiclesOnRoadComponent.get(simWorld.getEntity(edgeEntityID));
			int amountOfCars = vehiclesComp.getVehiclesOnLaneIDs().size();
			if (amountOfCars < 1) {
				// add 3 seconds to the current lights timer.
				groupComp.setTimeElapsed(groupComp.getTimeElapsed() + timeToAdd);
			}
			groupComp.setTimeElapsed(0);
		}
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
