package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import trafficsim.data.DataGatherer;

public class ExportData {

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

	public static DataGatherer readFromFile(String fileName) {
		try {
			Scanner scanner = new Scanner(new File(fileName));
			int dataPoints = scanner.nextInt();
			List<Float> averageDistanceTravelled = new ArrayList<Float>();
			List<Float> averageTimeTravelled = new ArrayList<Float>();
			List<Float> averageVelocities = new ArrayList<Float>();
			List<Float> averagePercentageStopped = new ArrayList<Float>();
			List<Float> averageTimeWaited = new ArrayList<Float>();
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
									averagePercentageStopped, averageTimeWaited);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
