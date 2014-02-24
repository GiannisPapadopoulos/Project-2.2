package trafficsim.factories;

import static trafficsim.TrafficSimConstants.BOX_TO_WORLD;
import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.CAR_WIDTH;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import trafficsim.TrafficSimWorld;
import trafficsim.components.AccelerationComponent;
import trafficsim.components.DimensionComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.PositionComponent;
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
		// boxShape takes the half width/height as input
		car.addComponent(new DimensionComponent(CAR_LENGTH, CAR_WIDTH));
		/** @formatter:off */
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(CAR_LENGTH * BOX_TO_WORLD / 2, CAR_WIDTH * BOX_TO_WORLD / 2)
																.density(1.0f)
																.restitution(1.0f)
																.friction(0f);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.DynamicBody)
															.position(position)
															.build();
		/** @formatter:on */
		car.addComponent(new PhysicsBodyComponent(body));

		// SpriteComponent sprite = new SpriteComponent(name, 1, 1, 0);
		SpriteComponent sprite = new SpriteComponent(name);
		car.addComponent(sprite);

		car.addComponent(new AccelerationComponent(acceleration));
		car.addComponent(new MaxSpeedComponent(60));

		return car;
	}

	/**
	 * Creates a road with the given parameters
	 * 
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createRoad(TrafficSimWorld world, Vector2 position, float length, boolean vertical, String name) {
		Entity road = world.createEntity();
		if (!vertical)
			road.addComponent(new DimensionComponent(length, LANE_WIDTH * 2));
		else
			road.addComponent(new DimensionComponent(LANE_WIDTH * 2, length));
		// boxShape takes the half width/height as input
		// FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(length * BOX_TO_WORLD / 2, LANE_WIDTH * 2 *
		// BOX_TO_WORLD / 2)
		// .density(1.0f)
		// .restitution(1.0f)
		// .friction(0f)
		// .groupIndex((short) -1);
		// Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
		// .type(BodyType.StaticBody)
		// .position(position.scl(WORLD_TO_BOX))
		// .build();
		// road.addComponent(new PhysicsBodyComponent(body));
		road.addComponent(new PositionComponent(position));

		// / SpriteComponent sprite = new SpriteComponent(name, 1, 1, 0);
		SpriteComponent sprite = new SpriteComponent(name);
		road.addComponent(sprite);


		return road;
	}

}
