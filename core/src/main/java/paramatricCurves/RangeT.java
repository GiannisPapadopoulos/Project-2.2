package paramatricCurves;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RangeT {

	private double low;
	private double high;
	
	public ArrayList<Double> getDiscreteCover(int precision) {
		double dif = (high-low)/precision;
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i=0;i<precision+1;i++)
			result.add(low+dif*i);
		return result;
	}
}
