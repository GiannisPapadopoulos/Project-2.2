package trafficsim.systems;

import static trafficsim.TrafficSimConstants.WINDOW_HEIGHT;
import static trafficsim.TrafficSimConstants.WINDOW_WIDTH;
import trafficsim.TrafficSimWorld;
import trafficsim.callbacks.FindBodyQueryCallback;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.screens.SuperScreen;

import com.artemis.Entity;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import editor.MousePosition.Coordinates;

/** System to control the camera and other input commands we might define */
public class InputSystem extends VoidEntitySystem implements InputProcessor {

	private SuperScreen superScreen;
	private OrthographicCamera camera;

	private static final double ZOOMING_FACTOR1 = 0.05f;
	private static final double ZOOMING_FACTOR2 = .5f;
	private static final float TRANSLATION_FACTOR = 0.15f;

	protected float previousDragX;
	protected float previousDragY;

	public InputSystem(SuperScreen superScreen) {
		this.superScreen = superScreen;
		this.camera = superScreen.getCamera();
	}

	@Override
	protected void processSystem() {

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
		if (button == Buttons.RIGHT) {
			superScreen.getMousePosition().update(camera);
			World box2dWorld = ((TrafficSimWorld) world).getBox2dWorld();
			Coordinates coords = superScreen.getMousePosition().getAbsolute();

			float boxSize = 1f;

			FindBodyQueryCallback callBack = new FindBodyQueryCallback(new Vector2(coords.getX(), coords.getY()));
			box2dWorld.QueryAABB(	callBack, coords.getX() - boxSize, coords.getY() - boxSize,
									coords.getX() + boxSize,
									coords.getY() + boxSize);
			if (callBack.foundSomething()) {
				Entity car = world.getEntity(callBack.getClosestId());
				PhysicsBodyComponent physComp = car.getComponent(PhysicsBodyComponent.class);
				System.out.println("Found something " + car + " id " + callBack.getClosestId() + " "
									+ physComp.getPosition() + "\n");
			}
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 vec = new Vector3(screenX, screenY, 0);
		camera.unproject(vec, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		//System.out.println(screenX + " " + screenY + " v " + vec);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camera.translate((previousDragX - screenX) * TRANSLATION_FACTOR,
				-(previousDragY - screenY) * TRANSLATION_FACTOR);
		previousDragX = screenX;
		previousDragY = screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		double ZOOMING_FACTOR = camera.zoom <= 1 ? ZOOMING_FACTOR1
				: ZOOMING_FACTOR2;
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
