package trafficsim.movement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.badlogic.gdx.math.Vector2;

/**
 * Behavior that is linked to a fixed location, as opposed to another moving object
 * 
 * @see MovingTargetBehavior
 * @author Giannis Papadopoulos
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public abstract class FixedTargetBehavior
		extends Behavior {

	protected Vector2 targetLocation;

}
