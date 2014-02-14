package trafficsim.factories;

import trafficsim.TrafficSimWorld;
import trafficsim.components.Acceleration;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * Contains methods to create the different entities used in the world and add the required components
 * 
 * @author Giannis Papadopoulos
 */
public class EntityFactory {

	/**
	 * Creates a car with the given position and acceleration
	 * 
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createCar(TrafficSimWorld world, Vector2 position, float acceleration, String name) {
		Entity car = world.createEntity();
		PhysicsBodyComponent physComp = PhysicsBodyFactory.createCarBody(world.getBox2dWorld(), position);
		car.addComponent(physComp);

		SpriteComponent sprite = new SpriteComponent(name, 1, 1, 0);
		car.addComponent(sprite);

		car.addComponent(new Acceleration(acceleration));

		return car;
	}

}
