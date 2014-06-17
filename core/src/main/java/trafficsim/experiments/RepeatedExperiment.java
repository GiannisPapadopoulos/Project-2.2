package trafficsim.experiments;

import static trafficsim.TrafficSimConstants.*;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import trafficsim.TrafficSimWorld;
import trafficsim.TrafficSimulation;
import trafficsim.experiments.ExperimentData.AggregatedDataList;
import trafficsim.experiments.ExperimentData.AggregatedScalar;
import trafficsim.experiments.ExperimentData.DataList;
import trafficsim.experiments.ExperimentData.Scalar;
import trafficsim.screens.SimulationScreen;
import utils.ExportData;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/** Class used to run an experiment multiple times and accumulate the results */
public class RepeatedExperiment {

	/** The experiment to be run */
	@Getter
	private AbstractExperiment experiment;

	/** Will be run n times */
	private int timesToRepeat;

	/** Name of the output file */
	private String textFile;

	/** Current iteration of the experiment */
	private int run = 0;

	@Getter
	private boolean firstRun = true;

	private List<ExperimentData> experimentData;

	private List<AggregatedDataList> dataLists;
	private List<AggregatedScalar> scalars;

	@Setter
	private SimulationScreen simScreen;

	public RepeatedExperiment(AbstractExperiment experiment, int timesToRepeat, String textFile) {
		this.experiment = experiment;
		this.timesToRepeat = timesToRepeat;
		this.textFile = textFile;
		dataLists = new ArrayList<AggregatedDataList>();
		scalars = new ArrayList<AggregatedScalar>();
		experimentData = new ArrayList<ExperimentData>();
	}

	public void runExperiment() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		config.foregroundFPS = config.backgroundFPS = FPS;
		SimulationScreen.application = new LwjglApplication(new TrafficSimulation(this), config);
	}

	public void run() {
		runExperiment();
	}
	
	public void notifySimulationEnded(TrafficSimWorld world) {
		ExperimentData gatheredData = experiment.getGatheredData(world);
		experimentData.add(gatheredData);
		if (firstRun) {
			for (int i = 0; i < gatheredData.getDataLists().size(); i++) {
				DataList dataList = gatheredData.getDataLists().get(i);
				AggregatedDataList aggList = new AggregatedDataList(dataList.getLabel());
				aggList.add(dataList);
				dataLists.add(aggList);
			}
			for (int i = 0; i < gatheredData.getScalars().size(); i++) {
				Scalar scalar = gatheredData.getScalars().get(i);
				AggregatedScalar aggScalar = new AggregatedScalar(scalar.getLabel());
				aggScalar.add(scalar);
				scalars.add(aggScalar);
			}
			firstRun = false;
		}
		else {
			for (int i = 0; i < gatheredData.getDataLists().size(); i++) {
				DataList dataList = gatheredData.getDataLists().get(i);
				dataLists.get(i).add(dataList);
			}
			for (int i = 0; i < gatheredData.getScalars().size(); i++) {
				Scalar scalar = gatheredData.getScalars().get(i);
				scalars.get(i).add(scalar);
			}
		}
		run++;
		if (run < timesToRepeat) {
			if (simScreen != null) {
				simScreen.show();
			}
		}
		else {
			ExportData.writeToFile(this, textFile);
			System.out.println(this);
			SimulationScreen.application.exit();
		}
	}

	@Override
	public String toString() {
		String result = "";
		for (AggregatedDataList aggList : dataLists) {
			result += aggList.getLabel() + ": " + aggList.getAggregatedList().size() + "\n";
			for (DataList dataList : aggList.getAggregatedList()) {
				for (Float value : dataList.getData()) {
					result += value + " ";
				}
				result += "\n";
			}
		}
		for (AggregatedScalar aggScalar : scalars) {
			result += aggScalar.getLabel() + ": " + aggScalar.getAggregatedList().size() + "\n";
			for (Scalar scalar : aggScalar.getAggregatedList()) {
				result += scalar.getValue() + " ";
			}
			result += "\n";
		}
		return result;
	}

}
