package trafficsim;

import trafficsim.screens.SimulationScreen;

import com.badlogic.gdx.Game;

/** The application class, delegates everything to the active screen */
public class TrafficSimulation
		extends Game {

	@Override
	public void create() {
		setScreen(new SimulationScreen());
	}


}
