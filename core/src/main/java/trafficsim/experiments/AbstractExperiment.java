package trafficsim.experiments;

import static trafficsim.TrafficSimConstants.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import trafficsim.TrafficSimWorld;
import trafficsim.TrafficSimulation;
import trafficsim.screens.SimulationScreen;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Experiment interface
 * 
 * @author Giannis Papadopoulos
 */
@Getter
@AllArgsConstructor
public abstract class AbstractExperiment {

	private SimulationParameters parameters;

	public abstract ExperimentData getGatheredData(TrafficSimWorld world);

	public void initialize() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		config.foregroundFPS = config.backgroundFPS = FPS;
		SimulationScreen.application = new LwjglApplication(new TrafficSimulation(parameters), config);
	}

}
