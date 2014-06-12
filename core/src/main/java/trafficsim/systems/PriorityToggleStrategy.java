package trafficsim.systems;

import graph.Vertex;

import java.util.List;

import trafficsim.TrafficSimWorld;
import trafficsim.components.GroupedTrafficLightComponent;
import trafficsim.components.GroupedTrafficLightComponent.GroupedTrafficLightData;
import trafficsim.components.TrafficLightComponent.Status;
import trafficsim.components.VehiclesOnRoadComponent;
import trafficsim.roads.NavigationObject;

/** This strategy sets the next green traffic light on the road with most cars waiting */
public class PriorityToggleStrategy
		extends AbstractToggleStrategy {

	@Override
	public void toggle(GroupedTrafficLightSystem lightSystem, GroupedTrafficLightComponent groupComp) {
		// Set first light to green
		if (groupComp.isGreen()) {
			for (GroupedTrafficLightData lightData : groupComp.getGroupedLightsData().get(groupComp.getIndex())) {
				lightSystem.setLight(lightData, Status.ORANGE);
			}
			groupComp.setGreen(false);
		}
		else {
			// find the road with the most cars on it
			TrafficSimWorld simWorld = lightSystem.getWorld();
			int busiestRoadCarCount = 0;
			int busiestIndex = 0;
			int index = 0;

			for (List<GroupedTrafficLightData> lighListData : groupComp.getGroupedLightsData()) {
				int edgeID = lighListData.get(0).getEdgeID();
				int edgeEntityID = simWorld.getEdgeToEntityMap().get(edgeID);
				VehiclesOnRoadComponent vehiclesComp = lightSystem.getVehiclesOnRoadMapper()
																	.get(simWorld.getEntity(edgeEntityID));
				int amountOfCars = vehiclesComp.getVehiclesOnLaneIDs().size();

				int vertexID = groupComp.getVertexID();
				Vertex<NavigationObject> vertex = simWorld.getGraph().getVertex(vertexID);
				// System.out.println("strategy: edge " + edgeID + " id " + edgeEntityID + " vehicles " + amountOfCars);

				if (amountOfCars > busiestRoadCarCount) {
					busiestIndex = index;
					busiestRoadCarCount = amountOfCars;
				}
				index++;
			}

			// Set to the best index found
			groupComp.setIndex(busiestIndex);
			lightSystem.setGreen(groupComp);
			groupComp.setGreen(true);
		}
		groupComp.setTimeElapsed(0);

	}

}
