package trafficsim.spawning;

import static trafficsim.TrafficSimConstants.RANDOM;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ExponentialDistribution;
import org.apache.commons.math.distribution.ExponentialDistributionImpl;

public class PoissonSpawnStrategy
		extends AbstractSpawnStrategy {

	private ExponentialDistribution expDistr;
	double nextSpawnTime;

	public PoissonSpawnStrategy(double lambda) {
		expDistr = new ExponentialDistributionImpl(lambda);
	}

	@Override
	public boolean shouldSpawn(float currentTime) {

		return currentTime > nextSpawnTime;
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
