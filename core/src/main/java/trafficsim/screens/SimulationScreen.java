package trafficsim.screens;

import static trafficsim.TrafficSimConstants.DEBUG_FPS;
import static trafficsim.TrafficSimConstants.DEBUG_RENDER;
import static trafficsim.TrafficSimConstants.DEBUG_TABLES;
import static trafficsim.TrafficSimConstants.TIMER;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import trafficsim.TrafficSimWorld;
import trafficsim.components.DataSystem;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.NavigationObject;
import trafficsim.systems.AbstractToggleStrategy;
import trafficsim.systems.DestinationSystem;
import trafficsim.systems.ExpirySystem;
import trafficsim.systems.GroupedTrafficLightSystem;
import trafficsim.systems.InputSystem;
import trafficsim.systems.ManageMovementBehaviorsSystem;
import trafficsim.systems.MovementSystem;
import trafficsim.systems.PathFindingSystem;
import trafficsim.systems.PhysicsSystem;
import trafficsim.systems.RenderSystem;
import trafficsim.systems.RoutingSystem;
import trafficsim.systems.SpawnSystem;
import ui.tables.InfoPop;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import editor.WorldRenderer;
import graph.Graph;
import graph.GraphFactory;

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
	@Getter
	private InfoPop pop;

	private WorldRenderer wr;

	@Getter
	@Setter
	// TODO it's not perfectly functional
	private boolean paused;

	public SimulationScreen(Screens screens) {
		super(screens);
	}

	@Override
	public void show() {

		if (world != null)
			world.dispose();
		world = new TrafficSimWorld();

		// Add systems
		world.setSystem(new DataSystem());
		RenderSystem renderSystem = new RenderSystem(getCamera());
		world.setSystem(renderSystem);
		// world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new PathFindingSystem());
		world.setSystem(new DestinationSystem());
		world.setSystem(new SpawnSystem());
		// AbstractToggleStrategy toggleStrategy = AbstractToggleStrategy.basicToggleStrategy;
		AbstractToggleStrategy toggleStrategy = AbstractToggleStrategy.priorityToggleStrategy;
		world.setSystem(new GroupedTrafficLightSystem(toggleStrategy));


		world.setSystem(new RoutingSystem());
		world.setSystem(new MovementSystem());
		world.setSystem(new ManageMovementBehaviorsSystem());

		// Temporary hack
		// world.setSystem(new CollisionDisablingSystem());

		world.setSystem(new ExpirySystem());

		InputSystem inputSystem = new InputSystem(this);
		initMultiplexer();
		getMultiplexer().addProcessor(inputSystem);
		world.setSystem(inputSystem, true);

		world.initialize();

		EntityFactory.createBackground(world, "background").addToWorld();

		Graph<NavigationObject> graph;
		if (firstTimeSimulationRun ||  getScreens().getEditorScreen().getWorld()==null) {
			graph = GraphFactory.createManhattanGraph(10, 10, 100.0f, 0, 0);
			// graph = GraphFactory.addHighway(graph, 10, 10, 100.0f, 0, 0);
			// graph = GraphFactory.createNewSystem();
			graph = GraphFactory.createTestOneGraph(false);
		}
		else
			graph = getScreens().getEditorScreen().getWorld().getGraph();
		world.setGraph(graph);
		// EntityFactory.addSpawnPointsTest(world, world.getGraph());
		// EntityFactory.addSpawnPoints(world, graph);
		List<Entity> vertexEntities = EntityFactory.populateWorld(world, graph);
		
		firstTimeSimulationRun = false;



		EntityFactory.addTrafficLights(world, world.getGraph(), vertexEntities);

		/** Set the world reference */
		getSidePanels().setWorld(world);


		if (TIMER.isStarted())
			TIMER.reset();
		TIMER.start();
		pop = new InfoPop(renderSystem.getBatch());
		world.process();

		getCamera().translate(900, 0);

	}

	protected void initMultiplexer() {
		this.multiplexer = new InputMultiplexer(UILayer, worldLayer);
	}


	@Override
	public void render(float delta) {
		if (isPaused()) {
			return;
		}
		long start;
		if(DEBUG_FPS)
			start = TIMER.getTime();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		getCamera().update();
		world.setDelta(delta);


		world.process();
		if (DEBUG_RENDER)
			debugRenderer.render(world.getBox2dWorld(), getCamera().combined);

		// wr.renderDEBUG(getCamera(), world.getGraph());

		getWorldLayer().act(delta);
		getUILayer().act(delta);
		getWorldLayer().draw();
		getUILayer().draw();
		
		if (DEBUG_TABLES)
			Table.drawDebug(getUILayer());

		if (DEBUG_FPS) {
			long frameTime = TIMER.getTime() - start;
			if (frameTime > 1.1 * DELTA_TIME * 1000)
				System.out.println(frameTime + " milliseconds ");
		}
		pop.render();
		super.setWaitTimeUI(world);
		super.setCarsUI(world);
		
		
	}
	

	@Override
	public void populateUILayer() {

	}

	@Override
	public void populateWorldLayer() {

	}

}