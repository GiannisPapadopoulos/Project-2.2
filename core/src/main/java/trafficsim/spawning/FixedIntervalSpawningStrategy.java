package trafficsim.spawning;

import lombok.Getter;
import lombok.Setter;

@Getter
public class FixedIntervalSpawningStrategy
		extends AbstractSpawnStrategy {

	private float interval;

	@Setter
	private float lastSpawnTime;

	public FixedIntervalSpawningStrategy(float interval) {
		this.interval = interval;
	}

	@Override
	public boolean shouldSpawn(float currentTime) {
		return currentTime - lastSpawnTime >= interval || lastSpawnTime == 0;
	}

	@Override
	public void spawned(float currentTime) {
		this.lastSpawnTime = currentTime;
	}

}
