package trafficsim.screens;

import static trafficsim.TrafficSimConstants.DEBUG_FPS;
import static trafficsim.TrafficSimConstants.DEBUG_RENDER;
import static trafficsim.TrafficSimConstants.TIMER;
import graph.Graph;
import graph.GraphFactory;
import lombok.Getter;
import lombok.Setter;
import trafficsim.TrafficSimWorld;
import trafficsim.components.DataSystem;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;
import trafficsim.systems.DestinationSystem;
import trafficsim.systems.ExpirySystem;
import trafficsim.systems.InputSystem;
import trafficsim.systems.MovementSystem;
import trafficsim.systems.PathFindingSystem;
import trafficsim.systems.PhysicsSystem;
import trafficsim.systems.RenderSystem;
import trafficsim.systems.SpawnSystem;
import trafficsim.systems.TrafficLightSystem;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * The main screen of the simulation
 * 
 * @author Giannis Papadopoulos
 * 
 */
public class SimulationScreen extends SuperScreen {

	@Getter
	// So it's accessible by EditorScreen
	@Setter
	// So it's mutable by EditorScreen
	private TrafficSimWorld world;

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true, false, false, false, true, true);

	private boolean firstTimeSimulationRun = true;

	public SimulationScreen(Screens screens) {
		super(screens);

	}

	Entity car = null;

	@Override
	public void show() {

		if (world != null)
			world.dispose();
		world = new TrafficSimWorld();

		// Add systems
		world.setSystem(new DataSystem());
		world.setSystem(new RenderSystem(getCamera()));
		world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new PathFindingSystem());
		world.setSystem(new DestinationSystem());
		world.setSystem(new SpawnSystem());
		world.setSystem(new TrafficLightSystem());
		world.setSystem(new ExpirySystem());

		// Temporary hack
		// world.setSystem(new CollisionDisablingSystem());

		InputSystem inputSystem = new InputSystem(this);
		initMultiplexer();
		getMultiplexer().addProcessor(inputSystem);
		world.setSystem(inputSystem, true);

		world.initialize();

		EntityFactory.createBackground(world, "background").addToWorld();

		Graph<NavigationObject> graph;
		if (firstTimeSimulationRun ||  getScreens().getEditorScreen().getWorld()==null)
			graph = GraphFactory.createNewSystem();
		else
			graph = getScreens().getEditorScreen().getWorld().getGraph();
		world.setGraph(graph);
		EntityFactory.populateWorld(world, graph);
		
		firstTimeSimulationRun = false;


		EntityFactory.addTrafficLights(world, world.getGraph());


		if (TIMER.isStarted())
			TIMER.reset();
		TIMER.start();
		// GraphFactory.addSpawnPointsTest(world, world.getGraph());
		world.process();

		EntityFactory.addSpawnPoints(world, graph);

	}


	@Override
	public void render(float delta) {
		long start;
		if(DEBUG_FPS)
			start = TIMER.getTime();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		getCamera().update();
		world.setDelta(delta);

		world.process();
		if (DEBUG_RENDER)
			debugRenderer.render(world.getBox2dWorld(), getCamera().combined);

		getWorldLayer().act(delta);
		getUILayer().act(delta);
		getWorldLayer().draw();
		getUILayer().draw();

		if (DEBUG_FPS)
			System.out.println(TIMER.getTime() - start + " milliseconds ");
	}

	@Override
	public void populateUILayer() {

	}

	@Override
	public void populateWorldLayer() {

	}

}