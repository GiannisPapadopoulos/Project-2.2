package trafficsim.screens;

import static trafficsim.TrafficSimConstants.WINDOW_HEIGHT;
import static trafficsim.TrafficSimConstants.WINDOW_WIDTH;
import trafficsim.TrafficSimWorld;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.factories.EntityFactory;
import trafficsim.systems.InputSystem;
import trafficsim.systems.MovementSystem;
import trafficsim.systems.PhysicsSystem;
import trafficsim.systems.RenderSystem;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true, false, false, false, true, false);

	public SimulationScreen() {
		world = new TrafficSimWorld();
		camera = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);

		// Add systems
		world.setSystem(new RenderSystem(camera));
		world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new InputSystem(camera));

		world.initialize();


		EntityFactory.createRoad(world, new Vector2(0, 0), 60 * 10, false, "road1x1").addToWorld();
		EntityFactory.createRoad(world, new Vector2(0, -200), 60 * 10, false, "road1x1").addToWorld();
		EntityFactory.createRoad(world, new Vector2(330, 70), 60 * 10, true, "road1x1").addToWorld();
		EntityFactory.createCar(world, new Vector2(-270, -215), 20f, "red-car").addToWorld();
		EntityFactory.createCar(world, new Vector2(-200, -215), 20f, "red-car").addToWorld();
		Entity carV = EntityFactory.createCar(world, new Vector2(-100, 15), 20f, 0, "red-car");
		carV.addToWorld();

		Entity car = EntityFactory.createCar(world, new Vector2(280, -215), 20f, "red-car");
		car.getComponent(PhysicsBodyComponent.class).setLinearVelocity(40, 0);
		car.addToWorld();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		world.setDelta(delta);

		world.process();
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
