package trafficsim.data;

import static utils.Stats.mean;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import trafficsim.components.DataComponent;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataGatherer {

	List<Float> averageDistanceTravelled = new ArrayList<Float>();
	List<Float> averageTimeTravelled = new ArrayList<Float>();
	List<Float> averageVelocities = new ArrayList<Float>();
	List<Float> averagePercentageStopped = new ArrayList<Float>();
	List<Float> averageTimeWaited = new ArrayList<Float>();
	
	/** Contains cars which are currently on the map */
	TIntObjectMap<DataComponent> notYetAdded = new TIntObjectHashMap<DataComponent>();;
	
	private void add(DataComponent dataComp) {
		averageDistanceTravelled.add(dataComp.getTotalDistance());
		averageTimeTravelled.add(dataComp.getTotalTime());
		averageVelocities.add(dataComp.getAverageSpeed());
		averagePercentageStopped.add(dataComp.getPercentageStopped());
		averageTimeWaited.add(dataComp.getTimeSpentOnTrafficLights());
	}

	public void insert(int key, DataComponent dataComp) {
		notYetAdded.put(key, dataComp);
	}

	public void add(int key) {
		assert notYetAdded.containsKey(key) : "Key doesn't exist";
		add(notYetAdded.get(key));
		notYetAdded.remove(key);
	}

	/** Returns the average times of the vehicles currently on the map */
	public List<Float> getCurrentAveragePercentageStopped() {
		List<Float> percentageStopped = new ArrayList<Float>();
		for (DataComponent dataComponent : notYetAdded.values(new DataComponent[notYetAdded.size()])) {
			percentageStopped.add(dataComponent.getPercentageStopped());
		}
		return percentageStopped;
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
	
	static class DataList {
		private List<Float> data;
		private int runningSum;
	}

}
