package trafficsim.experiments;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import trafficsim.spawning.AbstractSpawnStrategy.SpawnStrategyType;
import trafficsim.systems.AbstractToggleStrategy;

@AllArgsConstructor
@Getter
@Setter
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
}
