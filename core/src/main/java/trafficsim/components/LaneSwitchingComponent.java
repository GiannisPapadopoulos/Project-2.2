package trafficsim.components;

import graph.Edge;
import lombok.Getter;
import lombok.Setter;
import trafficsim.roads.NavigationObject;

import com.artemis.Component;


public class LaneSwitchingComponent extends Component {
	
	@Getter
	@Setter
	private int exitLaneIndex; // -1 if doesn't matter

	@Getter
	@Setter
	private int currentLaneIndex;
	
	@Getter
	@Setter
	private int minLaneIndex;
	@Getter
	@Setter
	private int maxLaneIndex;
	
	@Getter
	@Setter
	private Edge<NavigationObject> currentEdge;
}
