package trafficsim.movement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.artemis.Entity;

/**
 * Behavior that is linked to a moving target, as opposed to a fixed point
 * 
 * @see FixedTargetBehavior
 * @author Giannis Papadopoulos
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class MovingTargetBehavior
		extends Behavior {

	private Entity targetEntity;

}
