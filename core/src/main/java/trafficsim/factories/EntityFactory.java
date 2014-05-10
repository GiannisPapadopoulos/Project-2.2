package trafficsim.factories;

import static com.badlogic.gdx.math.MathUtils.degRad;
import static functions.VectorUtils.getVector;
import static trafficsim.TrafficSimConstants.*;
import functions.VectorUtils;
import graph.Edge;
import graph.Element;
import graph.Graph;
import graph.Vertex;
import trafficsim.TrafficSimWorld;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.DataComponent;
import trafficsim.components.DimensionComponent;
import trafficsim.components.ExpiryComponent;
import trafficsim.components.LightToRoadMappingComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.MovementComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpawnComponent;
import trafficsim.components.SpriteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;
import trafficsim.movement.SeekBehavior;
import trafficsim.movement.WeightedBehavior;
import trafficsim.roads.Road;
import trafficsim.spawning.FixedIntervalSpawningStrategy;

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
																.restitution(0.2f)
																.friction(0f);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.DynamicBody)
															.position(position)
															.angle(angleInRads)
															.userData(car.getId())
															.build();
		/** @formatter:on */
		car.addComponent(new PhysicsBodyComponent(body));

		// SpriteComponent sprite = new SpriteComponent(name, 1, 1, 0);
		SpriteComponent sprite = new SpriteComponent(name);
		car.addComponent(sprite);

		// car.addComponent(new AccelerationComponent(acceleration));
		car.addComponent(new MaxSpeedComponent(maxSpeed));
		
		// TODO Steering is a magic constant, experiment with different cars
		car.addComponent(new SteeringComponent(State.DEFAULT, maxForce, 350f));

		// Data
		car.addComponent(new DataComponent());
		// So that it can be properly erased when it reaches the destination
		car.addComponent(new ExpiryComponent());
		// add empty movement behavior
		WeightedBehavior behavior = new WeightedBehavior();
		behavior.add(new SeekBehavior(), 1f);
		car.addComponent(new MovementComponent(behavior));
		return car;
	}


	/**
	 * Creates a road with the given parameters
	 * 
	 * @param angle The angle in degrees
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createRoad(TrafficSimWorld world, Element<Road> element) {
		Road roadData = element.getData();
		String name;
		Entity road = world.createEntity();
		if (element.getClass() == Vertex.class) {
			world.getVertexToEntityMap().put(element.getID(), road.getId());
			name = "intersection";
		}
		else {
			world.getEdgeToEntityMap().put(element.getID(), road.getId());
			road.addComponent(new AttachedLightsComponent());
			name = "road1x1";
		}
		
		Vector2 position = new Vector2((roadData.getPointB().x + roadData.getPointA().x) / 2,
										(roadData.getPointB().y + roadData.getPointA().y) / 2);
		float angle = VectorUtils.getAngle(roadData);

		float length = VectorUtils.getLength(roadData);

		// TODO Check number of lanes here
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

	public static void populateWorld(TrafficSimWorld world, Graph<Road> graph) {
		world.setGraph(graph);
		for (Vertex<Road> vertex : graph.getVertexIterator()) {
			createRoad(world, vertex).addToWorld();
		}
		for (Edge<Road> edge : graph.getEdgeIterator()) {
			createRoad(world, edge).addToWorld();
		}

	}

	public static void addTrafficLights(TrafficSimWorld world, Graph<Road> graph) {
		/*
		 * for (Edge<Road> edge : graph.getEdgeIterator()) {
		 * val iterator = edge.getAdjacentVertexIterator();
		 * // TODO single for loop
		 * Vertex<Road> vertexA = iterator.next();
		 * if (vertexA.getAdjacentVertices().size() > 1) {
		 * Vector2 posA = edge.getData().getPointA().cpy().add(VectorUtils.getVector(edge.getData()).nor().scl(1f));
		 * Vector2 corr = getVector(edge.getData()).nor().rotate(90);
		 * EntityFactory.createTrafficLight(world, posA.cpy().add(corr.cpy().scl(2f)), 5, 2, 7, Status.GREEN,
		 * true).addToWorld();
		 * // TODO left light should always point at a 90 degree angle from the road
		 * EntityFactory.createTrafficLight(world, posA.cpy().add(corr.cpy().scl(1f)), 5, 2, 7, Status.RED, false)
		 * .addToWorld();
		 * }
		 * Vertex<Road> vertexB = iterator.next();
		 * if (vertexB.getAdjacentVertices().size() > 1) {
		 * Vector2 posB = edge.getData()
		 * .getPointB()
		 * .cpy()
		 * .add(VectorUtils.getVector(edge.getData()).nor().scl(-1f));
		 * Vector2 corr = getVector(edge.getData()).nor().rotate(-90);
		 * EntityFactory.createTrafficLight(world, posB.cpy().add(corr.cpy().scl(2f)), 5, 2, 7, Status.ORANGE,
		 * true).addToWorld();
		 * // TODO left light should always point at a 90 degree angle from the road
		 * EntityFactory.createTrafficLight(world, posB.cpy().add(corr.cpy().scl(1f)), 5, 2, 7, Status.RED, false)
		 * .addToWorld();
		 * }
		 * }
		 */

		for (Vertex<Road> vertex : graph.getVertexIterator()) {
			int edgesPerVertex = vertex.getAdjacentEdges().size();
			for (Edge<Road> edge : vertex.getAdjacentEdgeIterator()) {

				// val iterator2 = edge.getAdjacentVertexIterator();
				boolean onPointA = edge.getAdjacentVertices().get(0) == vertex.getID();
				float angleOfRoad = VectorUtils.getAngle(edge.getData());

				if (onPointA) {
					Vertex<Road> vertexA = graph.getVertex(edge.getAdjacentVertices().get(0));
					// addLightA(world, edge, vertexA, edgesPerVertex, angleOfRoad);
					addLight(world, edge, vertexA, edgesPerVertex, angleOfRoad, onPointA);
				}
				else {
					Vertex<Road> vertexB = graph.getVertex(edge.getAdjacentVertices().get(1));
					// addLightB(world, edge, vertexB, edgesPerVertex, angleOfRoad);
					addLight(world, edge, vertexB, edgesPerVertex, angleOfRoad, onPointA);
				}
			}
		}
	}

	private static void addLight(TrafficSimWorld world, Edge<Road> edge, Vertex<Road> vertex, int edgesPerVertex,
			float angleOfRoad, boolean onPointA) {
		if (vertex.getAdjacentVertices().size() > 1) {

			Vector2 pos = onPointA ? edge.getData().getPointA().cpy() : edge.getData().getPointB().cpy();
			int direction = onPointA ? 1 : -1;
			pos.add(VectorUtils.getVector(edge.getData()).nor().scl(direction));
			// Vector2 pos = edge.getData().getPointA().cpy().add(VectorUtils.getVector(edge.getData()).nor().scl(1f));
			Vector2 corr = getVector(edge.getData()).nor().rotate(90 * direction);
			
			//int for changing speed of lights
			float interval = 3f;
			
			Vector2 roadVector = getVector(edge.getData());
			if (onPointA)
				roadVector.scl(-1);
			float angle = roadVector.angle() * degRad;

			Entity entityStraight = EntityFactory.createTrafficLight(	world, pos.cpy().add(corr.cpy().scl(2f)),
																		(interval - 1), 1, (interval * 3), Status.RED,
																		true, onPointA, angle);
			entityStraight.addComponent(new LightToRoadMappingComponent(entityStraight.getId(),
																		world.getEdgeToEntityMap().get(edge.getID())));
			entityStraight.addToWorld();
			// TODO left light should always point at a 90 degree angle from the road
			Entity entityLeft = EntityFactory.createTrafficLight(	world, pos.cpy().add(corr.cpy().scl(1f)),
																	(interval - 1), 1, (interval * 3), Status.RED,
																	false, onPointA, angle);
			entityLeft.addComponent(new LightToRoadMappingComponent(entityStraight.getId(), world.getEdgeToEntityMap()
																									.get(edge.getID())));
			entityLeft.addToWorld();
			if (angleOfRoad < 45) {
				if (onPointA) {
					TrafficLightComponent flatRightLeft = entityLeft.getComponent(TrafficLightComponent.class);
					flatRightLeft.setStatus(Status.GREEN);
					TrafficLightComponent flatRightStraight = entityStraight.getComponent(TrafficLightComponent.class);
					flatRightStraight.setStatus(Status.GREEN);
				}
				else {
					TrafficLightComponent flatLeftLeft = entityLeft.getComponent(TrafficLightComponent.class);
					flatLeftLeft.setTimeElapsed(interval * 2);
					TrafficLightComponent flatLeftStraight = entityStraight.getComponent(TrafficLightComponent.class);
					flatLeftStraight.setTimeElapsed(interval * 2);
				}

			}
			else {
				if(onPointA){
					TrafficLightComponent verticalTopLeft = entityLeft.getComponent(TrafficLightComponent.class);
					verticalTopLeft.setTimeElapsed(interval);
					TrafficLightComponent verticalTopStraight = entityStraight.getComponent(TrafficLightComponent.class);
					verticalTopStraight.setTimeElapsed(interval);
				}
					
			}

		}
	}

	public static Entity createTrafficLight(TrafficSimWorld world, Vector2 position, float timerG, float timerO,
			float timerR, Status status, boolean straight, boolean OnPointA, float angleInRads) {
		Entity trafficLight = world.createEntity();
		float width = 1f;
		float length = 1f;
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(length / 2, width / 2)
																.density(1.0f)
																.restitution(1.0f)
																.friction(0f)
																.sensor(true)
																// There should be a better way
																.groupIndex((short) -1);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.StaticBody)
															.position(position)
															.angle(angleInRads)
															.build();
		trafficLight.addComponent(new PhysicsBodyComponent(body));
		trafficLight.addComponent(new DimensionComponent(length, width));


		TrafficLightComponent lightComp = new TrafficLightComponent(timerG, timerO, timerR, status, !straight, OnPointA);
		trafficLight.addComponent(lightComp);

		SpriteComponent sprite = new SpriteComponent(lightComp.getTextureName());
		trafficLight.addComponent(sprite);

		return trafficLight;
	}


	public static void addSpawnPoints(TrafficSimWorld world, Graph<Road> graph) {
		for (Vertex<Road> vertex : graph.getVertexIterator()) {
			if (vertex.getAdjacentVertices().size() == 1) {
				Entity vertexEntity = world.getEntity(world.getVertexToEntityMap().get(vertex.getID()));
				float interval = 2000;
				vertexEntity.addComponent(new SpawnComponent(vertex, new FixedIntervalSpawningStrategy(interval)));
				world.changedEntity(vertexEntity);
			}
		}
	}

	public static Entity createBackground(TrafficSimWorld world, String name) {
		Entity backGround = world.createEntity();

		float length = 1000;
		float width = 1000;

		Vector2 position = new Vector2(250, 250);
		float angle = 0;



		backGround.addComponent(new DimensionComponent(length, width));
		angle *= MathUtils.degRad;

		// boxShape takes the half width/height as input
		// TODO Check number of lanes here
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(length / 2, width / 2)
																.density(1.0f)
																.restitution(1.0f)
																.friction(0f)
																.sensor(true)
																// There should be a better way
																.groupIndex((short) -2);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.StaticBody)
															.position(position)
															.angle(angle)
															.build();
		backGround.addComponent(new PhysicsBodyComponent(body));
		// road.addComponent(new PositionComponent(position));

		SpriteComponent sprite = new SpriteComponent(name);
		backGround.addComponent(sprite);
		return backGround;
	}


}
