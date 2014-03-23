package trafficsim.factories;

import static functions.VectorUtils.getVector;
import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.CAR_WIDTH;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import functions.VectorUtils;
import graph.Edge;
import graph.Element;
import graph.Graph;
import graph.Vertex;
import lombok.val;
import trafficsim.TrafficSimWorld;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.DimensionComponent;
import trafficsim.components.LightToRoadMappingComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;
import trafficsim.components.SteeringComponent;
import trafficsim.components.SteeringComponent.State;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;
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

		return car;
	}

	// /**
	// * Creates a road with the given parameters
	// *
	// * @param angle The andgle in degrees
	// * @param name Must be the same as the name of the texture file
	// */
	// public static Entity createRoad(TrafficSimWorld world, Vector2 position, float length, float angle, String name)
	// {
	// Entity road = world.createEntity();
	// // TODO Check number of lanes here
	// road.addComponent(new DimensionComponent(length, LANE_WIDTH * 2));
	// // else
	// // road.addComponent(new DimensionComponent(LANE_WIDTH * 2, length));
	// angle *= MathUtils.degRad;
	//
	// // boxShape takes the half width/height as input
	// // TODO Check number of lanes here
	// FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(length / 2, LANE_WIDTH * 2 / 2)
	// .density(1.0f)
	// .restitution(1.0f)
	// .friction(0f)
	// .sensor(true) // There should be a better way
	// .groupIndex((short) -1);
	// Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
	// .type(BodyType.StaticBody)
	// .position(position)
	// .angle(angle)
	// .build();
	// road.addComponent(new PhysicsBodyComponent(body));
	// // road.addComponent(new PositionComponent(position));
	//
	// SpriteComponent sprite = new SpriteComponent(name);
	// road.addComponent(sprite);
	// return road;
	// }

	/**
	 * Creates a road with the given parameters
	 * 
	 * @param angle The angle in degrees
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createRoad(TrafficSimWorld world, Element<Road> element) {
		Road roadData = element.getData();
		Entity road = world.createEntity();
		if (element.getClass() == Vertex.class) {
			world.getVertexToEntityMap().put(element.getID(), road.getId());
		}
		else {
			world.getEdgeToEntityMap().put(element.getID(), road.getId());
			road.addComponent(new AttachedLightsComponent());
		}
		
		Vector2 position = new Vector2((roadData.getPointB().x + roadData.getPointA().x) / 2,
										(roadData.getPointB().y + roadData.getPointA().y) / 2);
		float angle = VectorUtils.getAngle(roadData);

		float length = VectorUtils.getLength(roadData);

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
			val iterator = vertex.getAdjacentEdgeIterator();
			int edgesPerVertex = vertex.getAdjacentEdges().size();
			for (Edge<Road> edge : iterator) {

				val iterator2 = edge.getAdjacentVertexIterator();
				// TODO single for loop
				boolean onPointA = edge.getAdjacentVertices().get(0) == vertex.getID();
				float angleOfRoad = VectorUtils.getAngle(edge.getData());

				if (onPointA) {
					Vertex<Road> vertexA = iterator2.next();
					// addLightA(world, edge, vertexA, edgesPerVertex, angleOfRoad);
					addLight(world, edge, vertexA, edgesPerVertex, angleOfRoad, onPointA);
				}
				else {
					Vertex<Road> vertexB = iterator2.next();
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
			int interval = 3;
			
			Entity entityStraight = EntityFactory.createTrafficLight(	world, pos.cpy().add(corr.cpy().scl(2f)),
																		(int) (interval - 1), 1, (int) (interval * 3),
																		Status.RED, true, onPointA);
			entityStraight.addComponent(new LightToRoadMappingComponent(entityStraight.getId(),
																		world.getEdgeToEntityMap().get(edge.getID())));
			entityStraight.addToWorld();
			// TODO left light should always point at a 90 degree angle from the road
			Entity entityLeft = EntityFactory.createTrafficLight(	world, pos.cpy().add(corr.cpy().scl(1f)),
																	(int) (interval - 1), 1, (int) (interval * 3),
																	Status.RED, false, onPointA);
			entityLeft.addComponent(new LightToRoadMappingComponent(entityStraight.getId(), world.getEdgeToEntityMap()
																									.get(edge.getID())));
			entityLeft.addToWorld();
			if (angleOfRoad < 45) {
				if(onPointA){
				TrafficLightComponent flatRightLeft = entityLeft.getComponent(TrafficLightComponent.class);
				flatRightLeft.setStatus(Status.GREEN);
				TrafficLightComponent flatRightStraight = entityStraight.getComponent(TrafficLightComponent.class);
				flatRightStraight.setStatus(Status.GREEN);
				}
				else {
					TrafficLightComponent flatLeftLeft = entityLeft.getComponent(TrafficLightComponent.class);
					flatLeftLeft.setTimeElapsed(interval*2);
					TrafficLightComponent flatLeftStraight = entityStraight.getComponent(TrafficLightComponent.class);
					flatLeftStraight.setTimeElapsed(interval*2);
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

	public static Entity createTrafficLight(TrafficSimWorld world, Vector2 position, int timerG, int timerO,
			int timerR, Status status, boolean straight, boolean OnPointA) {
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
															.angle(0)
															.build();
		trafficLight.addComponent(new PhysicsBodyComponent(body));
		trafficLight.addComponent(new DimensionComponent(length, width));


		TrafficLightComponent lightComp = new TrafficLightComponent(timerG, timerO, timerR, status, !straight, OnPointA);
		trafficLight.addComponent(lightComp);

		SpriteComponent sprite = new SpriteComponent(lightComp.getTextureName());
		trafficLight.addComponent(sprite);

		return trafficLight;
	}

	// private static void addLightA(TrafficSimWorld world, Edge<Road> edge, Vertex<Road> vertexA, int edgesPerVertex,
	// float angleOfRoad) {
	// if (vertexA.getAdjacentVertices().size() > 1) {
	// Vector2 posA = edge.getData().getPointA().cpy().add(VectorUtils.getVector(edge.getData()).nor().scl(1f));
	// Vector2 corr = getVector(edge.getData()).nor().rotate(90);
	// int interval = 5;
	// Entity entityStraight = EntityFactory.createTrafficLight( world, posA.cpy().add(corr.cpy().scl(2f)),
	// (int) (interval - 1), 1, (int) (interval * 3),
	// Status.RED, true);
	// entityStraight.addComponent(new LightToRoadMappingComponent(entityStraight.getId(),
	// world.getEdgeToEntityMap().get(edge.getID())));
	// entityStraight.addToWorld();
	// // TODO left light should always point at a 90 degree angle from the road
	// Entity entityLeft = EntityFactory.createTrafficLight( world, posA.cpy().add(corr.cpy().scl(1f)),
	// (int) (interval - 1), 1, (int) (interval * 3),
	// Status.RED, false);
	// entityLeft.addComponent(new LightToRoadMappingComponent(entityStraight.getId(), world.getEdgeToEntityMap()
	// .get(edge.getID())));
	// entityLeft.addToWorld();
	// if (angleOfRoad < 45) {
	// TrafficLightComponent flatRightLeft = entityLeft.getComponent(TrafficLightComponent.class);
	// flatRightLeft.setTimeElapsed(interval * 2);
	// TrafficLightComponent flatRightStraight = entityStraight.getComponent(TrafficLightComponent.class);
	// flatRightStraight.setStatus(Status.GREEN);
	//
	// }
	// else {
	// TrafficLightComponent VerticalTopStraight = entityStraight.getComponent(TrafficLightComponent.class);
	// VerticalTopStraight.setTimeElapsed(interval);
	// }
	//
	// }
	// }
	//
	// private static void addLightB(TrafficSimWorld world, Edge<Road> edge, Vertex<Road> vertexB, int edgesPerVertex,
	// float angleOfRoad) {
	// if (vertexB.getAdjacentVertices().size() > 1) {
	// Vector2 posB = edge.getData().getPointB().cpy().add(VectorUtils.getVector(edge.getData()).nor().scl(-1f));
	// Vector2 corr = getVector(edge.getData()).nor().rotate(-90);
	// int interval = 5;
	//
	// Entity entityStraight = EntityFactory.createTrafficLight( world, posB.cpy().add(corr.cpy().scl(2f)),
	// (int) (interval - 1), 1, (int) (interval * 3),
	// Status.RED, true);
	// entityStraight.addComponent(new LightToRoadMappingComponent(entityStraight.getId(),
	// world.getEdgeToEntityMap().get(edge.getID())));
	// entityStraight.addToWorld();
	// // TODO left light should always point at a 90 degree angle from the road
	// Entity entityLeft = EntityFactory.createTrafficLight( world, posB.cpy().add(corr.cpy().scl(1f)),
	// (int) (interval - 1), 1, (int) (interval * 3),
	// Status.RED, false);
	// entityLeft.addComponent(new LightToRoadMappingComponent(entityStraight.getId(), world.getEdgeToEntityMap()
	// .get(edge.getID())));
	// entityStraight.addToWorld();
	// entityLeft.addToWorld();
	// if (angleOfRoad < 45) {
	// TrafficLightComponent flatLeftLeft = entityLeft.getComponent(TrafficLightComponent.class);
	// flatLeftLeft.setTimeElapsed(interval * 2);
	// TrafficLightComponent flatLeftStraight = entityStraight.getComponent(TrafficLightComponent.class);
	// flatLeftStraight.setStatus(Status.GREEN);
	//
	// }
	// else {
	// TrafficLightComponent VerticalBotStraight = entityStraight.getComponent(TrafficLightComponent.class);
	// VerticalBotStraight.setTimeElapsed(interval);
	//
	// }
	//
	// }
	// }

}
