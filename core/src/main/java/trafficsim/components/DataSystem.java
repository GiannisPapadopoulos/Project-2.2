package trafficsim.components;

import trafficsim.TrafficSimWorld;
import trafficsim.components.SteeringComponent.State;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

public class DataSystem
		extends EntitySystem {

	@Mapper
	private ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;

	@Mapper
	private ComponentMapper<SteeringComponent> steeringComponentMapper;

	@Mapper
	private ComponentMapper<DataComponent> dataComponentMapper;

	@SuppressWarnings("unchecked")
	public DataSystem() {
		super(Aspect.getAspectForAll(DataComponent.class));
	}

	@Override
	protected void inserted(Entity entity) {
		DataComponent dataComp = dataComponentMapper.get(entity);
		((TrafficSimWorld) world).getDataGatherer().insert(entity.getId(), dataComp);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity car = entities.get(i);
			if (physicsBodyMapper.has(car) && steeringComponentMapper.has(car)) {
				PhysicsBodyComponent physComp = physicsBodyMapper.get(car);
				SteeringComponent steeringComp = steeringComponentMapper.get(car);
				DataComponent dataComp = dataComponentMapper.get(car);

				float dT = dataComp.getDeltaTime();
				if (dT != 0) {
					Vector2 previousPosition = dataComp.getLastPosition().cpy();
					float distance = physComp.getPosition().dst(previousPosition);
					//dataComp.setDistanceLeft(dataComp.getTotalDistance() + distance);
					dataComp.setTotalDistance(dataComp.getTotalDistance() + distance);
					dataComp.setTotalTime(dataComp.getTotalTime() + world.getDelta());
					if (steeringComp.getState() == State.STOPPED) {
						dataComp.setTimeSpentOnTrafficLights(dataComp.getTimeSpentOnTrafficLights() + world.getDelta());
					}
					// System.out.println(distance + " " + physComp.getPosition() + " " + dataComp.getLastPosition());
				}
				dataComp.setLastPosition(physComp.getPosition().cpy());
				dataComp.setDeltaTime(world.getDelta());
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
