package trafficsim.systems;

import pathfinding.GraphAction;
import pathfinding.GraphBasedAstar;
import pathfinding.GraphState;
import search.Path;
import trafficsim.components.RouteComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class PathFindingSystem
		extends EntitySystem {

	@Mapper
	ComponentMapper<RouteComponent> routeComponentMapper;

	@SuppressWarnings("unchecked")
	public PathFindingSystem() {
		super(Aspect.getAspectForAll(RouteComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (routeComponentMapper.has(entity)) {
				RouteComponent routeComp = routeComponentMapper.get(entity);
				if (!routeComp.isSet()) {
					// sanity check
					assert routeComp.getSource() != null && routeComp.getTarget() != null;
					Path<GraphState, GraphAction> path = new GraphBasedAstar().findRoute(	routeComp.getSource(),
																							routeComp.getTarget());
					assert path.isValidPath() : " No valid path found!";
					routeComp.setPath(path);
					routeComp.setSet(true);
					routeComp.setCurrentVertex(routeComp.getSource());
					routeComp.setEdgeIndex(0);
				}
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
