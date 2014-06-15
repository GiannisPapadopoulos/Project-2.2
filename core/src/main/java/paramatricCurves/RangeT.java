package paramatricCurves;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RangeT {

	private Float low;
	private Float high;
	
	public ArrayList<Float> getDiscreteCover(int numPoints) {
		double dif = (high - low) / numPoints;
		ArrayList<Float> result = new ArrayList<Float>();
		for (int i = 0; i < numPoints + 1; i++)
			result.add((float) (low + dif * i));
		return result;
	}
}
