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
		int size = 15; // of manhattan graph
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
		int spawnRate = 3 * 1000;
		double[] intervals = { spawnRate, spawnRate, spawnRate, spawnRate };
		for (int i = 0; i < 4; i++) {
			spawnInfo.add(new SpawnInfo(i + 1, intervals[i], SpawnStrategyType.POISSON));
			manhattanSpawnInfo.add(new SpawnInfo(indices[i], intervals[i], SpawnStrategyType.POISSON));
		}
		float totalTime = 60 * 5;
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
	
	public static void createParametersDense() {
		int size = 15; // of manhattan graph
		ManhattanGraphInfo graphInfo = new ManhattanGraphInfo(size, size, 100f);
		
		int[] indices;
		double[] intervals;
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int index =0 ; index<size*size;index++) {
			if(Math.random()<0.05)
				indexes.add(index);
		}
		indices = new int[indexes.size()];
		intervals = new double[indexes.size()];
		for(Integer i:indexes) {
			indices[indexes.indexOf(i)] = i;
			intervals[indexes.indexOf(i)] = 2000;
		}
			
			
		List<SpawnInfo> noSpawnPoints = new ArrayList<SpawnInfo>();
		List<SpawnInfo> spawnInfo = new ArrayList<SpawnInfo>();
		List<SpawnInfo> manhattanSpawnInfo = new ArrayList<SpawnInfo>();
		
		for (int i = 0; i < indices.length; i++) {
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
