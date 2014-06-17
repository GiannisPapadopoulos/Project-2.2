package desktop;

import static trafficsim.TrafficSimConstants.*;
import trafficsim.TrafficSimulation;
import trafficsim.screens.SimulationScreen;
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
		SimulationScreen.application = new LwjglApplication(new TrafficSimulation(), config);
	}
}
