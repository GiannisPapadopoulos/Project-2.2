package trafficsim;

import lombok.Getter;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

/**
 * The artemis world. Keeps a reference to the box2d world used for the physics
 * 
 * @author Giannis Papadopoulos
 * 
 */
public class TrafficSimWorld
		extends World {

	/** Box 2D World Used For Internal Physics Representation And Calculation */
	@Getter
	private com.badlogic.gdx.physics.box2d.World box2dWorld;

	public TrafficSimWorld() {
		super();
		box2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
	}

}
