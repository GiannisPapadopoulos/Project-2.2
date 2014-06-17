package trafficsim.experiments;

import static trafficsim.TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT;

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
		int size = 15; // of manhattan graph
		ManhattanGraphInfo graphInfo = new ManhattanGraphInfo(size, size, 100f);
		int[] indices = { 0, size * size - 1, (int) Math.sqrt(size * size - 1),
							size * size - (int) Math.sqrt(size * size) };
		List<SpawnInfo> noSpawnPoints = new ArrayList<SpawnInfo>();
		List<SpawnInfo> spawnInfo = new ArrayList<SpawnInfo>();
		List<SpawnInfo> manhattanSpawnInfo = new ArrayList<SpawnInfo>();
		double[] intervals = { 2000, 2000, 2000, 2000 };
		for (int i = 0; i < 4; i++) {
			spawnInfo.add(new SpawnInfo(i + 1, intervals[i], SpawnStrategyType.POISSON));
			manhattanSpawnInfo.add(new SpawnInfo(indices[i], intervals[i], SpawnStrategyType.POISSON));
		}
		float totalTime = 60 * 3;
		timedLightsSimpleGraph = new SimulationParameters(false, false, null, DEFAULT_CITY_SPEED_LIMIT,
															AbstractToggleStrategy.basicToggleStrategy, spawnInfo,
															totalTime);

		prioritydLightsSimpleGraph = new SimulationParameters(false, false, null, DEFAULT_CITY_SPEED_LIMIT,
																AbstractToggleStrategy.priorityToggleStrategy,
																spawnInfo, totalTime);
		roundaboutSimpleGraph = new SimulationParameters(false, true, null, DEFAULT_CITY_SPEED_LIMIT,
															AbstractToggleStrategy.priorityToggleStrategy, spawnInfo,
															totalTime);
		priorityLightsmanhattanGraph = new SimulationParameters(true, false, graphInfo, DEFAULT_CITY_SPEED_LIMIT,
																AbstractToggleStrategy.priorityToggleStrategy,
																manhattanSpawnInfo, totalTime);
	}
	

}
