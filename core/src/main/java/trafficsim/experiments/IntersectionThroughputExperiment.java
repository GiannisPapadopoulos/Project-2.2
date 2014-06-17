package trafficsim.experiments;

import static utils.EntityRetrievalUtils.getVertexEntity;

import java.util.ArrayList;
import java.util.List;

import trafficsim.TrafficSimWorld;
import trafficsim.components.IntersectionThroughputComponent;
import trafficsim.experiments.ExperimentData.DataList;
import trafficsim.experiments.ExperimentData.Scalar;

import com.artemis.Entity;

public class IntersectionThroughputExperiment
		extends AbstractExperiment {

	public IntersectionThroughputExperiment(SimulationParameters parameters) {
		super(parameters);
	}

	@Override
	public ExperimentData getGatheredData(TrafficSimWorld world) {
		Entity vertexEntity = getVertexEntity(world, world.getGraph().getVertex(0));
		float carsPerMinute = vertexEntity.getComponent(IntersectionThroughputComponent.class)
													.getTotalCarsPassed();
		List<Scalar> scalars = new ArrayList<Scalar>();
		List<DataList> dataLists = new ArrayList<DataList>();
		scalars.add(new Scalar("CarsPerMinute: ", 60 * carsPerMinute / getParameters().getTotalTimeInSecs()));
		dataLists.add(new DataList("Average Velocity:", world.getDataGatherer().getAverageVelocities()));
		ExperimentData experimentData = new ExperimentData(dataLists, scalars);
		return experimentData;
	}

}
