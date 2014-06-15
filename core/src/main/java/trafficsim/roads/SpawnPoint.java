package trafficsim.roads;

import graph.Vertex;

import com.badlogic.gdx.math.Vector2;

public class SpawnPoint extends CrossRoad {

	public SpawnPoint( float size, Vector2 position) {
		super( size, position,CrossRoad.CR_TYPE.SpawnPoint);
	}
	
	public enum Type {
		Garage, Highway
	}

}
