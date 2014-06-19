package trafficsim.systems;

import static trafficsim.TrafficSimConstants.RANDOM;
import graph.Graph;
import graph.Vertex;

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

	boolean tagged = false;

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
				Vertex<NavigationObject> source = routeComp.getSource();
				assert source != null;
				Graph<NavigationObject> graph = source.getParent();
				// To showcase highway
				if (source.getID() == 230 && !tagged) {
					tagged = true;
					routeComp.setTarget(graph.getVertex(234));
				}
				else if (spawnPoints.size() <= 1) {
					// int randIndex = RANDOM.nextInt(graph.getVertexCount() - 11);
					int randIndex = RANDOM.nextInt(graph.getVertexCount() - 1);

					// Make sure source != target
					if (randIndex >= source.getID()) {
						randIndex++;
					}
					routeComp.setTarget(graph.getVertex(randIndex));
				}
				else {
					int randIndex = RANDOM.nextInt(spawnPoints.size() - 1);
					if (spawnComponentMapper.get(spawnPoints.get(randIndex)).getVertex() == source) {
						randIndex++;
					}
					routeComp.setTarget(spawnComponentMapper.get(spawnPoints.get(randIndex)).getVertex());
					assert routeComp.getTarget() != source;
				}

			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
