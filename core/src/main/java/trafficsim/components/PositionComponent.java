package trafficsim.components;

import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

@Getter
@AllArgsConstructor
public class PositionComponent
		extends Component {

	@Delegate
	private Vector2 position;

}
