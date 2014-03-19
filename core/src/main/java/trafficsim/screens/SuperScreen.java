package trafficsim.screens;

import lombok.Data;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

@Data
public abstract class SuperScreen implements Screen {
	
	Screens screens;
	
	private OrthographicCamera camera;
	
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true, false,
			false, false, true, true);


	
	public SuperScreen(Screens screens) {
		this.screens = screens;
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
