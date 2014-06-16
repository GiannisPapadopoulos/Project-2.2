package utils;

import graph.EntityIdentificationData;
import graph.EntityIdentificationData.ElementType;
import trafficsim.TrafficSimWorld;

import com.artemis.Entity;
import com.artemis.World;

public class EntityRetrievalUtils {

	public static TrafficSimWorld getWorld(World world) {
		return (TrafficSimWorld) world;
	}

	public static Entity getEntity(World world, EntityIdentificationData idData) {
		TrafficSimWorld tfWorld = getWorld(world);
		int entityID = -1;
		if (idData.getType() == ElementType.CAR) {
			entityID = idData.getID();
		}
		else if (idData.getType() == ElementType.VERTEX) {
			entityID = tfWorld.getVertexToEntityMap().get(idData.getID());
		}
		else if (idData.getType() == ElementType.EDGE) {
			entityID = tfWorld.getEdgeToEntityMap().get(idData.getID());
		}
		return world.getEntity(entityID);
	}
}
