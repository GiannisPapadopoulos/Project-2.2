package trafficsim.factories;

import static functions.VectorUtils.getLength;
import graph.Edge;
import graph.Vertex;

import java.util.List;

import trafficsim.TrafficSimWorld;
import trafficsim.components.GroupedTrafficLightComponent;
import trafficsim.roads.NavigationObject;

import com.artemis.Entity;

public class GreenMile {

	public static void createGreenMile(TrafficSimWorld world, List< Vertex<NavigationObject>> vertexList , int lengthOfMile) {

		// The angle of the roads that will be part of the greenWave
		//float angle = VectorUtils.getAngle(currentIntersection.getData(), nextIntersection.getData());

		
		Vertex<NavigationObject> currentIntersection= vertexList.get(0);
		Vertex<NavigationObject> nextIntersection = vertexList.get(1);
		Entity vertexEntity;
		GroupedTrafficLightComponent groupedLightComp;
		float totalGreenWaveTime = 0;

		setLight(world, currentIntersection, currentIntersection.getNeighbor(nextIntersection), 0);

		for (int i = 2; i < vertexList.size() - 1; i++) {

			vertexEntity = world.getEntity(world.getVertexToEntityMap().get(nextIntersection.getID()));
			groupedLightComp = vertexEntity.getComponent(GroupedTrafficLightComponent.class);
			Edge<NavigationObject> connectingEdge = currentIntersection.getNeighbor(nextIntersection);
			float totalDistance = getLength(connectingEdge.getData());
			float timeToTravel = totalDistance / connectingEdge.getData().getSpeedLimit();
			totalGreenWaveTime = totalGreenWaveTime + timeToTravel;
			//angle = VectorUtils.getAngle(currentIntersection.getData(), nextIntersection.getData());
			int correctIndex = findCorrectLight(currentIntersection, currentIntersection.getNeighbor(nextIntersection));
			vertexEntity = world.getEntity(world.getVertexToEntityMap().get(nextIntersection.getID()));
			groupedLightComp = vertexEntity.getComponent(GroupedTrafficLightComponent.class);
			// float redTimer = groupedLightComp.getGroupedLightsData().get(correctIndex).get(0).getRedTimer();
			float redTimer = groupedLightComp.getRedTimer(correctIndex);
			if (redTimer < totalGreenWaveTime) {
				setLight(	world, nextIntersection, currentIntersection.getNeighbor(nextIntersection),
							redTimer - totalGreenWaveTime);
			}
			else {
				break;
			}

			// TODO update groupedLightComp timers here

			currentIntersection = nextIntersection;
			nextIntersection = vertexList.get(i);

		}
	}

	private static void setLight(TrafficSimWorld world, Vertex<NavigationObject> intersection, Edge<NavigationObject> edge, float timeElapsed) {
		int currectIndex = findCorrectLight(intersection, edge);
		Entity vertexEntity = world.getEntity(world.getVertexToEntityMap().get(intersection.getID()));
		GroupedTrafficLightComponent groupedLightComp = vertexEntity.getComponent(GroupedTrafficLightComponent.class);
		groupedLightComp.setIndex(currectIndex);
		groupedLightComp.setGreen(true);
		groupedLightComp.setTimeElapsed(timeElapsed);
		// float lightTime = (float)
		// groupedLightComp.getGroupedLightsData().get(currectIndex).get(0).getGreenTimer();
		// return lightTime;
	}

	private static int findCorrectLight(Vertex<NavigationObject> intersection, Edge<NavigationObject> edge) {
		return 0;
	}

	// This is not really correct, won't work in every situation
	/*
	private static Vertex<NavigationObject> findNextIntersection(Vertex<NavigationObject> nextIntersection, float angle) {
		edge<NavigationObject> nextEdge = null;
		float closestAngleDiff = 2 * PI;
		for (edge<NavigationObject> edge : nextIntersection.getParent().getEdgeIterator()) {
			float angleDiff = Math.abs(getAngle(edge.getData()) - angle);
			if (angleDiff < closestAngleDiff) {
				closestAngleDiff = angleDiff;
				nextEdge = edge;
			}
		}
		return nextEdge == null ? null : nextIntersection.getNeighbor(nextEdge);
	}*/
}