package trafficsim.data;

import static utils.Stats.mean;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import trafficsim.components.DataComponent;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class DataGatherer {

	List<Float> averageDistanceTravelled = new ArrayList<Float>();
	List<Float> averageTimeTravelled = new ArrayList<Float>();
	List<Float> averageVelocities = new ArrayList<Float>();
	List<Float> averagePercentageStopped = new ArrayList<Float>();
	List<Float> averageTimeWaited = new ArrayList<Float>();
	
	
	public void add(DataComponent dataComp) {
		averageDistanceTravelled.add(dataComp.getTotalDistance());
		averageTimeTravelled.add(dataComp.getTotalTime());
		averageVelocities.add(dataComp.getAverageSpeed());
		averagePercentageStopped.add(dataComp.getPercentageStopped());
		averageTimeWaited.add(dataComp.getTimeSpentOnTrafficLights());
	}

	@Override
	public String toString() {
		//@formatter:off
		return "averageDistanceTravelled " + mean(averageDistanceTravelled) + "\n" +
			   "averageVelocity " + 3.6 * mean(averageVelocities) + "\n" +
			   "averageTimeTravelled " + mean(averageTimeTravelled) + "\n" +
			   "averageTimeStopped " + mean(averagePercentageStopped) + "\n";
		//@formatter:on
	}

}
