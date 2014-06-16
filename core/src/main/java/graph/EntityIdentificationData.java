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

	private ElementType type;
	private int ID;

	public enum ElementType {
		VERTEX,
		EDGE,
		CAR
	}


}
