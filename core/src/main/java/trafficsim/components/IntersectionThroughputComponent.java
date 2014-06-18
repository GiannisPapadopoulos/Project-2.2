package trafficsim.components;

import lombok.Getter;

import com.artemis.Component;

/** Component that measures how many cars cross an intersection */
@Getter
public class IntersectionThroughputComponent
		extends Component {

	private int totalCarsPassed;

	public void increment() {
		totalCarsPassed++;
	}

	public void reset() {
		totalCarsPassed = 0;
	}

}
