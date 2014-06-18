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
		testManhattan();
		testRoundabout();
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
