package trafficsim.experiments;

import static trafficsim.TrafficSimConstants.DEFAULT_CITY_SPEED_LIMIT;
import gnu.trove.list.array.TIntArrayList;

import java.util.ArrayList;
import java.util.List;

import trafficsim.experiments.SimulationParameters.GreenWaveInfo;
import trafficsim.experiments.SimulationParameters.ManhattanGraphInfo;
import trafficsim.experiments.SimulationParameters.SpawnInfo;
import trafficsim.spawning.AbstractSpawnStrategy.SpawnStrategyType;
import trafficsim.systems.AbstractToggleStrategy;

public class PredefinedParameters {

	public static SimulationParameters timedLightsSimpleGraph;

	public static SimulationParameters prioritydLightsSimpleGraph;

	public static SimulationParameters roundaboutSimpleGraph;

	public static SimulationParameters priorityLightsmanhattanGraph;

	public static SimulationParameters timedLightsmanhattanGraph;

	public static SimulationParameters greenWaveManhattanGraph;

	static {
		createParameters();
	}

	public static void createParameters() {
		int size = 6; // of manhattan graph
		ManhattanGraphInfo graphInfo = new ManhattanGraphInfo(size, size, 100f);
		TIntArrayList greenMileVertices = new TIntArrayList();
		for (int i = 0; i < size; i++) {
			greenMileVertices.add(i * size);
		}
		GreenWaveInfo gwInfo = new GreenWaveInfo(greenMileVertices);
		int vertexCount = size * size;
		int[] indices = { vertexCount, vertexCount + 1, vertexCount + 2, vertexCount + 3 };
		List<SpawnInfo> noSpawnPoints = new ArrayList<SpawnInfo>();
		List<SpawnInfo> spawnInfo = new ArrayList<SpawnInfo>();
		List<SpawnInfo> manhattanSpawnInfo = new ArrayList<SpawnInfo>();
		double[] intervals = { 2000, 2000, 2000, 2000 };
		for (int i = 0; i < 4; i++) {
			spawnInfo.add(new SpawnInfo(i + 1, intervals[i], SpawnStrategyType.POISSON));
			manhattanSpawnInfo.add(new SpawnInfo(indices[i], intervals[i], SpawnStrategyType.POISSON));
		}
		float totalTime = 60 * 3;
		float manhattanTime = 60 * 5;
		float defaultGreenInterval = 5;
		float longGreenInterval = 15;
		timedLightsSimpleGraph = new SimulationParameters(false, false, null, DEFAULT_CITY_SPEED_LIMIT,
															AbstractToggleStrategy.basicToggleStrategy, spawnInfo,
															totalTime, null, defaultGreenInterval);

		prioritydLightsSimpleGraph = new SimulationParameters(false, false, null, DEFAULT_CITY_SPEED_LIMIT,
																AbstractToggleStrategy.priorityToggleStrategy,
																spawnInfo, totalTime, null, defaultGreenInterval);
		roundaboutSimpleGraph = new SimulationParameters(false, true, null, DEFAULT_CITY_SPEED_LIMIT,
															AbstractToggleStrategy.priorityToggleStrategy, spawnInfo,
															totalTime, null, defaultGreenInterval);
		priorityLightsmanhattanGraph = new SimulationParameters(true, false, graphInfo, DEFAULT_CITY_SPEED_LIMIT,
																AbstractToggleStrategy.priorityToggleStrategy,
																manhattanSpawnInfo, manhattanTime, null,
																defaultGreenInterval);
		timedLightsmanhattanGraph = new SimulationParameters(true, false, graphInfo, DEFAULT_CITY_SPEED_LIMIT,
																AbstractToggleStrategy.basicToggleStrategy,
																manhattanSpawnInfo, manhattanTime, null,
																defaultGreenInterval);
		greenWaveManhattanGraph = new SimulationParameters(true, false, graphInfo, DEFAULT_CITY_SPEED_LIMIT,
															AbstractToggleStrategy.basicToggleStrategy,
															manhattanSpawnInfo, manhattanTime, gwInfo,
															longGreenInterval);
	}
	

}
