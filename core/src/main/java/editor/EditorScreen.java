package editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class EditorScreen implements Screen {

	private Game editorGame;
	private EditorData editorData;
	private Stage outputScreen;
	private WorldRenderer renderer;
	private Group g;

	private static final double ZOOMING_FACTOR = 10;
	private static final float TRANSLATION_FACTOR = 50f;

	public EditorScreen(Game editorGame, EditorData editorData) {
		this.editorGame = editorGame;
		this.editorData = editorData;
		this.g = new Group();

		renderer = new WorldRenderer(editorData);

	}

	@Override
	public void dispose() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		outputScreen.act(delta);
		outputScreen.draw();
		outputScreen.getCamera().update();

		renderer.render();
		SLEEP(10);
		
	}

	private void SLEEP(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void resize(int width, int height) {
		outputScreen.setViewport(width, height, true);
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
		outputScreen = new Stage();
		Gdx.input.setInputProcessor(outputScreen);
		outputScreen.addListener(new InputListener() {
			OrthographicCamera cam = (OrthographicCamera) outputScreen
					.getCamera();

			float previousDragX;
			float previousDragY;

			@Override
			public boolean scrolled(InputEvent event, float x, float y,
					int amount) {
				System.out.println("Scrolled");
				if (cam.zoom + amount > 0) {
					cam.zoom += amount * ZOOMING_FACTOR;
					cam.update();
				}
				return false;
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				System.out.println("TouchDown");
				previousDragX = x;
				previousDragY = y;
				return true;
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				System.out.println("TouchDragged");
				cam.translate((previousDragX - x) * TRANSLATION_FACTOR,
						(previousDragY - y) * TRANSLATION_FACTOR);
				previousDragX = x;
				previousDragY = y;
				cam.update();
			}
		});

		System.out.println("SHOW");
	}

}
