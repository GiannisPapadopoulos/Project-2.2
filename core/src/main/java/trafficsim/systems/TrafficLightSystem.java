package trafficsim.systems;

import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.LightToRoadMappingComponent;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class TrafficLightSystem extends EntitySystem{
	
	@Mapper
	ComponentMapper<TrafficLightComponent> trafficLightMapper;

	@Mapper
	ComponentMapper<LightToRoadMappingComponent> lightToRoadMapper;

	@Mapper
	ComponentMapper<AttachedLightsComponent> attachedLightsMapper;

	@SuppressWarnings("unchecked")
	public TrafficLightSystem() {
		super(Aspect.getAspectForAll(TrafficLightComponent.class));
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for(int i = 0;i<entities.size();i++){
			Entity trafficLight = entities.get(i);
			TrafficLightComponent lightComp = trafficLightMapper.get(trafficLight);
			lightComp.setTimeElapsed(lightComp.getTimeElapsed() + world.getDelta());
			if(lightComp.getTimeElapsed()>=lightComp.timer()){
				lightComp.setTimeElapsed(0);
				int index = (lightComp.getStatus().ordinal() + 1) % Status.values().length;
				lightComp.setStatus(Status.values()[index]);
			}

			if (lightToRoadMapper.has(trafficLight)) {
				Entity road = world.getEntity(lightToRoadMapper.get(trafficLight).getRoadId());
				if (attachedLightsMapper.has(road)) {
					attachedLightsMapper.get(road).getTrafficLightIDs().add(trafficLight.getId());
					trafficLight.removeComponent(LightToRoadMappingComponent.class);
				}
			}
		}
	}
}
