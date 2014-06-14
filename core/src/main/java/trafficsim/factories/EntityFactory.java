package trafficsim.factories;

import static com.badlogic.gdx.math.MathUtils.degRad;
import static functions.VectorUtils.getMidPoint;
import static functions.VectorUtils.getVector;
import static trafficsim.TrafficSimConstants.*;
import functions.VectorUtils;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

import paramatricCurves.ParametricCurve;
import paramatricCurves.curveDefs.C_Linear;
import trafficsim.TrafficSimConstants;
import trafficsim.TrafficSimWorld;
import trafficsim.components.AttachedLightsComponent;
import trafficsim.components.DataComponent;
import trafficsim.components.DimensionComponent;
import trafficsim.components.ExpiryComponent;
import trafficsim.components.GroupedTrafficLightComponent;
import trafficsim.components.GroupedTrafficLightComponent.GroupedTrafficLightData;
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
import trafficsim.components.VehiclesOnRoadComponent;
import trafficsim.movement.BrakeBehavior;
import trafficsim.movement.SeekBehavior;
import trafficsim.movement.WeightedBehavior;
import trafficsim.roads.CrossRoad;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;
import trafficsim.spawning.AbstractSpawnStrategy;
import trafficsim.spawning.FixedIntervalSpawningStrategy;
import trafficsim.spawning.PoissonSpawnStrategy;

import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Contains methods to create the different entities used in the world and add
 * the required components
 * 
 * @author Giannis Papadopoulos
 */
public class EntityFactory {

	/**
	 * Creates a car with the given position and acceleration
	 * 
	 * @param position
	 *        The position of the center of mass
	 * @param angleInRads
	 *        The angle in radians
	 * @param name
	 *        Must be the same as the name of the texture file
	 */
	public static Entity createCar(TrafficSimWorld world, Vector2 position,
			float maxForce, float maxSpeed, float angleInRads, String name) {
		Entity car = world.createEntity();
		// boxShape takes the half width/height as input
		car.addComponent(new DimensionComponent(CAR_LENGTH, CAR_WIDTH));
		/** @formatter:off */
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder()
				.boxShape(CAR_LENGTH / 2, CAR_WIDTH / 2).density(1f)
				.restitution(0.2f).friction(0f);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
				.type(BodyType.DynamicBody).position(position)
				.angle(angleInRads).userData(car.getId()).build();
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
		behavior.add(new BrakeBehavior(DEFAULT_BRAKING_FACTOR), 0f);
		car.addComponent(new MovementComponent(behavior));
		return car;
	}

	/**
	 * Creates a road with the given parameters
	 * 
	 * @param angle
	 *        The angle in degrees
	 * @param name
	 *        Must be the same as the name of the texture file
	 */
	public static Entity createRoad(TrafficSimWorld world,
			Edge<NavigationObject> edge) {
		Road roadData = (Road) edge.getData();
		String name;
		Entity road = world.createEntity();

		world.getEdgeToEntityMap().put(edge.getID(), road.getId());
		road.addComponent(new AttachedLightsComponent());
		name = "road1x1";

		Vector2 position = new Vector2(
				(roadData.getPointB().x + roadData.getPointA().x) / 2,
				(roadData.getPointB().y + roadData.getPointA().y) / 2);
		float angle = VectorUtils.getAngle(roadData);

		// boxShape takes the half width/height as input
		// TODO Check number of lanes here
		float length = VectorUtils.getLength(roadData);
		road.addComponent(new DimensionComponent(length, 2 * LANE_WIDTH * roadData.getNumLanes()));
		angle *= MathUtils.degRad;


		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(length / 2, LANE_WIDTH * 2 / 2)
																.density(1.0f)
																.restitution(1.0f)
																.friction(0f)
																.sensor(true)
																.groupIndex((short) -1);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.StaticBody)
															.position(position)
															.angle(angle)
															.build();
		road.addComponent(new PhysicsBodyComponent(body));
		// road.addComponent(new PositionComponent(position));

