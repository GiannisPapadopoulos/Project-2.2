package trafficsim;

import trafficsim.screens.EditorScreen;
import trafficsim.screens.Screens;
import trafficsim.screens.SimulationScreen;
import trafficsim.screens.StatisticsScreen;

import com.badlogic.gdx.Game;

/** The application class, delegates everything to the active screen */
public class TrafficSimulation extends Game {

	@Override
	public void create() {
		Screens screens = new Screens();
		
		SimulationScreen sims = new SimulationScreen(screens);
		EditorScreen edis = new EditorScreen(screens);
		StatisticsScreen stas = new StatisticsScreen(screens);

		screens.setSimulationScreen(sims);
		screens.setEditorScreen(edis);
		screens.setStatisticsScreen(stas);
		screens.setTrafficSimulation(this);
		
		sims.switchToScreen(sims);
	}

}
