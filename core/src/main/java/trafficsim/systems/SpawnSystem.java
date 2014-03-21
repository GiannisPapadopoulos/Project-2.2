package trafficsim.systems;

import static com.badlogic.gdx.math.MathUtils.degRad;
import static functions.VectorUtils.getAngle;
import static functions.VectorUtils.getVector;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import static trafficsim.TrafficSimConstants.TIMER;
import functions.VectorUtils;
import graph.Vertex;
import trafficsim.TrafficSimWorld;
import trafficsim.components.RouteComponent;
import trafficsim.components.SpawnComponent;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.Road;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

public class SpawnSystem
		extends EntitySystem {

	@Mapper
	ComponentMapper<SpawnComponent> spawnComponentMapper;

	@SuppressWarnings("unchecked")
	public SpawnSystem() {
		super(Aspect.getAspectForAll(SpawnComponent.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			SpawnComponent spawnComp = spawnComponentMapper.get(entity);
			if (spawnComp.shouldSpawn(TIMER.getTime())) {
				// TODO AABB query to check if another car is already there
				if (canSpawn(spawnComp)) {
					Vertex<Road> spawnVertex = spawnComp.getVertex();
					Vertex<Road> connection = spawnVertex.getParent().getVertex(spawnVertex.getAdjacentVertices()
																							.get(0));
					Vector2 position = VectorUtils.getMidPoint(spawnVertex.getData());
					Vector2 laneCorrection = getVector(spawnVertex.getData(), connection.getData()).nor()
																									.rotate(-90)
																									.scl(LANE_WIDTH / 2);
					position.add(laneCorrection);
					float angle = getAngle(spawnVertex.getData(), connection.getData()) * degRad;
					Entity car = EntityFactory.createCar((TrafficSimWorld) world, position, 1f, 40, angle, "car4");
					car.addComponent(new RouteComponent(spawnComp.getVertex()));
					car.addToWorld();
					spawnComp.spawned(TIMER.getTime());
				}
			}
		}

	}

	// TODO AABB query to check if another car is already there
	private boolean canSpawn(SpawnComponent spawnComp) {
		return true;
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
