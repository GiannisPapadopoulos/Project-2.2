package desktop;

import static trafficsim.TrafficSimConstants.FPS;
import static trafficsim.TrafficSimConstants.PACK;
import static trafficsim.TrafficSimConstants.WINDOW_HEIGHT;
import static trafficsim.TrafficSimConstants.WINDOW_WIDTH;
import trafficsim.TrafficSimulation;
import utils.ImagePacker;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

	public static void main(String[] args) {
		
		if (PACK)
			ImagePacker.run();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		config.foregroundFPS = config.backgroundFPS = FPS;
		new LwjglApplication(new TrafficSimulation(), config);
	}
}
