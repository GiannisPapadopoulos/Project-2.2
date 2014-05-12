package trafficsim.screens;

import static functions.VectorUtils.getMidPoint;
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
import trafficsim.systems.MovementSystem;
import trafficsim.systems.PathFindingSystem;
import trafficsim.systems.PhysicsSystem;
import trafficsim.systems.RenderSystem;
import trafficsim.systems.RoutingSystem;
import trafficsim.systems.SpawnSystem;

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

	@Getter
	@Setter
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
		world.setSystem(new RenderSystem(getCamera()));
		// world.setSystem(new OldMovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new PathFindingSystem());
		world.setSystem(new DestinationSystem());
		world.setSystem(new SpawnSystem());
		// world.setSystem(new TrafficLightSystem());
		world.setSystem(new ExpirySystem());

		world.setSystem(new RoutingSystem());
		world.setSystem(new MovementSystem());

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
		List<Entity> vertexEntities = EntityFactory.populateWorld(world, graph);
		
		firstTimeSimulationRun = false;

		EntityFactory.addSpawnPoints(world, graph, vertexEntities);
		EntityFactory.addTrafficLights(world, world.getGraph(), vertexEntities);


		if (TIMER.isStarted())
			TIMER.reset();
		TIMER.start();
		// GraphFactory.addSpawnPointsTest(world, world.getGraph());
		world.process();


		System.out.println(getMidPoint(graph.getEdge(0).getData()));
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

		world.process();
		if (DEBUG_RENDER)
			debugRenderer.render(world.getBox2dWorld(), getCamera().combined);

		getWorldLayer().act(delta);
		getUILayer().act(delta);
		getWorldLayer().draw();
		getUILayer().draw();

		if (DEBUG_FPS)
			System.out.println(TIMER.getTime() - start + " milliseconds ");

		if (Math.abs(TIMER.getTime() / 1000.0 - 200) < 0.02) {
			System.out.println("Total time running " + TIMER.getTime() / 1000.0 + " cars spawned "
								+ world.getSystem(MovementSystem.class).getTotalCars());
			System.out.println(world.getDataGatherer());
		}
	}

	@Override
	public void populateUILayer() {

	}

	@Override
	public void populateWorldLayer() {

	}

}