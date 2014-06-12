package trafficsim.systems;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.degRad;
import static com.badlogic.gdx.math.MathUtils.sin;
import static functions.VectorUtils.getAngle;
import static functions.VectorUtils.getVector;
import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.LANE_WIDTH;
import static trafficsim.TrafficSimConstants.RANDOM;
import static trafficsim.TrafficSimConstants.TIMER;
import functions.VectorUtils;
import graph.Vertex;
import trafficsim.TrafficSimWorld;
import trafficsim.callbacks.TrafficRayCastCallBack;
import trafficsim.components.RouteComponent;
import trafficsim.components.SpawnComponent;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class SpawnSystem
		extends EntitySystem {

	@Mapper
	ComponentMapper<SpawnComponent> spawnComponentMapper;

	@SuppressWarnings("unchecked")
	public SpawnSystem() {
		super(Aspect.getAspectForAll(SpawnComponent.class));
	}

	@Override
	protected void inserted(Entity entity) {
		world.getSystem(DestinationSystem.class).getSpawnPoints().add(entity);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {

			Entity entity = entities.get(i);
			SpawnComponent spawnComp = spawnComponentMapper.get(entity);
			if (spawnComp.shouldSpawn(TIMER.getTime())) {

				Vertex<NavigationObject> spawnVertex = spawnComp.getVertex();
				Vertex<NavigationObject> connection = spawnVertex.getParent().getVertex(spawnVertex.getAdjacentVertices().get(0));
				float angle = 0.0f;//TODOgetAngle(spawnVertex.getData(), connection.getData()) * degRad;
				
				Vector2 position = VectorUtils.getMidPoint(spawnVertex.getData());
				Vector2 laneCorrection = null;//TODO
				position.add(laneCorrection);
				if (canSpawn(spawnComp, position, angle)) {
					int randInt = RANDOM.nextInt(7) + 1;
					Entity car = EntityFactory.createCar((TrafficSimWorld) world, position, 1f, 40, angle, "car"
																											+ randInt);
					car.addComponent(new RouteComponent(spawnComp.getVertex()));
					car.addToWorld();
					spawnComp.spawned(TIMER.getTime());
				}
			}
		}

	}

	/**
	 * @param position
	 * @param angle the car will be spawned at
	 * @return If it's legal to spawn i.e. there is no other car there
	 */
	private boolean canSpawn(SpawnComponent spawnComp, Vector2 position, float angle) {
		World box2dWorld = ((TrafficSimWorld) world).getBox2dWorld();
		// Vector2 position = getMidPoint(spawnComp.getVertex().getData());
		Vector2 angleAdjustment = new Vector2(cos(angle), sin(angle));

		// If there is a car within this distance we will not spawn
		float rayLength = 1.5f * CAR_LENGTH;

		TrafficRayCastCallBack rayCallBack = new TrafficRayCastCallBack();
		box2dWorld.rayCast(rayCallBack, position, position.cpy().add(angleAdjustment.cpy().scl(rayLength)));

		return rayCallBack.foundSomething() ? false : true;
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
