package trafficsim.factories;

import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.CAR_WIDTH;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import trafficsim.TrafficSimWorld;
import trafficsim.components.DimensionComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.roads.Road;

import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import functions.VectorUtils;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

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
	 * @param angleInRads The angle in radians
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createCar(TrafficSimWorld world, Vector2 position, float maxForce, float maxSpeed,
			float angleInRads, String name) {
		Entity car = world.createEntity();
		// boxShape takes the half width/height as input
		car.addComponent(new DimensionComponent(CAR_LENGTH, CAR_WIDTH));
		/** @formatter:off */
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(CAR_LENGTH   / 2, CAR_WIDTH   / 2)
																.density(1f)
																.restitution(0.1f)
																.friction(0f);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.DynamicBody)
															.position(position)
															.angle(angleInRads)
															.build();
		
		/** @formatter:on */
		car.addComponent(new PhysicsBodyComponent(body));

		// SpriteComponent sprite = new SpriteComponent(name, 1, 1, 0);
		SpriteComponent sprite = new SpriteComponent(name);
		car.addComponent(sprite);

		// car.addComponent(new AccelerationComponent(acceleration));
		car.addComponent(new MaxSpeedComponent(maxSpeed));
		
		// TODO Steering is a magic constant, experiment with different cards
		car.addComponent(new SteeringComponent(State.DEFAULT, maxForce, 350f));

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
	 * @param angle The angle in degrees
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createRoad(TrafficSimWorld world, Road data) {
		Entity road = world.createEntity();

		// Vector2 vector = new GetVector().apply(data);
		Vector2 position = new Vector2((data.getPointB().x + data.getPointA().x) / 2, (data.getPointB().y + data.getPointA().y) / 2);
		float angle = VectorUtils.getAngle(data);

		float length = VectorUtils.getLength(data);

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

	public static float computeAngle(Vector2 vector) {
		float angle = vector.angle();
		return angle;
	}

	// public static Entity createCar(TrafficSimWorld world, Vector2 vector2, float acceleration, String string) {
	// return createCar(world, vector2, acceleration, 0, string);
	// }

	public static void populateWorld(TrafficSimWorld world, Graph<Road> graph) {
		for (Edge<Road> edge : graph.getEdgeIterator()) {
			world.addEntity(createRoad(world, edge.getData()));
		}
		for (Vertex<Road> vertex : graph.getVertexIterator()) {
			world.addEntity(createRoad(world, vertex.getData()));
		}
	}

}
