package desktop;

import trafficsim.experiments.AbstractExperiment;
import trafficsim.experiments.IntersectionThroughputExperiment;
import trafficsim.experiments.PredefinedParameters;
import trafficsim.experiments.RepeatedExperiment;
import trafficsim.experiments.SimulationParameters;

public class RunExperiments {

	public static void main(String[] args) {
		// Change this
		SimulationParameters parameters = PredefinedParameters.roundaboutSimpleGraph;
		// Change this
		String textFile = "data/roundabout.txt";
		AbstractExperiment experiment = new IntersectionThroughputExperiment(parameters);

		RepeatedExperiment repeatedExp = new RepeatedExperiment(experiment, 3, textFile);
		repeatedExp.run();
	}

}
