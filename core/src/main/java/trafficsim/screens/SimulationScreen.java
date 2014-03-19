package trafficsim.screens;

import static trafficsim.TrafficSimConstants.DEBUG_RENDER;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import static trafficsim.TrafficSimConstants.TIMER;
import static trafficsim.TrafficSimConstants.WINDOW_HEIGHT;
import static trafficsim.TrafficSimConstants.WINDOW_WIDTH;
import static trafficsim.TrafficSimConstants.WORLD_TO_BOX;
import graph.Graph;
import graph.GraphFactory;
import trafficsim.TrafficSimWorld;
import trafficsim.components.RouteComponent;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.Road;
import trafficsim.systems.DestinationSystem;
import trafficsim.systems.InputSystem;
import trafficsim.systems.MovementSystem;
import trafficsim.systems.PathFindingSystem;
import trafficsim.systems.PhysicsSystem;
import trafficsim.systems.RenderSystem;
import trafficsim.systems.SpawnSystem;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * The main screen of the simulation
 * 
 * @author Giannis Papadopoulos
 * 
 */
public class SimulationScreen
		implements Screen {

	private TrafficSimWorld world;
	private OrthographicCamera camera;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true, false, false, false, true, true);

	public SimulationScreen() {
		world = new TrafficSimWorld();
		camera = new OrthographicCamera(WINDOW_WIDTH * WORLD_TO_BOX, WINDOW_HEIGHT * WORLD_TO_BOX);

		// Add systems
		world.setSystem(new RenderSystem(camera));
		world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new InputSystem(camera));
		world.setSystem(new PathFindingSystem());
		world.setSystem(new DestinationSystem());
		world.setSystem(new SpawnSystem());

		world.initialize();

		Graph<Road> graph = GraphFactory.createManhattanGraph(6, 5, 60, 0, 0);
		EntityFactory.populateWorld(world, graph);

		Entity car = EntityFactory.createCar(world, new Vector2(0, -LANE_WIDTH / 2), 1f, 30, 0, "car4");
		car.addComponent(new RouteComponent(graph.getVertex(0), graph.getVertex(graph.getVertexCount() - 1)));
		car.addToWorld();

		Entity car2 = EntityFactory.createCar(	world, new Vector2(303, 240 + LANE_WIDTH / 2), 1f, 30, MathUtils.PI,
												"car4");
		car2.addComponent(new RouteComponent(graph.getVertex(graph.getVertexCount() - 1), graph.getVertex(0)));
		car2.addToWorld();

		GraphFactory.addSpawnPointsTest(world, graph);

		TIMER.start();
	}



	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		world.setDelta(delta);

		world.process();
		if (DEBUG_RENDER)
			debugRenderer.render(world.getBox2dWorld(), camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}
