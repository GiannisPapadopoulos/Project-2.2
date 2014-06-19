package trafficsim.systems;

import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.TIMER;
import static utils.EntityRetrievalUtils.getEntity;
import graph.EntityIdentificationData;
import lombok.Getter;
import trafficsim.TrafficSimWorld;
import trafficsim.components.ExpiryComponent;
import trafficsim.components.PhysicsBodyComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.Body;
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

	@Getter
	private int deleted;

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
			if (userData1 != null && userData1.getClass() == EntityIdentificationData.class) {
				Entity car = getEntity(world, (EntityIdentificationData) userData1);
				expiryMapper.get(car).setExpired(true);
			}
		}

		@Override
		public void endContact(Contact contact) {
			// TODO Auto-generated method stub

		}

		boolean firstHit = false;

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			disableCollision(contact);
			// handleCollision(contact);
		}

		private void disableCollision(Contact contact) {
			if (contact.isTouching() && bothDynamic(contact)) {
				contact.setEnabled(false);
				if (!firstHit) {
					System.out.println("first hit " + TIMER.getTime() / 1000.0 + " pos "
										+ contact.getFixtureA().getBody().getPosition());
					firstHit = true;
				}
			}
		}

		private void handleCollision(Contact contact) {
			if (contact.isTouching() && bothDynamic(contact)) {
				Body physBody1 = contact.getFixtureA().getBody();
				Body physBody2 = contact.getFixtureB().getBody();
				if (physBody1.getPosition().dst(physBody2.getPosition()) < CAR_LENGTH / 2) {
					removeFromWorld(physBody1.getUserData());
					deleted++;
					if (!firstHit) {
						System.out.println("first hit " + TIMER.getTime() / 1000.0 + " pos "
											+ contact.getFixtureA().getBody().getPosition());
						firstHit = true;
					}
				}
				// removeFromWorld(physBody1.getUserData());
				contact.setEnabled(false);

			}
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub
		}

	}


}
