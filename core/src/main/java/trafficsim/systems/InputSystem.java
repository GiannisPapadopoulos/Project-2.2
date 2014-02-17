package trafficsim.systems;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/** System to control the camera and other input commands we might define */
public class InputSystem extends VoidEntitySystem implements InputProcessor {
	
	private OrthographicCamera camera;
	
	public InputSystem(OrthographicCamera camera) {
		this.camera = camera;
	}

	@Override
	protected void initialize() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	protected void processSystem() {
		// The position of the mouse
		Vector3 mouseVector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
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
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// System.out.println(screenX + " " + screenY);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO zoom in or out
		return false;
	}



}
