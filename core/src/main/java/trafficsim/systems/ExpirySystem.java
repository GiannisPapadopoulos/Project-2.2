package trafficsim.systems;

import trafficsim.TrafficSimWorld;
import trafficsim.components.DataComponent;
import trafficsim.components.ExpiryComponent;
import trafficsim.components.PhysicsBodyComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Will remove expired components like bullets from the world
 * 
 * @author Giannis Papadopoulos
 */
public class ExpirySystem
		extends EntitySystem {

	@Mapper
	private ComponentMapper<ExpiryComponent> expiryMapper;
	@Mapper
	private ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	private ComponentMapper<DataComponent> dataComponentMapper;

	@SuppressWarnings("unchecked")
	public ExpirySystem() {
		super(Aspect.getAspectForAll(ExpiryComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (expiryMapper.get(entity).isExpired()) {
				if (physicsBodyMapper.has(entity)) {
					// Remove the physicsBody from the world
					World box2dWorld = ((TrafficSimWorld) world).getBox2dWorld();
					box2dWorld.destroyBody(physicsBodyMapper.get(entity).getBody());
				}
				if (dataComponentMapper.has(entity) && dataComponentMapper.get(entity).getTotalTime() > 5) {
					DataComponent dataComp = dataComponentMapper.get(entity);
					((TrafficSimWorld) world).getDataGatherer().add(entity.getId());
					// System.out.println(dataComp.getTotalDistance() + " " + dataComp.getAverageSpeed() + " pct "
					// + dataComp.getPercentageStopped());
					world.getSystem(MovementSystem.class).carRemoved();
				}
				entity.deleteFromWorld();

			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
