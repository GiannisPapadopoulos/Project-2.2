package desktop;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.inference.TestUtils;

import trafficsim.experiments.ExperimentData.DataList;
import trafficsim.experiments.ExperimentData.RepeatedExperimentData;
import utils.ExportData;
import utils.Stats;

public class AnalyzeData {

	public static void main(String[] args) {
		// testManhattan();
		// testRoundabout();
		// manhattanMeans();
		highways();
	}

	public static void highways() {
		String high = "data/setHighWays.txt";
		String noHigh = "data/setNoHighWays.txt";
		RepeatedExperimentData highway = ExportData.readFromFile(high, 1, 0);
		RepeatedExperimentData noHighway = ExportData.readFromFile(noHigh, 1, 0);
		double[] speedHighway = toDoubleArray(highway.getDataLists().get(0).getAggregatedList().get(0));
		double[] speedNoHighway = toDoubleArray(noHighway.getDataLists().get(0).getAggregatedList().get(0));

		double highMean = Stats.mean(speedHighway);
		double noHighMean = Stats.mean(speedNoHighway);

		double conHigh = Stats.confidenceHi(speedHighway) - highMean;
		double conNoHigh = Stats.confidenceHi(speedNoHighway) - noHighMean;

		System.out.println("Highways " + highMean + " con " + conHigh);
		System.out.println("NoHighways " + noHighMean + " con " + conNoHigh);


	}

	private static void manhattanMeans() {
		double[] meanSpeedP = new double[6];
		double[] meanSpeedT = new double[6];


		double[] stdSpeedP = new double[6];
		double[] stdSpeedT = new double[6];

		double[] confidenceSpeedT = new double[6];
		double[] confidenceSpeedP = new double[6];

		boolean[] pairedT = new boolean[6];
		double[] pTTest = new double[6];

		for (int i = 1; i <= 6; i++) {
			String basic = "data/manhattanBasic" + i + ".txt";
			String pr = "data/manhattanPriority" + i + ".txt";
			RepeatedExperimentData timed = ExportData.readFromFile(basic, 2, 0);
			RepeatedExperimentData priority = ExportData.readFromFile(pr, 2, 0);
			double[] speedTimed = toDoubleArray(timed.getDataLists().get(0).getAggregatedList().get(0));
			double[] speedPriority = toDoubleArray(priority.getDataLists().get(0).getAggregatedList().get(0));
			meanSpeedP[i - 1] = Stats.mean(speedPriority);
			stdSpeedP[i - 1] = Stats.stddev(speedPriority);
			confidenceSpeedP[i - 1] = Stats.confidenceHi(speedPriority) - meanSpeedP[i - 1];

			meanSpeedT[i - 1] = Stats.mean(speedTimed);
			stdSpeedT[i - 1] = Stats.stddev(speedTimed);
			confidenceSpeedT[i - 1] = Stats.confidenceHi(speedTimed) - meanSpeedT[i - 1];

			try {
				double pValue = TestUtils.pairedTTest(speedPriority, speedTimed);
				boolean significant = TestUtils.pairedTTest(speedPriority, speedTimed, .05);
				pairedT[i - 1] = significant;
				pTTest[i - 1] = pValue;
			}
			catch (IllegalArgumentException | MathException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Priority mean " + Arrays.toString(meanSpeedP) + " std " + Arrays.toString(stdSpeedP) + " con " + Arrays.toString(confidenceSpeedP) 
							+ "\nTimed mean" + Arrays.toString(meanSpeedT) + " std " + Arrays.toString(stdSpeedT) + " con " + Arrays.toString(confidenceSpeedT) );
		System.out.println("Significant " + Arrays.toString(pairedT) + " pvalue " + Arrays.toString(pTTest));
	}

	private static void testManhattan() {
		String mhB2 = "data/manhattanBasic2.txt";
		RepeatedExperimentData MHB2 = ExportData.readFromFile(mhB2, 2, 0);
		String mhP2 = "data/manhattanPriority2.txt";
		RepeatedExperimentData MHP2 = ExportData.readFromFile(mhP2, 2, 0);
		int size = MHB2.getDataLists().get(0).getAggregatedList().size();
		double[] meanB = new double[size];
		double[] meanP = new double[size];
		System.out.println(size);
		for (int i = 0; i < size; i++) {
			meanB[i] = Stats.mean(toDoubleArray(MHB2.getDataLists().get(0).getAggregatedList().get(i)));
			meanP[i] = Stats.mean(toDoubleArray(MHP2.getDataLists().get(0).getAggregatedList().get(i)));
		}
		System.out.println(Arrays.toString(meanB) + "\n" + Arrays.toString(meanP));
		try {
			double pValue = TestUtils.pairedTTest(meanP, meanB);
			boolean significant = TestUtils.pairedTTest(meanP, meanB, .05);
			System.out.println("Significant " + significant + " pvalue " + pValue);
		}
		catch (IllegalArgumentException | MathException e) {
			e.printStackTrace();
		}
	}

	public static void testRoundabout() {
		double[] intersection = { 67.0, 68.333336, 69.333336, 69.333336, 69.333336, 68.333336, 68.333336, 69.333336,
									69.0, 69.0 };
		double[] roundabout = { 117.0, 112.666664, 113.0, 112.333336, 119.333336, 113.333336, 110.0, 111.0, 108.0,
								112.0 };
		try {
			double pValue = TestUtils.pairedTTest(roundabout, intersection);
			boolean significant = TestUtils.pairedTTest(roundabout, intersection, .05);
			System.out.println("Significant " + significant + " pvalue " + pValue);
		}
		catch (IllegalArgumentException | MathException e) {
			e.printStackTrace();
		}

	}

	public static double[] toDoubleArray(DataList dataList) {
		List<Float> data = dataList.getData();
		double[] result = new double[data.size()];
		int i = 0;
		for (Float value : data) {
			result[i++] = Double.parseDouble("" + value);
		}
		return result;
	}

}
