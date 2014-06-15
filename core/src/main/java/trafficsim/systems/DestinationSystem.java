package trafficsim.systems;

import static trafficsim.TrafficSimConstants.RANDOM;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import trafficsim.components.RouteComponent;
import trafficsim.components.SpawnComponent;
import trafficsim.roads.NavigationObject;

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
	private ComponentMapper<RouteComponent> routeComponentMapper;

	@Mapper
	private ComponentMapper<SpawnComponent> spawnComponentMapper;

	@Getter
	private List<Entity> spawnPoints = new ArrayList<>();

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
				Graph<NavigationObject> graph = routeComp.getSource().getParent();
				if (spawnPoints.size() <= 1) {
					int randIndex = RANDOM.nextInt(graph.getVertexCount() - 5);

					// Make sure source != target
					if (randIndex >= routeComp.getSource().getID()) {
						randIndex++;
					}
					routeComp.setTarget(graph.getVertex(randIndex));
				}
				else {

					System.out.println("yes");
					int randIndex = RANDOM.nextInt(spawnPoints.size() - 1);
					if (spawnComponentMapper.get(spawnPoints.get(randIndex)).getVertex() == routeComp.getSource()) {
						randIndex++;
					}
					routeComp.setTarget(spawnComponentMapper.get(spawnPoints.get(randIndex)).getVertex());
					assert routeComp.getTarget() != routeComp.getSource();
				}

			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
