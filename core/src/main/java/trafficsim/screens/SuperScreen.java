package trafficsim.screens;

import com.badlogic.gdx.Screen;

public abstract class SuperScreen implements Screen {
	
	Screens screens;

	
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
