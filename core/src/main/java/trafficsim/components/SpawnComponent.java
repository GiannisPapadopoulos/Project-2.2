package trafficsim.components;

import graph.Vertex;
import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;
import trafficsim.roads.Road;
import trafficsim.spawning.AbstractSpawnStrategy;

import com.artemis.Component;

/**
 * 
 * @author Giannis Papadopoulos
 */
@Getter
@AllArgsConstructor
public class SpawnComponent
		extends Component {

	/**
	 * The graph vertex where cards will be spawned.
	 * The convention is that it will have only one adjacent edge
	 */
	private Vertex<Road> vertex;

	@Delegate
	@Setter
	private AbstractSpawnStrategy spawnStrategy;
}
