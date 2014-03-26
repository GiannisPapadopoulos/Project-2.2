package trafficsim.systems;

import trafficsim.TrafficSimWorld;
import trafficsim.components.ExpiryComponent;
import trafficsim.components.PhysicsBodyComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class CollisionDisablingSystem
		extends EntitySystem {

	@Mapper
	private ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	private ComponentMapper<ExpiryComponent> expiryMapper;

	@SuppressWarnings("unchecked")
	public CollisionDisablingSystem() {
		super(Aspect.getAspectForAll(PhysicsBodyComponent.class));
	}

	@Override
	public void initialize() {
		super.initialize();
		World world2D = ((TrafficSimWorld) world).getBox2dWorld();
		world2D.setContactListener(new CollisionListener());
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	class CollisionListener
			implements ContactListener {

		@Override
		public void beginContact(Contact contact) {

		}

		private boolean bothDynamic(Contact contact) {
			return contact.getFixtureA().getBody().getType() == BodyType.DynamicBody
					&& contact.getFixtureB().getBody().getType() == BodyType.DynamicBody;
		}

		private void removeFromWorld(Object userData1) {
			if (userData1 != null && userData1.getClass() == Integer.class) {
				int id = (Integer) userData1;
				Entity car = world.getEntity(id);
				expiryMapper.get(car).setExpired(true);
			}
		}

		@Override
		public void endContact(Contact contact) {
			// TODO Auto-generated method stub

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			if (contact.isTouching() && bothDynamic(contact)) {
				Object userData1 = contact.getFixtureA().getBody().getUserData();
				Object userData2 = contact.getFixtureB().getBody().getUserData();
				removeFromWorld(userData1);
				contact.setEnabled(false);
				// removeFromWorld(userData2);
			}
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub
		}

	}


}
