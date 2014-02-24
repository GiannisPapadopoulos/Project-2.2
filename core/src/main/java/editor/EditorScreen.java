package editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
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

	
	private int verticalLeft = 0,verticalRight = 75,horizontalTop = 40,horizontalBottom = 0;
	
	private static final double ZOOMING_FACTOR = 0.1f;
	private static final float TRANSLATION_FACTOR = 0.5f;

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

		renderer.render((OrthographicCamera) outputScreen.getCamera());
		
	}

	public void SLEEP(int time) {
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
				/*if(zoom out)
				 * horizontalBottom = 
				 * 
				 */
				
				Vector3 botLeft = new Vector3(0,cam.viewportHeight,0);
				Vector3 topRight = new Vector3(cam.viewportWidth, 0, 0);
				
				System.out.format("before unproject %s  -  %s \r\n", botLeft, topRight);
				
				cam.unproject(botLeft);
				cam.unproject(topRight);
				
				System.out.format("after unproject %s  -  %s \r\n", botLeft, topRight);
				
				botLeft.scl(1f / WorldRenderer.CELL_SIZE);
				topRight.scl(1f / WorldRenderer.CELL_SIZE);
				
				System.out.format("after grid transform %s  -  %s \r\n", botLeft, topRight);
				
				// Ensure we don't count the black stuff to the left and bottom
				for(Vector3 vec : new Vector3[]{botLeft, topRight}){
					vec.x = Math.max(0, vec.x);
					vec.y = Math.max(0, vec.y);
				}
				
				// Ensure we don't count the black stuff to the right and top
				for(Vector3 vec : new Vector3[]{botLeft, topRight}){
					vec.x = Math.min(editorData.getWidth(), vec.x);
					vec.y = Math.min(editorData.getHeight(), vec.y);
				}
				
				// Ensure we correctly count the boxes
				topRight.sub(botLeft);
				
				int totBoxWScreen = (int) topRight.x;
				int totBoxHScreen = (int) topRight.y;
				
				System.out.format("Boxes displayed on screen (width & height): %d-%d, total: %d \r\n", totBoxWScreen, totBoxHScreen, totBoxWScreen * totBoxHScreen);
				
				System.out.println(cam.zoom);
				if (cam.zoom + amount > 0) {
					cam.zoom += amount * ZOOMING_FACTOR;
				}
				return false;
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {		
				previousDragX = x;
				previousDragY = y;
				return true;
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				cam.translate((previousDragX - x) * TRANSLATION_FACTOR,
						(previousDragY - y) * TRANSLATION_FACTOR);
				previousDragX = x;
				previousDragY = y;
			}
		});

		System.out.println("SHOW");
	}

}
