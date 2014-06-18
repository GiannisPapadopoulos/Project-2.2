package trafficsim.experiments;

import gnu.trove.list.array.TIntArrayList;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import trafficsim.spawning.AbstractSpawnStrategy.SpawnStrategyType;
import trafficsim.systems.AbstractToggleStrategy;

@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimulationParameters {

	boolean ManhattanGraph;

	/** For the 5-vertex graph */
	boolean roundabout;

	/** For manhattan graphs */
	ManhattanGraphInfo graphInfo;

	/** City speed limit, should add one for highway */
	float speedLimit;

	/** Traffic light toggle strategy */
	AbstractToggleStrategy toggleStrategy;

	/** Spawn-component will be added to those vertices */
	List<SpawnInfo> spawnpoints;

	/** How long the simulation will be run for */
	float totalTimeInSecs;

	/** Greenmile */
	private GreenWaveInfo greenWaveInfo;

	private float greenTimer;


	@AllArgsConstructor
	@Getter
	/** Contains the id of a vertex that will be a spawnpoint and the spawnRate */
	public static class SpawnInfo {

		private int vertexID;
		/** In milliseconds */
		@Setter
		private double spawnInterval;
		/** Poisson or uniform distribution */
		@Setter
		private SpawnStrategyType strategy;
	}

	/** Used to specify a manhattan graph */
	@AllArgsConstructor
	@Getter
	public static class ManhattanGraphInfo {
		
		private int width;
		private int height;
		private float laneLength;
	}

	/** Vertex ids for greenMile */
	@AllArgsConstructor
	@Getter
	public static class GreenWaveInfo {
		@Delegate
		private TIntArrayList vertexIDlist;
	}
}
