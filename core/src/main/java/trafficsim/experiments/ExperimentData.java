package trafficsim.experiments;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents all the data collected from a given experiment
 * 
 * @author Giannis Papadopoulos
 */
@Getter
@AllArgsConstructor
public class ExperimentData {

	private List<DataList> dataLists;
	private List<Scalar> scalars;

	/** List of data extracted from the simulation */
	@AllArgsConstructor
	@Getter
	public static class DataList {
		private String label;
		private List<Float> data;
	}

	/** Scalar data point (single number) */
	@AllArgsConstructor
	@Getter
	public static class Scalar {

		/** To write on the text file */
		private String label;
		private float value;
	}

	/** A list of DataLists, produced by running the same experiment multiple times */
	@Getter
	public static class AggregatedDataList {

		private String label;
		private List<DataList> aggregatedList;

		public AggregatedDataList(String label) {
			this.label = label;
			aggregatedList = new ArrayList<DataList>();
		}

		public void add(DataList dataList) {
			assert dataList.getLabel().equals(label);
			aggregatedList.add(dataList);
		}
	}

	/** A list of Scalars, produced by running the same experiment multiple times */
	@Getter
	public static class AggregatedScalar {

		private String label;
		private List<Scalar> aggregatedList;

		public AggregatedScalar(String label) {
			this.label = label;
			aggregatedList = new ArrayList<Scalar>();
		}

		public void add(Scalar scalar) {
			assert scalar.getLabel().equals(label);
			aggregatedList.add(scalar);
		}
	}

	@AllArgsConstructor
	@Getter
	public static class RepeatedExperimentData {

		private List<AggregatedDataList> dataLists;
		private List<AggregatedScalar> scalars;

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

}
