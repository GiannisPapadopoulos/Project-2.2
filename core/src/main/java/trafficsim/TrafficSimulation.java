package trafficsim;

import trafficsim.experiments.RepeatedExperiment;
import trafficsim.experiments.SimulationParameters;
import trafficsim.screens.EditorScreen;
import trafficsim.screens.Screens;
import trafficsim.screens.SimulationScreen;
import trafficsim.screens.StatisticsScreen;

import com.badlogic.gdx.Game;

/** The application class, delegates everything to the active screen */
public class TrafficSimulation extends Game {

	private SimulationParameters parameters;

	private RepeatedExperiment experiment;

	private SimulationScreen simScreen;

	public TrafficSimulation() {
	}

	public TrafficSimulation(SimulationScreen simScreen) {
		this.simScreen = simScreen;
	}

	public TrafficSimulation(SimulationParameters parameters) {
		this.parameters = parameters;
	}

	public TrafficSimulation(RepeatedExperiment experiment) {
		this.experiment = experiment;
	}

	@Override
	public void create() {
		Screens screens = new Screens();
		
		SimulationScreen sims;
		if (simScreen != null) {
			sims = simScreen;
		}
		if (experiment != null) {
			sims = new SimulationScreen(screens, experiment);
			experiment.setSimScreen(sims);
		}
		else if (parameters == null)
			sims = new SimulationScreen(screens);
		else {
			sims = new SimulationScreen(screens, parameters);
		}
		EditorScreen edis = new EditorScreen(screens);
		StatisticsScreen stas = new StatisticsScreen(screens);

		screens.setSimulationScreen(sims);
		screens.setEditorScreen(edis);
		screens.setStatisticsScreen(stas);
		screens.setTrafficSimulation(this);
		
		sims.switchToScreen(sims);
	}

}
