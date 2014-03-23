package trafficsim.systems;

import trafficsim.screens.EditorScreen;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class InputEditorSystem extends InputSystem {

	private EditorScreen editorScreen;

	// Used to distinguish between drag and click of mouse
	private int x_val, y_val;

	public InputEditorSystem(OrthographicCamera camera,
			EditorScreen editorScreen) {
		super(editorScreen);
		this.editorScreen = editorScreen;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		previousDragX = screenX;
		previousDragY = screenY;
		x_val = screenX;
		y_val = screenY;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (x_val == screenX && y_val == screenY)
			editorScreen.notifyEditorClicked(button);
		previousDragX = screenX;
		previousDragY = screenY;

		return true;
	}

}
