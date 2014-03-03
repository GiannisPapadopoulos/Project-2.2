package trafficsim.factories;

import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.CAR_WIDTH;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import graph.Edge;
import graph.Graph;
import trafficsim.TrafficSimWorld;
import trafficsim.components.AccelerationComponent;
import trafficsim.components.DimensionComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;
import trafficsim.roads.Road;

import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
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
	 * @param position The position of the center of mass
	 * @param angle The angle in ?
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createCar(TrafficSimWorld world, Vector2 position, float acceleration, float angle, String name) {
		Entity car = world.createEntity();
		// boxShape takes the half width/height as input
		car.addComponent(new DimensionComponent(CAR_LENGTH, CAR_WIDTH));
		/** @formatter:off */
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(CAR_LENGTH   / 2, CAR_WIDTH   / 2)
																.density(1f)
																.restitution(1.0f)
																.friction(0f);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.DynamicBody)
															.position(position)
															.angle(angle)
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
	 * @param angle The andgle in degrees
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createRoad(TrafficSimWorld world, Vector2 position, float length, float angle, String name) {
		Entity road = world.createEntity();
		// TODO Check number of lanes here
		road.addComponent(new DimensionComponent(length, LANE_WIDTH * 2));
		// else
		// road.addComponent(new DimensionComponent(LANE_WIDTH * 2, length));
		angle *= MathUtils.degRad;

		// boxShape takes the half width/height as input
		// TODO Check number of lanes here
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(length / 2, LANE_WIDTH * 2 / 2)
																.density(1.0f)
																.restitution(1.0f)
																.friction(0f)
																.sensor(true) // There should be a better way 
																.groupIndex((short) -1);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.StaticBody)
															.position(position)
															.angle(angle)
															.build();
		road.addComponent(new PhysicsBodyComponent(body));
		// road.addComponent(new PositionComponent(position));

		SpriteComponent sprite = new SpriteComponent(name);
		road.addComponent(sprite);
		return road;
	}

	/**
	 * Creates a road with the given parameters
	 * 
	 * @param angle The andgle in degrees
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createRoad(TrafficSimWorld world, Road data) {
		Entity road = world.createEntity();

		Vector2 vector = data.getPointB().cpy().sub(data.getPointA());
		Vector2 position = new Vector2((data.getPointB().x + data.getPointA().x) / 2, (data.getPointB().y + data.getPointA().y) / 2);
		float length = vector.len();
		float angle = vector.angle();

		// TODO Check number of lanes here
		String name = "road1x1";
		road.addComponent(new DimensionComponent(length, LANE_WIDTH * 2));
		angle *= MathUtils.degRad;

		// boxShape takes the half width/height as input
		// TODO Check number of lanes here
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(length / 2, LANE_WIDTH * 2 / 2)
																.density(1.0f)
																.restitution(1.0f)
																.friction(0f)
																.sensor(true) // There should be a better way
																.groupIndex((short) -1);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.StaticBody)
															.position(position)
															.angle(angle)
															.build();
		road.addComponent(new PhysicsBodyComponent(body));
		// road.addComponent(new PositionComponent(position));

		SpriteComponent sprite = new SpriteComponent(name);
		road.addComponent(sprite);
		return road;
	}

	public static Entity createCar(TrafficSimWorld world, Vector2 vector2, float acceleration, String string) {
		return createCar(world, vector2, acceleration, 0, string);
	}

	public static void populateWorld(TrafficSimWorld world, Graph<Road> graph) {
		for (Edge<Road> edge : graph.getEdgeIterator()) {
			world.addEntity(createRoad(world, edge.getData()));
			Vector2 vector = edge.getData().getPointB().cpy().sub(edge.getData().getPointA());
			Vector2 position = new Vector2((edge.getData().getPointB().x + edge.getData().getPointA().x) / 2,
											(edge.getData().getPointB().y + edge.getData().getPointA().y) / 2);
			System.out.println("pointA " + edge.getData().getPointA());
			System.out.println("pointB " + edge.getData().getPointB());
			System.out.println("vector " + vector);
			System.out.println("pos " + position);
			System.out.println("len " + vector.len());
			// Entity road = createRoad(world, position, vector.len(), vector.angle(), "road1x1");
			// world.addEntity(road);
		}
	}

}
