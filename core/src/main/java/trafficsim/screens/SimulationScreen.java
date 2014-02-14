package trafficsim.screens;

import static trafficsim.TrafficSimConstants.WINDOW_HEIGHT;
import static trafficsim.TrafficSimConstants.WINDOW_WIDTH;
import trafficsim.TrafficSimWorld;
import trafficsim.factories.EntityFactory;
import trafficsim.systems.InputSystem;
import trafficsim.systems.MovementSystem;
import trafficsim.systems.PhysicsSystem;
import trafficsim.systems.RenderSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

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

	public SimulationScreen() {
		world = new TrafficSimWorld();
		camera = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);

		// Add systems
		world.setSystem(new RenderSystem(camera));
		world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new InputSystem(camera));

		world.initialize();

		EntityFactory.createCar(world, new Vector2(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2), 20f, "red-car").addToWorld();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		world.setDelta(delta);

		world.process();
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
