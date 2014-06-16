package trafficsim.spawning;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Giannis Papadopoulos
 */
public abstract class AbstractSpawnStrategy {

	@Getter
	@Setter
	protected double interval;

	public AbstractSpawnStrategy(double interval) {
		this.interval = interval;
	}

	public abstract boolean shouldSpawn(float currentTime);

	// used to update
	public abstract void spawned(float currentTime);

}
