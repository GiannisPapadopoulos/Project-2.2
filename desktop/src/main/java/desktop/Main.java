package desktop;

import static trafficsim.TrafficSimConstants.WINDOW_HEIGHT;
import static trafficsim.TrafficSimConstants.WINDOW_WIDTH;
import trafficsim.TrafficSimulation;
import utils.ImagePacker;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

	public static void main(String[] args) {
		
		ImagePacker.run();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		config.foregroundFPS = config.backgroundFPS = 60;
		new LwjglApplication(new TrafficSimulation(), config);

	}
}
