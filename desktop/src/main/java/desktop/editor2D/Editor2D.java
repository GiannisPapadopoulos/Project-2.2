package desktop.editor2D;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import editor.Editor2Dgame;

public class Editor2D {

	public Editor2D() {
		
	}
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.height = 800;
		config.width = 1500;
		config.foregroundFPS = 10;
		new LwjglApplication(new Editor2Dgame(), config);
	}
	
}
