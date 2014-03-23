package trafficsim.screens;

import static trafficsim.TrafficSimConstants.DEBUG_FPS;
import static trafficsim.TrafficSimConstants.DEBUG_RENDER;
import static trafficsim.TrafficSimConstants.TIMER;
import graph.Graph;
import graph.GraphFactory;
import lombok.Getter;
import lombok.Setter;
import trafficsim.TrafficSimWorld;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.Road;
import trafficsim.systems.DestinationSystem;
import trafficsim.systems.MovementSystem;
import trafficsim.systems.PathFindingSystem;
import trafficsim.systems.PhysicsSystem;
import trafficsim.systems.RenderSystem;
import trafficsim.systems.SpawnSystem;
import trafficsim.systems.TrafficLightSystem;

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

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true,
			false, false, false, true, true);

	public SimulationScreen(Screens screens) {
		super(screens);

	}

	@Override
	public void show() {

		world = new TrafficSimWorld();

		// Add systems
		world.setSystem(new RenderSystem(getCamera()));
		world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new PathFindingSystem());
		world.setSystem(new DestinationSystem());
		world.setSystem(new SpawnSystem());
		world.setSystem(new TrafficLightSystem());

		world.initialize();

		Graph<Road> graph = GraphFactory.createManhattanGraph(6, 5, 60, 0, 0);
		world.setGraph(graph);
		EntityFactory.populateWorld(world, graph);

		// Entity car = EntityFactory.createCar(world, new Vector2(0,
		// -LANE_WIDTH / 2), 1f, 30, 0, "car4");
		// car.addComponent(new RouteComponent(graph.getVertex(0),
		// graph.getVertex(graph.getVertexCount() - 1)));
		// car.addToWorld();
		//
		// Entity car2 = EntityFactory.createCar( world, new Vector2(303, 240 +
		// LANE_WIDTH / 2), 1f, 30, MathUtils.PI,
		// "car4");
		// car2.addComponent(new
		// RouteComponent(graph.getVertex(graph.getVertexCount() - 1),
		// graph.getVertex(0)));
		// car2.addToWorld();

		// Entity car3 = EntityFactory.createCar(world, new Vector2(0, 240 -
		// LANE_WIDTH / 2), 1f, 30, 0, "car4");
		// car3.addComponent(new RouteComponent(graph.getVertex(4),
		// graph.getVertex(25)));
		// car3.addToWorld();

		GraphFactory.addSpawnPointsTest(world, graph);
		EntityFactory.addTrafficLights(world, graph);
		if (!TIMER.isStarted())
			TIMER.start();

		world.process();

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