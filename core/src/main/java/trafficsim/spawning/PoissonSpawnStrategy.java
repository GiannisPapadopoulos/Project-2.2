package trafficsim.spawning;

import static trafficsim.TrafficSimConstants.RANDOM;
import static trafficsim.TrafficSimConstants.TIMER;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ExponentialDistribution;
import org.apache.commons.math.distribution.ExponentialDistributionImpl;

public class PoissonSpawnStrategy
		extends AbstractSpawnStrategy {

	private ExponentialDistribution expDistr;
	double nextSpawnTime;

	public PoissonSpawnStrategy(double mean) {
		super(mean);
		expDistr = new ExponentialDistributionImpl(mean);
	}

	@Override
	public boolean shouldSpawn(float currentTime) {
		return currentTime > nextSpawnTime;
	}

	@Override
	public void setInterval(double interval) {
		super.setInterval(interval);
		expDistr = new ExponentialDistributionImpl(interval);
		double u = RANDOM.nextDouble();
		try {
			nextSpawnTime = TIMER.getTime() + expDistr.inverseCumulativeProbability(u);
		}
		catch (MathException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void spawned(float currentTime) {
		double u = RANDOM.nextDouble();
		try {
			nextSpawnTime = currentTime + expDistr.inverseCumulativeProbability(u);
		}
		catch (MathException e) {
			e.printStackTrace();
		}
	}

}
