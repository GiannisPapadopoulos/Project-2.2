package editor;

import lombok.val;

import com.badlogic.gdx.Game;

public class Editor2Dgame extends Game {

	@Override
	public void create() {
		val currentScreen = new EditorScreen(this, new EditorData(100,100));
		setScreen(currentScreen);
	}
	@Override
	public void dispose() {
		super.dispose();	
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int arg0, int arg1) {
		super.resize(arg0,arg1);
	}

	@Override
	public void resume() {
		super.resume();
	}
}
