package desktop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import trafficsim.experiments.AbstractExperiment;
import trafficsim.experiments.IntersectionThroughputExperiment;
import trafficsim.experiments.PredefinedParameters;
import trafficsim.experiments.RepeatedExperiment;
import trafficsim.experiments.SimulationParameters;

public class RunExperiments {

	public static void main(String[] args) {

		ExperimentDefiniton definition = intersectionExperiment;

		AbstractExperiment experiment = new IntersectionThroughputExperiment(definition.getParameters());
		// experiment = new ManhattanExperiment(definition.getParameters());

		int timesToRepeat = 1;
		RepeatedExperiment repeatedExp = new RepeatedExperiment(experiment, timesToRepeat, definition.getTextFile());
		repeatedExp.run();
	}

	static ExperimentDefiniton roundaboutExperiment = new ExperimentDefiniton(
																				"data/roundabout",
																		PredefinedParameters.roundaboutSimpleGraph);

	static ExperimentDefiniton intersectionExperiment = new ExperimentDefiniton(
																				"data/intersection",
																			PredefinedParameters.prioritydLightsSimpleGraph);

	static ExperimentDefiniton priorityManhattanExperiment = new ExperimentDefiniton(
																				"data/intersection",
																				PredefinedParameters.priorityLightsmanhattanGraph);
	
	
	@AllArgsConstructor
	@Getter
	static class ExperimentDefiniton {
		private String textFile;
		private SimulationParameters parameters;
	}


}
