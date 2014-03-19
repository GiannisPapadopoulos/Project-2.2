package trafficsim.screens;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

@Data
@AllArgsConstructor
public class Screens {
	
	public Screens() {}
	
	private Game trafficSimulation;
	// Screen used to view traffic simulation
	private Screen SimulationScreen;
	// Screen used to edit traffic world
	private Screen EditorScreen;
	// Screen used later for things like traffic heat maps etc... 
	private Screen StatisticsScreen; 

}
