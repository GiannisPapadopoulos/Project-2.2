package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.artemis.Component;

@Getter
@Setter
public class TrafficLightComponent
		extends Component {

	// The time the light is green
	private float timerGreen;
	// The time the light is orange
	private float timerOrange;
	// The time the light is red
	private float timerRed;
	// obvious time is obvious
	private float timeElapsed;

	private Status status;

	private boolean left;

	private boolean OnPointA;

	@AllArgsConstructor
	@Getter
	public enum Status {
		GREEN("light_green", "left_green"),
		ORANGE("light_orange", "left_orange"),
		RED("light_red", "left_red");

		private String straightLightTexture;
		private String leftLightTexture;
	}

	public TrafficLightComponent(float timerGreen, float timerOrange, float timerRed, Status status, boolean left,
									boolean OnPointA) {
		this.timerGreen = timerGreen;
		this.timerOrange = timerOrange;
		this.timerRed = timerRed;
		this.status = status;
		this.left = left;
		this.OnPointA = OnPointA;
	}

	public float timer() {
		if (status == Status.GREEN) {
			return timerGreen;
		}
		else if (status == Status.ORANGE) {
			return timerOrange;
		}
		else {
			return timerRed;
		}
	}

	// public void ColorChanger() {
	// if (timeElapsed >= timer()) {
	// if (status == Status.GREEN) {
	// status = Status.ORANGE;
	// timeElapsed = 0;
	// }
	// else if (status == Status.ORANGE) {
	// status = Status.RED;
	// timeElapsed = 0;
	// }
	// else {
	// status = Status.GREEN;
	// timeElapsed = 0;
	// }
	// }
	// }

	public String getTextureName() {
		return left ? status.getLeftLightTexture() : status.getStraightLightTexture();
	}

	@Override
	public String toString() {
		return "TrafficLightComponent [status=" + status + ", straight=" + left + ", OnPointA=" + OnPointA + "]";
	}

}
