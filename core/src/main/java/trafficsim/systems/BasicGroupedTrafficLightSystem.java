//package trafficsim.systems;
//
//import trafficsim.components.AttachedLightsComponent;
//import trafficsim.components.GroupedTrafficLightComponent;
//import trafficsim.components.GroupedTrafficLightComponent.GroupedTrafficLightData;
//import trafficsim.components.LightToRoadMappingComponent;
//import trafficsim.components.PhysicsBodyComponent;
//import trafficsim.components.SpriteComponent;
//import trafficsim.components.TrafficLightComponent;
//import trafficsim.components.TrafficLightComponent.Status;
//
//import com.artemis.ComponentMapper;
//import com.artemis.annotations.Mapper;
//
//public class BasicGroupedTrafficLightSystem
//		extends GroupedTrafficLightSystem {
//
//	@Mapper
//	private ComponentMapper<GroupedTrafficLightComponent> groupedTrafficLightMapper;
//	@Mapper
//	private ComponentMapper<TrafficLightComponent> trafficLightMapper;
//	@Mapper
//	private ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
//	@Mapper
//	private ComponentMapper<SpriteComponent> spriteMapper;
//
//	@Mapper
//	private ComponentMapper<LightToRoadMappingComponent> lightToRoadMapper;
//	@Mapper
//	private ComponentMapper<AttachedLightsComponent> attachedLightsMapper;
//
//	@SuppressWarnings("unchecked")
//	public BasicGroupedTrafficLightSystem() {
//	}
//
//	/** Sets the lights at the current index to green, rest to red */
//	@Override
//	public void toggle(GroupedTrafficLightComponent groupComp) {
//		// Set first light to green
//		if (groupComp.isGreen()) {
//			for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(groupComp.getIndex())) {
//				setLight(lightData, Status.ORANGE);
//			}
//			groupComp.setGreen(false);
//		}
//		else {
//			groupComp.setIndex((groupComp.getIndex() + 1) % groupComp.getGroupedLightsData().size());
//			// Set rest to red
//			setGreen(groupComp);
//			groupComp.setGreen(true);
//		}
//		groupComp.setTimeElapsed(0);
//	}
//
// }
