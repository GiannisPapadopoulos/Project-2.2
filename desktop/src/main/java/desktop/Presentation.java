package desktop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import trafficsim.experiments.AbstractExperiment;
import trafficsim.experiments.ManhattanExperiment;
import trafficsim.experiments.PredefinedParameters;
import trafficsim.experiments.RepeatedExperiment;
import trafficsim.experiments.SimulationParameters;
import utils.ImagePacker;

public class Presentation {

	// Roundabout or highway
	static boolean roundabout = false;

	public static void main(String[] args) {

		ImagePacker.run();

		ExperimentDefiniton definition;
		if (roundabout)
			definition = roundaboutExperiment;
		else
			definition = highwaysExperiment;

		AbstractExperiment experiment = new ManhattanExperiment(definition.getParameters());

		int timesToRepeat = 5;
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
																						"data/manhattanPriority",
																						PredefinedParameters.priorityLightsmanhattanGraph);

	static ExperimentDefiniton basicManhattanExperiment = new ExperimentDefiniton(
																					"data/manhattanBasic",
																					PredefinedParameters.timedLightsmanhattanGraph);

	static ExperimentDefiniton greenWaveManhattanExperiment = new ExperimentDefiniton(
																						"data/manhattanGreenWave",
																						PredefinedParameters.greenWaveManhattanGraph);

	static ExperimentDefiniton highwaysExperiment = new ExperimentDefiniton("data/highways",
																			PredefinedParameters.highwaysGraph);

	@AllArgsConstructor
	@Getter
	static class ExperimentDefiniton {
		private String textFile;
		private SimulationParameters parameters;
	}


}
