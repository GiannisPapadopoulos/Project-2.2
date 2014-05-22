package trafficsim.screens;

import static trafficsim.TrafficSimConstants.*;
import graph.Graph;
import graph.GraphFactory;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import trafficsim.TrafficSimWorld;
import trafficsim.components.DataSystem;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.Road;
import trafficsim.systems.CollisionDisablingSystem;
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
import trafficsim.systems.TrafficLightSystem;
import ui.tables.InfoPop;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
		world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new PathFindingSystem());
		world.setSystem(new DestinationSystem());
		world.setSystem(new SpawnSystem());
		world.setSystem(new GroupedTrafficLightSystem());
		world.setSystem(new ExpirySystem());

		world.setSystem(new RoutingSystem());
		world.setSystem(new MovementSystem());
		world.setSystem(new ManageMovementBehaviorsSystem());

		// Temporary hack
		world.setSystem(new CollisionDisablingSystem());

		InputSystem inputSystem = new InputSystem(this);
		initMultiplexer();
		getMultiplexer().addProcessor(inputSystem);
		world.setSystem(inputSystem, true);

		world.initialize();

		EntityFactory.createBackground(world, "background").addToWorld();

		Graph<Road> graph;
		if (firstTimeSimulationRun ||  getScreens().getEditorScreen().getWorld()==null)
			graph = GraphFactory.createManhattanGraph(6, 5, 60, 0, 0);
		else
			graph = getScreens().getEditorScreen().getWorld().getGraph();
		world.setGraph(graph);
		

		firstTimeSimulationRun = false;

		// EntityFactory.addSpawnPoints(world, graph, vertexEntities);
		GraphFactory.addSpawnPointsTest(world, world.getGraph());
		List<Entity> vertexEntities = EntityFactory.populateWorld(world, graph);
		EntityFactory.addTrafficLights(world, world.getGraph(), vertexEntities);


		if (TIMER.isStarted())
			TIMER.reset();
		TIMER.start();
		GraphFactory.addSpawnPointsTest(world, world.getGraph());
		world.process();

		
		pop = new InfoPop(renderSystem.getBatch());
		
		

	}
	protected void initMultiplexer() {
		this.multiplexer = new InputMultiplexer(UILayer, worldLayer);
	}


	@Override
	public void render(float delta) {
		if (paused) {
			return;
		}
		long start;
		if(DEBUG_FPS)
			start = TIMER.getTime();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		getCamera().update();
		world.setDelta(delta);
		//super.render(delta);
		world.process();
		if (DEBUG_RENDER)
			debugRenderer.render(world.getBox2dWorld(), getCamera().combined);

		getWorldLayer().act(delta);
		getUILayer().act(delta);
		getWorldLayer().draw();
		getUILayer().draw();
		
		if (DEBUG_TABLES)
			Table.drawDebug(getUILayer());

		if (DEBUG_FPS)
			System.out.println(TIMER.getTime() - start + " milliseconds ");
		pop.render();
		super.setWaitTimeUI(world);
		super.setCarsUI(world);
		
		
	}
	

	boolean exported = false;

	@Override
	public void populateUILayer() {

	}

	@Override
	public void populateWorldLayer() {

	}

}