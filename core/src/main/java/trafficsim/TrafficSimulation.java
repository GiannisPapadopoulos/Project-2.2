package trafficsim;

import trafficsim.screens.EditorScreen;
import trafficsim.screens.Screens;
import trafficsim.screens.SimulationScreen;
import trafficsim.screens.StatisticsScreen;

import com.badlogic.gdx.Game;

/** The application class, delegates everything to the active screen */
public class TrafficSimulation
		extends Game {

	@Override
	public void create() {
		Screens screens = new Screens();
		screens = new Screens(this, new SimulationScreen(screens), new EditorScreen(screens), new StatisticsScreen(screens));
		setScreen(screens.getSimulationScreen());
	}


}
