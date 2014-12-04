package utils;

import java.util.List;
import java.util.Random;

import lombok.Getter;


/**
 * A list of elements, with a probability assigned to each used for random sampling
 * The probabilities should sum up to 1
 * 
 * @author Giannis Papadopoulos
 */
@Getter
public class WeightedProbabilityList<E> {

	/** The elements of the weighted list */
	private List<E> elements;
	/** The probabilities in 1-1 correspondence with the elements */
	private List<Double> probabilities;

	private Random random = new Random();
	
	public WeightedProbabilityList(List<E> elements, List<Double> probabilities) {
		assert elements.size() == probabilities.size();
		assert epsilonEquals(sum(probabilities), 1, 1E-3);
		this.elements = elements;
		this.probabilities = probabilities;
	}

	/** Sample the list based on the probabilities */
	public E sample() {
		return sample(random);
	}

	/** Sample using the given Random instance */
	public E sample(Random random) {
		double rand = random.nextDouble();
		double accum = 0;
		for (int i = 0; i < elements.size(); i++) {
			accum += probabilities.get(i);
			if (accum >= rand)
				return elements.get(i);
		}
		return null;
	}
	
	public static double sum(List<Double> list) {
		double result = 0;
		for (Double d : list)
			result += d;
		return result;
	}

	public static boolean epsilonEquals(double x, double y, double epsilon) {
		assert epsilon >= 0 : "Epsilon should be positive";
		return Math.abs(x - y) <= epsilon;
	}

	
}
