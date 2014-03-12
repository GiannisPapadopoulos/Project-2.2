package trafficsim.systems;

import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SteeringComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class SteeringSystem
		extends EntitySystem {

	@Mapper
	ComponentMapper<RouteComponent> routeComponentMapper;
	@Mapper
	ComponentMapper<SteeringComponent> steeringComponentMapper;
	@Mapper
	ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;

	@SuppressWarnings("unchecked")
	public SteeringSystem() {
		super(Aspect.getAspectForAll(SteeringComponent.class, RouteComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if(routeComponentMapper.has(entity) && steeringComponentMapper.has(entity)) {
				RouteComponent routeComp = routeComponentMapper.get(entity);
				SteeringComponent steeringComp = steeringComponentMapper.get(entity);
				if(steeringComp.isTurning()) {
					PhysicsBodyComponent physComp = physicsBodyMapper.get(entity);
					if (isDesiredAngle(physComp.getAngle(), steeringComp.getDesiredAngle())) {
						steeringComp.setTurning(false);
						physComp.setAngularVelocity(0);
					}
				}
				else {
					// Do AABB querry
				}
			}
		}
	}

	private boolean isDesiredAngle(float angle, float desiredAngle) {
		// TODO define this as a constant, check delta time
		return Math.abs(desiredAngle - angle) < 0.05;
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
