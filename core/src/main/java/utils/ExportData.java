package utils;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import trafficsim.components.DataComponent;
import trafficsim.data.DataGatherer;
import trafficsim.experiments.ExperimentData.AggregatedDataList;
import trafficsim.experiments.ExperimentData.AggregatedScalar;
import trafficsim.experiments.ExperimentData.DataList;
import trafficsim.experiments.ExperimentData.RepeatedExperimentData;
import trafficsim.experiments.RepeatedExperiment;

public class ExportData {

	public static void writeToFile(RepeatedExperiment experiment, String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(experiment.toString());
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RepeatedExperimentData readFromFile(String fileName, int numLists, int numScalars) {
		try {
			Scanner scanner = new Scanner(new File(fileName));
			List<AggregatedDataList> aggLists = new ArrayList<AggregatedDataList>();
			List<AggregatedScalar> scalars = new ArrayList<AggregatedScalar>();
			RepeatedExperimentData data = new RepeatedExperimentData(aggLists, scalars);
			for (int i = 0; i < numLists; i++) {
				String firstLine = scanner.nextLine();
				String[] s = firstLine.split("::");
				String label = s[0];
				int size = Integer.parseInt(s[1].trim());
				AggregatedDataList aggList = new AggregatedDataList(label);
				List<DataList> dataLists;

				String line;
				for (int j = 0; j < size; j++) {
					line = scanner.nextLine();
					String[] split = line.split(" ");
					List<Float> values = new ArrayList<Float>();
					for (String number : split) {
						values.add(Float.parseFloat(number));
					}
					DataList list = new DataList(label, values);
					aggList.add(list);
				}
				aggLists.add(aggList);
			}
			for (int i = 0; i < numScalars; i++) {
				String label = scanner.next();
				int size = scanner.nextInt();
				System.out.println("label " + size);
				List<Float> values = new ArrayList<Float>();
				for (int j = 0; j < size; j++) {
					float value = scanner.nextFloat();
					values.add(value);
				}
				AggregatedScalar scalar = new AggregatedScalar(label);
				scalars.add(scalar);
			}
			scanner.close();
			return data;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeToFile(DataGatherer dataGatherer, String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(dataGatherer.getAverageDistanceTravelled().size() + "\n");
			for (float distance : dataGatherer.getAverageDistanceTravelled()) {
				writer.write(distance + " ");
			}
			writer.write("\n");
			for (float time : dataGatherer.getAverageTimeTravelled()) {
				writer.write(time + " ");
			}
			writer.write("\n");
			for (float velocity : dataGatherer.getAverageVelocities()) {
				writer.write(velocity + " ");
			}
			writer.write("\n");
			for (float pctStopped : dataGatherer.getAveragePercentageStopped()) {
				writer.write(pctStopped + " ");
			}
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DataGatherer readGatehererFromFile(String fileName) {
		try {
			Scanner scanner = new Scanner(new File(fileName));
			int dataPoints = scanner.nextInt();
			List<Float> averageDistanceTravelled = new ArrayList<Float>();
			List<Float> averageTimeTravelled = new ArrayList<Float>();
			List<Float> averageVelocities = new ArrayList<Float>();
			List<Float> averagePercentageStopped = new ArrayList<Float>();
			List<Float> averageTimeWaited = new ArrayList<Float>();
			TIntObjectMap<DataComponent> notYetAdded = new TIntObjectHashMap<DataComponent>();
			for (int i = 0; i < dataPoints; i++) {
				averageDistanceTravelled.add(scanner.nextFloat());
			}
			for (int i = 0; i < dataPoints; i++) {
				averageTimeTravelled.add(scanner.nextFloat());
			}
			for (int i = 0; i < dataPoints; i++) {
				averageVelocities.add(scanner.nextFloat());
			}
			for (int i = 0; i < dataPoints; i++) {
				averagePercentageStopped.add(scanner.nextFloat());
			}
			scanner.close();
			return new DataGatherer(averageDistanceTravelled, averageTimeTravelled, averageVelocities,
									averagePercentageStopped, averageTimeWaited, notYetAdded);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
