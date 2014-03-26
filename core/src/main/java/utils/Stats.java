package utils;

import java.util.List;

/**
 * Methods for statistic analysis
 * 
 * @author Giannis Papadopoulos
 */
public class Stats {

	/**
	 * Return maximum value in array, -infinity if no such value.
	 */
	public static double max(double[] a) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * Return maximum value in subarray a[lo..hi], -infinity if no such value.
	 */
	public static double max(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double max = Double.NEGATIVE_INFINITY;
		for (int i = lo; i <= hi; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * Return maximum value of array, Integer.MIN_VALUE if no such value
	 */
	public static int max(int[] a) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * Return maximum value in subarray a[lo..hi], Integer.MIN_VALUE if no such value
	 */
	public static double max(int[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		int max = Integer.MIN_VALUE;
		for (int i = lo; i <= hi; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * Return minimum value in array, +infinity if no such value.
	 */
	public static double min(double[] a) {
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Return minimum value in subarray a[lo..hi], +infinity if no such value.
	 */
	public static double min(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double min = Double.POSITIVE_INFINITY;
		for (int i = lo; i <= hi; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Return minimum value of array, Integer.MAX_VALUE if no such value
	 */
	public static int min(int[] a) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Return minimum value in subarray a[lo..hi], Integer.MAX_VALUE if no such value
	 */
	public static double min(int[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double min = Integer.MAX_VALUE;
		for (int i = lo; i <= hi; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Return sum of all values in array.
	 */
	public static double sum(double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Return sum of all values in list.
	 */
	public static double sum(List<Float> a) {
		double sum = 0.0;
		for (int i = 0; i < a.size(); i++) {
			sum += a.get(i);
		}
		return sum;
	}

	/**
	 * Return sum of all values in subarray a[lo..hi].
	 */
	public static double sum(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Return sum of all values in array.
	 */
	public static int sum(int[] a) {
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Return sum of all values in subarray a[lo..hi].
	 */
	public static double sum(int[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Return average value in array, NaN if no such value.
	 */
	public static double mean(double[] a) {
		if (a.length == 0)
			return Double.NaN;
		double sum = sum(a);
		return sum / a.length;
	}

	/**
	 * Return average value in list, NaN if no such value.
	 */
	public static double mean(List<Float> a) {
		if (a.size() == 0)
			return Double.NaN;
		double sum = sum(a);
		return sum / a.size();
	}

	/**
	 * Return average value in subarray a[lo..hi], NaN if no such value.
	 */
	public static double mean(double[] a, int lo, int hi) {
		int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		double sum = sum(a, lo, hi);
		return sum / length;
	}

	/**
	 * Return average value in array, NaN if no such value.
	 */
	public static double mean(int[] a) {
		if (a.length == 0)
			return Double.NaN;
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i];
		}
		return sum / a.length;
	}

	/**
	 * Return average value in subarray a[lo..hi], NaN if no such value.
	 */
	public static double mean(int[] a, int lo, int hi) {
		int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		double sum = sum(a, lo, hi);
		return sum / length;
	}

	/**
	 * Return sample variance of array, NaN if no such value.
	 */
	public static double var(double[] a) {
		if (a.length == 0)
			return Double.NaN;
		double avg = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / a.length;
	}

	/**
	 * Return sample variance of subarray a[lo..hi], NaN if no such value.
	 */
	public static double var(double[] a, int lo, int hi) {
		int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		double avg = mean(a, lo, hi);
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / length;
	}

	/**
	 * Return sample variance of array, NaN if no such value.
	 */
	public static double var(int[] a) {
		if (a.length == 0)
			return Double.NaN;
		double avg = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / a.length;
	}

	/**
	 * Return sample variance of subarray a[lo..hi], NaN if no such value.
	 */
	public static double var(int[] a, int lo, int hi) {
		int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		double avg = mean(a, lo, hi);
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / length;
	}

	/**
	 * Return sample standard deviation of array, NaN if no such value.
	 */
	public static double stddev(double[] a) {
		return Math.sqrt(var(a));
	}

	/**
	 * Return population standard deviation of subarray a[lo..hi], NaN if no such value.
	 */
	public static double stddev(double[] a, int lo, int hi) {
		return Math.sqrt(var(a, lo, hi));
	}

	/**
	 * Return sample standard deviation of array, NaN if no such value.
	 */
	public static double stddev(int[] a) {
		return Math.sqrt(var(a));
	}

	/**
	 * Return population standard deviation of subarray a[lo..hi], NaN if no such value.
	 */
	public static double stddev(int[] a, int lo, int hi) {
		return Math.sqrt(var(a, lo, hi));
	}

	public double confidenceLo(double[] a) {
		return mean(a) - 1.96 * stddev(a) / Math.sqrt(a.length);
	}

	public static double confidenceHi(double[] a) {
		return mean(a) + 1.96 * stddev(a) / Math.sqrt(a.length);
	}

	public static double confidenceLo(int[] a) {
		return mean(a) - 1.96 * stddev(a) / Math.sqrt(a.length);
	}

	public static double confidenceHi(int[] a) {
		return mean(a) + 1.96 * stddev(a) / Math.sqrt(a.length);
	}

}
