package trafficsim.systems;

import static trafficsim.TrafficSimConstants.RANDOM;
import graph.Graph;
import trafficsim.components.RouteComponent;
import trafficsim.roads.Road;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

/**
 * Assigns the destination vertex for each entity, at the moment randomly
 * 
 * @author Giannis Papadopoulos
 */
public class DestinationSystem
		extends EntitySystem {

	@Mapper
	ComponentMapper<RouteComponent> routeComponentMapper;

	@SuppressWarnings("unchecked")
	public DestinationSystem() {
		super(Aspect.getAspectForAll(RouteComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			RouteComponent routeComp = routeComponentMapper.get(entity);
			if (routeComp.getTarget() == null) {
				assert routeComp.getSource() != null;
				Graph<Road> graph = routeComp.getSource().getParent();
				int randIndex = RANDOM.nextInt(graph.getVertexCount() - 1);
				// Make sure source != target
				if (randIndex >= routeComp.getSource().getID()) {
					randIndex++;
				}
				routeComp.setTarget(graph.getVertex(randIndex));
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
