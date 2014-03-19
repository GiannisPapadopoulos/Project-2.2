package trafficsim.screens;

import lombok.Data;

import com.badlogic.gdx.Game;

@Data
public class Screens {

	private Game trafficSimulation;
	// Screen used to view traffic simulation
	private SimulationScreen SimulationScreen;
	// Screen used to edit traffic world
	private EditorScreen EditorScreen;
	// Screen used later for things like traffic heat maps etc...
	private StatisticsScreen StatisticsScreen;

}
