package trafficsim.roads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoadTransition {

	private Road origin;
	private Road destination;
	
	
}
