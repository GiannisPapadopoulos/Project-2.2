package trafficsim.screens;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

@Getter
@Setter
public abstract class SuperScreen implements Screen {
	
	private Screens screens;

	private OrthographicCamera camera;

	
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
