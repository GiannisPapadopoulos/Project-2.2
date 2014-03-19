package trafficsim.spawning;

/**
 * 
 * @author Giannis Papadopoulos
 */
public abstract class AbstractSpawnStrategy {

	public abstract boolean shouldSpawn(float currentTime);

	// used to update
	public abstract void spawned(float currentTime);

}