		road.addComponent(new VehiclesOnRoadComponent());

		SpriteComponent sprite = new SpriteComponent(name);
		road.addComponent(sprite);
		return road;
	}

	private static Entity createCrossRoad(TrafficSimWorld world,
			Vertex<NavigationObject> vertex) {
		CrossRoad crossRoadData = (CrossRoad) vertex.getData();
		String name;
		Entity crossRoad = world.createEntity();

		world.getVertexToEntityMap().put(vertex.getID(), crossRoad.getId());
		crossRoad.addComponent(new AttachedLightsComponent());
		name = "road1x1";

		Vector2 position = crossRoadData.getPosition();
		float angle = 0.0f;

		float length = crossRoadData.getSize();

		// TODO Check number of lanes here
		crossRoad.addComponent(new DimensionComponent(length, length));
		angle *= MathUtils.degRad;

		// boxShape takes the half width/height as input
		// TODO Check number of lanes here
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder().boxShape(length / 2, LANE_WIDTH * 2 / 2)
																.density(1.0f)
																.restitution(1.0f)
																.friction(0f)
																.sensor(true)
																.groupIndex((short) -1);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
															.type(BodyType.StaticBody)
															.position(position)
															.angle(angle)
															.build();
		crossRoad.addComponent(new PhysicsBodyComponent(body));
		// road.addComponent(new PositionComponent(position));

		SpriteComponent sprite = new SpriteComponent(name);
		crossRoad.addComponent(sprite);
		// || vertex.getID() == vertex.getParent().getVertexCount() - 1
		if (vertex.getID() == 0 || vertex.getID() == vertex.getParent().getVertexCount() - 5) {
			crossRoad.addComponent(new SpawnComponent(vertex, new FixedIntervalSpawningStrategy(2000 * 1000)));
		}
		return crossRoad;
	}

	public static float computeAngle(Vector2 vector) {
		float angle = vector.angle();
		return angle;
	}

	public static List<Entity> populateWorld(TrafficSimWorld world,
			Graph<NavigationObject> graph) {
		world.setGraph(graph);
		List<Entity> vertexEntities = new ArrayList<Entity>();
		for (Vertex<NavigationObject> vertex : graph.getVertexIterator()) {
			Entity vertexEntity = createCrossRoad(world, vertex);
			vertexEntity.addToWorld();
			vertexEntities.add(vertexEntity);
		}
		for (Edge<NavigationObject> edge : graph.getEdgeIterator()) {
			createRoad(world, edge).addToWorld();
		}
		return vertexEntities;
	}


	public static void addTrafficLights(TrafficSimWorld world, Graph<NavigationObject> graph,
			List<Entity> vertexEntities) {
		int interval = TRAFFIC_LIGHT_GREEN_INTERVAL;
		int orangeInterval = TRAFFIC_LIGHT_ORANGE_INTERVAL;

		// iterator
		int index = 0;

		for (Vertex<NavigationObject> vertex : graph.getVertexIterator()) {
			List<List<GroupedTrafficLightData>> groupedLights = new ArrayList<List<GroupedTrafficLightData>>();
			int edgesPerVertex = vertex.getAdjacentEdges().size();
			for (Edge<NavigationObject> edge : vertex.getIncomingEdgeIterator()) {

				// val iterator2 = edge.getAdjacentVertexIterator();
				boolean onPointA = edge.getAdjacentVertices().get(0) == vertex
						.getID();
				float angleOfRoad = VectorUtils.getAngle((Road) edge.getData());
				TIntList lightIDs = addLight(world, edge, vertex, edgesPerVertex, angleOfRoad, onPointA);
				List<GroupedTrafficLightData> leftAndStraightData = new ArrayList<GroupedTrafficLightData>();
				if (lightIDs.size() > 0) {
					for (int i = 0; i < lightIDs.size(); i++) {
						leftAndStraightData.add(new GroupedTrafficLightData(lightIDs.get(i), interval - orangeInterval,
																			orangeInterval, edge.getID()));
					}
					groupedLights.add(leftAndStraightData);
				}
			}
			// Entity vertexEntity = world.getEntity(world.getVertexToEntityMap().get(vertex.getID()));
			Entity vertexEntity = vertexEntities.get(index);
			if (groupedLights.size() > 0) {
				vertexEntity.addComponent(new GroupedTrafficLightComponent(groupedLights, vertex.getID()));
			}
			index++;
			// System.out.println(vertexEntity.getComponents(new Bag<Component>()));
		}
	}

	/** Returns a list of IDs of the lights created */
	private static TIntList addLight(TrafficSimWorld world, Edge<NavigationObject> edge,
			Vertex<NavigationObject> vertex,
			int edgesPerVertex,
			float angleOfRoad, boolean onPointA) {
		TIntList trafficLightIDs = new TIntArrayList();
		if (vertex.getAdjacentVertices().size() > 1) {

			Road road = (Road) edge.getData();
			Vector2 pos = onPointA ? road.getPointA().cpy() : road.getPointB().cpy();
			int direction = onPointA ? 1 : -1;
			pos.add(VectorUtils.getVector(road).nor().scl(direction));
			// Vector2 pos = edge.getData().getPointA().cpy().add(VectorUtils.getVector(edge.getData()).nor().scl(1f));
			Vector2 corr = getVector(road).nor().rotate(90 * direction);

			// int for changing speed of lights
			int interval = TRAFFIC_LIGHT_GREEN_INTERVAL;

			Vector2 roadVector = getVector(road);
			if (onPointA)
				roadVector.scl(-1);
			float angle = roadVector.angle() * degRad;

			// .add(corr.cpy().scl(2f))
			Entity entityStraight = EntityFactory.createTrafficLight(	world, pos.cpy(),
																		(interval - 1), 1, (interval * 3), Status.RED,
																		true, onPointA, angle);
			entityStraight.addComponent(new LightToRoadMappingComponent(entityStraight.getId(),
																		world.getEdgeToEntityMap().get(edge.getID())));
			entityStraight.addToWorld();
			trafficLightIDs.add(entityStraight.getId());
			// TODO left light should always point at a 90 degree angle from the road
			Entity entityLeft = EntityFactory.createTrafficLight(	world, pos.cpy().add(corr.cpy().scl(1f)),
																	(interval - 1), 1, (interval * 3), Status.RED,
																	false, onPointA, angle);
			entityLeft.addComponent(new LightToRoadMappingComponent(entityStraight.getId(), world.getEdgeToEntityMap()
																									.get(edge.getID())));
			entityLeft.addToWorld();
			trafficLightIDs.add(entityLeft.getId());
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
				if (onPointA) {
					TrafficLightComponent verticalTopLeft = entityLeft.getComponent(TrafficLightComponent.class);
					verticalTopLeft.setTimeElapsed(interval);
					TrafficLightComponent verticalTopStraight = entityStraight
							.getComponent(TrafficLightComponent.class);
					verticalTopStraight.setTimeElapsed(interval);
				}

			}

		}
		return trafficLightIDs;
	}

	public static Entity createTrafficLight(TrafficSimWorld world, Vector2 position, float timerG, float timerO,
			float timerR, Status status, boolean straight, boolean OnPointA, float angleInRads) {
		Entity trafficLight = world.createEntity();
		float width = 1f;
		float length = 1f;
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder()
				.boxShape(length / 2, width / 2).density(1.0f)
				.restitution(1.0f).friction(0f).sensor(true)
				// There should be a better way
				.groupIndex((short) -1);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
				.type(BodyType.StaticBody).position(position)
				.angle(angleInRads).build();
		trafficLight.addComponent(new PhysicsBodyComponent(body));
		trafficLight.addComponent(new DimensionComponent(length, width));

		TrafficLightComponent lightComp = new TrafficLightComponent(timerG, timerO, timerR, status, !straight, OnPointA);
		trafficLight.addComponent(lightComp);

		SpriteComponent sprite = new SpriteComponent(lightComp.getTextureName());
		trafficLight.addComponent(sprite);

		return trafficLight;
	}

	public static void addSpawnPointsTest(TrafficSimWorld world, Graph<NavigationObject> graph) {

		float length = 60;
		int interval = 3000;

		int[] indices = { 0, graph.getVertexCount() - 1, (int) Math.sqrt(graph.getVertexCount() - 1),
							graph.getVertexCount() - (int) Math.sqrt(graph.getVertexCount()) };

		Vertex<NavigationObject> connection = graph.getVertex(indices[0]);
		// Vector2 edgeB = connection.getData().getPointA();
		Vector2 edgeB = connection.getData().getPosition();
		Vector2 edgeA = new Vector2(edgeB.x - length, edgeB.y);
		makeSpawnVertex(world, connection, graph, edgeA, edgeB, new Vector2(edgeA.x - 2 * LANE_WIDTH, edgeA.y), edgeA,
						true, interval);

		Vertex<NavigationObject> connection2 = graph.getVertex(indices[1]);
		// Vector2 edgeA2 = connection2.getData().getPointB();
		Vector2 edgeA2 = connection2.getData().getPosition();
		Vector2 edgeB2 = new Vector2(edgeA2.x + length, edgeA2.y);
		makeSpawnVertex(world, connection2, graph, edgeA2, edgeB2, edgeB2, new Vector2(edgeB2.x + 2 * LANE_WIDTH,
																						edgeB2.y), false, interval);

		Vertex<NavigationObject> connection3 = graph.getVertex(indices[2]);
		// Vector2 edgeB3 = connection3.getData().getPointA();
		Vector2 edgeB3 = connection3.getData().getPosition();
		Vector2 edgeA3 = new Vector2(edgeB3.x - length, edgeB3.y);
		makeSpawnVertex(world, connection3, graph, edgeA3, edgeB3, new Vector2(edgeA3.x - 2 * LANE_WIDTH, edgeA3.y),
						edgeA3, true, interval);

		Vertex<NavigationObject> connection4 = graph.getVertex(indices[3]);
		// Vector2 edgeA4 = connection4.getData().getPointB();
		Vector2 edgeA4 = connection4.getData().getPosition();
		Vector2 edgeB4 = new Vector2(edgeA4.x + length, edgeA4.y);
		makeSpawnVertex(world, connection4, graph, edgeA4, edgeB4, edgeB4, new Vector2(edgeB4.x + 2 * LANE_WIDTH,
																						edgeB4.y), false, interval);
	}

	public static boolean poisson = true;

	// TODO Refactor this mess
	public static Vertex<NavigationObject> makeSpawnVertex(TrafficSimWorld world, Vertex<NavigationObject> connection,
			Graph<NavigationObject> graph, Vector2 edgeA, Vector2 edgeB, Vector2 vertexA, Vector2 vertexB,
			boolean AtoB,
			int interval) {
		// NavigationObject intersection = new Road(vertexA, vertexB, 1, Direction.BOTH, CITY_SPEED_LIMIT);
		Vertex<NavigationObject> spawn1 = graph.addVertex(new CrossRoad(2 * LANE_WIDTH,
																		new Vector2(getMidPoint(vertexA, vertexB)),CrossRoad.CR_TYPE.SpawnPoint));
		// NavigationObject edge = new Road(edgeA, edgeB, 1, Direction.BOTH, CITY_SPEED_LIMIT);

		Vector2 pointA1 = new Vector2(edgeA.x + LANE_WIDTH, edgeA.y - LANE_WIDTH / 2);
		Vector2 pointB1 = new Vector2(edgeB.x - LANE_WIDTH, edgeB.y - LANE_WIDTH / 2);

		Vector2 pointA2 = new Vector2(edgeA.x + LANE_WIDTH, edgeA.y + LANE_WIDTH / 2);
		Vector2 pointB2 = new Vector2(edgeB.x - LANE_WIDTH, edgeB.y + LANE_WIDTH / 2);

		Edge<NavigationObject> roadEdge1 = graph.addEdge(	new Road(
																		new ParametricCurve(new C_Linear(pointA1,
																											pointB1)),
																		1, TrafficSimConstants.CITY_SPEED_LIMIT,
																		(CrossRoad) spawn1.getData(),
																		(CrossRoad) connection.getData()), spawn1,
															connection, true);
		Edge<NavigationObject> roadEdge2 = graph.addEdge(	new Road(
																		new ParametricCurve(new C_Linear(pointB2,
																											pointA2)),
																		1, TrafficSimConstants.CITY_SPEED_LIMIT,
																		(CrossRoad) connection.getData(),
																		(CrossRoad) spawn1.getData()), connection,
															spawn1,
															true);
		// System.out.println(VectorUtils.getAngle(intersection) + " " + VectorUtils.getAngle(edge));
		// Edge<NavigationObject> roadEdge;
		// if (AtoB)
		// roadEdge = graph.addEdge(edge, spawn1, connection, false);
		// else
		// roadEdge = graph.addEdge(edge, connection, spawn1, false);
		EntityFactory.createCrossRoad(world, spawn1).addToWorld();
		EntityFactory.createRoad(world, roadEdge1).addToWorld();
		EntityFactory.createRoad(world, roadEdge2).addToWorld();
		Entity spawnPoint = world.createEntity();
		AbstractSpawnStrategy spawnStrategy = poisson	? new PoissonSpawnStrategy(interval)
														: new FixedIntervalSpawningStrategy(interval);
		// AbstractSpawnStrategy spawnStrategy = new PoissonSpawnStrategy(interval);
		spawnPoint.addComponent(new SpawnComponent(spawn1, spawnStrategy));
		spawnPoint.addToWorld();
		return spawn1;
	}

	public static void addSpawnPoints(TrafficSimWorld world, Graph<NavigationObject> graph, List<Entity> vertexEntities) {
		int index = 0; // iterator
		for (Vertex<NavigationObject> vertex : graph.getVertexIterator()) {
			if (vertex.getAdjacentVertices().size() == 1) {
				// Entity vertexEntity = world.getEntity(world.getVertexToEntityMap().get(vertex.getID()));
				Entity vertexEntity = vertexEntities.get(index);
				if (vertexEntity.getComponent(SpawnComponent.class) == null) {
					float interval = 2000;
					// AbstractSpawnStrategy spawnStrategy = new FixedIntervalSpawningStrategy(interval);
					AbstractSpawnStrategy spawnStrategy = new PoissonSpawnStrategy(interval);
					vertexEntity.addComponent(new SpawnComponent(vertex, spawnStrategy));
					// world.changedEntity(vertexEntity);
				}
			}
			index++;
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
		FixtureDefBuilder fixtureDef = new FixtureDefBuilder()
				.boxShape(length / 2, width / 2).density(1.0f)
				.restitution(1.0f).friction(0f).sensor(true)
				// There should be a better way
				.groupIndex((short) -2);
		Body body = new BodyBuilder(world.getBox2dWorld()).fixture(fixtureDef)
				.type(BodyType.StaticBody).position(position).angle(angle)
				.build();
		backGround.addComponent(new PhysicsBodyComponent(body));
		// road.addComponent(new PositionComponent(position));

		SpriteComponent sprite = new SpriteComponent(name);
		backGround.addComponent(sprite);
		return backGround;
	}

}
