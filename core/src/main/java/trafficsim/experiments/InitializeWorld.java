package trafficsim.experiments;

import static trafficsim.TrafficSimConstants.CITY_SPEED_LIMIT;
import static trafficsim.TrafficSimConstants.TIMER;
import graph.EntityIdentificationData;
import graph.Graph;
import graph.GraphFactory;

import java.util.List;

import trafficsim.TrafficSimWorld;
import trafficsim.components.DataSystem;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.experiments.SimulationParameters.ManhattanGraphInfo;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.NavigationObject;
import trafficsim.screens.SimulationScreen;
import trafficsim.systems.AbstractToggleStrategy;
import trafficsim.systems.DestinationSystem;
import trafficsim.systems.ExpirySystem;
import trafficsim.systems.GroupedTrafficLightSystem;
import trafficsim.systems.InputSystem;
import trafficsim.systems.ManageMovementBehaviorsSystem;
import trafficsim.systems.ManageSpawnRateChangeSystem;
import trafficsim.systems.ManageSpeedLimitChangeSystem;
import trafficsim.systems.MovementSystem;
import trafficsim.systems.PathFindingSystem;
import trafficsim.systems.PhysicsSystem;
import trafficsim.systems.RenderSystem;
import trafficsim.systems.RoutingSystem;
import trafficsim.systems.SpawnSystem;
import ui.tables.CurrentFocus;
import ui.tables.InfoPop;

import com.artemis.Entity;

public class InitializeWorld {

	public static void init(TrafficSimWorld world, SimulationParameters parameters, SimulationScreen screen) {
		// Add systems
		world.setSystem(new DataSystem());
		RenderSystem renderSystem = new RenderSystem(screen.getCamera());
		world.setSystem(renderSystem);
		// world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new PathFindingSystem());
		world.setSystem(new DestinationSystem());
		world.setSystem(new SpawnSystem());
		AbstractToggleStrategy toggleStrategy = parameters.getToggleStrategy();
		world.setSystem(new GroupedTrafficLightSystem(toggleStrategy));

		world.setSystem(new RoutingSystem());
		world.setSystem(new MovementSystem());
		world.setSystem(new ManageMovementBehaviorsSystem());

		// Temporary hack
		// world.setSystem(new CollisionDisablingSystem());

		world.setSystem(new ExpirySystem());
		world.setSystem(new ManageSpawnRateChangeSystem());
		world.setSystem(new ManageSpeedLimitChangeSystem());

		InputSystem inputSystem = new InputSystem(screen);
		screen.initMultiplexer();
		screen.getMultiplexer().addProcessor(inputSystem);
		world.setSystem(inputSystem, true);

		CITY_SPEED_LIMIT = parameters.getSpeedLimit();

		world.initialize();

		EntityFactory.createBackground(world, "background").addToWorld();

		Graph<NavigationObject> graph;
		if (parameters.isManhattanGraph()) {
			ManhattanGraphInfo graphInfo = parameters.getGraphInfo();
			graph = GraphFactory.createManhattanGraph(graphInfo.getWidth(), graphInfo.getHeight(), 100f, 0, 0);
		}
		else {
			graph = GraphFactory.createTestOneGraph(parameters.isRoundabout());
		}
		world.setGraph(graph);
		// EntityFactory.addSpawnPointsTest(world, world.getGraph());
		// EntityFactory.addSpawnPoints(world, graph);
		List<Entity> vertexEntities = EntityFactory.populateWorld(world, graph);
		for (Entity vertexEntity : vertexEntities) {
			int vertexID = ((EntityIdentificationData) vertexEntity.getComponent(PhysicsBodyComponent.class)
																	.getUserData()).getID();
		}

		EntityFactory.addTrafficLights(world, world.getGraph(), vertexEntities);

		/** Set the world reference */
		screen.getSidePanels().setWorld(world);

		if (TIMER.isStarted())
			TIMER.reset();
		TIMER.start();
		screen.setPop(new InfoPop(renderSystem.getBatch()));
		screen.setFocus(new CurrentFocus(screen.getSidePanels()));
		System.out.println(screen.getFocus());
		world.process();
	}
}
