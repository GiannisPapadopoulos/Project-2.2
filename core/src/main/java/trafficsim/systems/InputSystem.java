package trafficsim.systems;

import static trafficsim.TrafficSimConstants.WINDOW_HEIGHT;
import static trafficsim.TrafficSimConstants.WINDOW_WIDTH;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/** System to control the camera and other input commands we might define */
public class InputSystem
		extends VoidEntitySystem
		implements InputProcessor {
	
	private OrthographicCamera camera;
	
	private static final double ZOOMING_FACTOR1 = 0.1f;
	private static final double ZOOMING_FACTOR2 = 1f;
	private static final float TRANSLATION_FACTOR = 0.3f;

	private float previousDragX;
	private float previousDragY;

	public InputSystem(OrthographicCamera camera) {
		this.camera = camera;
	}

	@Override
	public void initialize() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	protected void processSystem() {
		// The position of the mouse
		// Vector3 mouseVector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		previousDragX = screenX;
		previousDragY = screenY;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 vec = new Vector3(screenX, screenY, 0);
		camera.unproject(vec, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		// System.out.println(screenX + " " + screenY + " v " + vec);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camera.translate((previousDragX - screenX) * TRANSLATION_FACTOR, -(previousDragY - screenY) * TRANSLATION_FACTOR);
		previousDragX = screenX;
		previousDragY = screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		double ZOOMING_FACTOR = camera.zoom <= 1 ? ZOOMING_FACTOR1 : ZOOMING_FACTOR2;
		if (camera.zoom + amount * ZOOMING_FACTOR > 0.05) {
			camera.zoom += amount * ZOOMING_FACTOR;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}



}
