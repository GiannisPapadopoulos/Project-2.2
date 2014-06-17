package trafficsim.experiments;

import java.util.ArrayList;
import java.util.List;

import trafficsim.experiments.SimulationParameters.ManhattanGraphInfo;
import trafficsim.experiments.SimulationParameters.SpawnInfo;
import trafficsim.spawning.AbstractSpawnStrategy.SpawnStrategyType;
import trafficsim.systems.AbstractToggleStrategy;

public class PredefinedParameters {

	public static SimulationParameters timedLightsSimpleGraph;

	public static SimulationParameters prioritydLightsSimpleGraph;

	public static SimulationParameters roundaboutSimpleGraph;

	public static SimulationParameters priorityLightsmanhattanGraph;

	static {
		createParameters();
	}

	public static void createParameters() {
		List<SpawnInfo> noSpawnPoints = new ArrayList<SpawnInfo>();
		List<SpawnInfo> spawnInfo = new ArrayList<SpawnInfo>();
		double[] intervals = { 2000, 2000, 2000, 2000 };
		for (int i = 1; i < 5; i++) {
			spawnInfo.add(new SpawnInfo(i, intervals[i - 1], SpawnStrategyType.POISSON));
		}
		float totalTime = 60 * 3;
		timedLightsSimpleGraph = new SimulationParameters(false, false, null, 0,
															AbstractToggleStrategy.basicToggleStrategy, spawnInfo,
															totalTime);

		prioritydLightsSimpleGraph = new SimulationParameters(false, false, null, 0,
																AbstractToggleStrategy.priorityToggleStrategy,
																spawnInfo, totalTime);
		roundaboutSimpleGraph = new SimulationParameters(false, true, null, 0,
															AbstractToggleStrategy.priorityToggleStrategy, spawnInfo,
															totalTime);
		ManhattanGraphInfo graphInfo = new ManhattanGraphInfo(6, 6, 100f);
		priorityLightsmanhattanGraph = new SimulationParameters(true, false, graphInfo, 0,
																AbstractToggleStrategy.priorityToggleStrategy,
																noSpawnPoints, totalTime);
	}
	

}
