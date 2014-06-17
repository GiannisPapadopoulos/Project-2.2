package graph;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Added to vertex and edge entities as user data for identification
 * 
 * @author Giannis Papadopoulos
 */
@AllArgsConstructor
@Getter
public class EntityIdentificationData {

	private EntityType type;
	private int ID;

	public enum EntityType {
		VERTEX,
		EDGE,
		CAR
	}


}
