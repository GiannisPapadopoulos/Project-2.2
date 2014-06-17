package utils;

import graph.Edge;
import graph.EntityIdentificationData;
import graph.EntityIdentificationData.EntityType;
import graph.Vertex;
import trafficsim.TrafficSimWorld;
import trafficsim.roads.NavigationObject;

import com.artemis.Entity;
import com.artemis.World;

public class EntityRetrievalUtils {

	public static TrafficSimWorld getWorld(World world) {
		return (TrafficSimWorld) world;
	}

	public static Entity getEntity(World world, EntityIdentificationData idData) {
		TrafficSimWorld tfWorld = getWorld(world);
		int entityID = -1;
		if (idData.getType() == EntityType.CAR) {
			entityID = idData.getID();
		}
		else if (idData.getType() == EntityType.VERTEX) {
			entityID = tfWorld.getVertexToEntityMap().get(idData.getID());
		}
		else if (idData.getType() == EntityType.EDGE) {
			entityID = tfWorld.getEdgeToEntityMap().get(idData.getID());
		}
		return world.getEntity(entityID);
	}

	public static Entity getVertexEntity(World world, Vertex<NavigationObject> vertex) {
		TrafficSimWorld tfWorld = getWorld(world);
		int vertexEntityId = tfWorld.getVertexToEntityMap().get(vertex.getID());
		return tfWorld.getEntity(vertexEntityId);
	}

	public static Entity getEdgeEntity(World world, Edge<NavigationObject> edge) {
		TrafficSimWorld tfWorld = getWorld(world);
		int edgeEntityId = tfWorld.getEdgeToEntityMap().get(edge.getID());
		return tfWorld.getEntity(edgeEntityId);
	}
}
