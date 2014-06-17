package trafficsim.systems;

import static trafficsim.TrafficSimConstants.spawnRate;
import lombok.Getter;
import lombok.Setter;
import trafficsim.components.SpawnComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

/**
 * This systems manages all changes that need to be made after the global speed limit changes
 * 
 * @author Giannis Papadopoulos
 */
public class ManageSpawnRateChangeSystem
		extends EntitySystem {
	
	@Mapper
	private ComponentMapper<SpawnComponent> spawnMapper;

	@Getter
	@Setter
	private boolean spawnRateModified = false;

	@SuppressWarnings("unchecked")
	public ManageSpawnRateChangeSystem() {
		super(Aspect.getAspectForAll(SpawnComponent.class));
	}

	@Override
	protected boolean checkProcessing() {
		return spawnRateModified;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			SpawnComponent spawnComp = spawnMapper.get(entity);
			spawnComp.getSpawnStrategy().setInterval(spawnRate);
		}
		spawnRateModified = false;
	}
}
