package trafficsim.factories;

import static trafficsim.TrafficSimConstants.WORLD_TO_BOX;
import trafficsim.TrafficSimWorld;
import trafficsim.components.Acceleration;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

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
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(3f, 2f)
																.density(1.0f)
																.restitution(1.0f)
																.friction(0f);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.DynamicBody)
															.position(position.scl(WORLD_TO_BOX))
															.build();
		car.addComponent(new PhysicsBodyComponent(body));

		SpriteComponent sprite = new SpriteComponent(name, 1, 1, 0);
		car.addComponent(sprite);

		car.addComponent(new Acceleration(acceleration));

		return car;
	}

}
