package examples;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsDemo
		implements ApplicationListener {

	// static {
	// GdxNativesLoader.load();
	// }

	// World world = new World(new Vector2(0, -100), true);
	World world;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;
	static final float BOX_STEP = 1 / 60f;
	static final int BOX_VELOCITY_ITERATIONS = 8;
	static final int BOX_POSITION_ITERATIONS = 3;
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_WORLD_TO = 100f;

	MovingBody body;
	MovingBody target;

	Body rectangleBody, circleBody;

	private void createWall(float x, float y, boolean vertical) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2(x, y));
		Body groundBody = world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		if (vertical)
			groundBox.setAsBox(0, (camera.viewportHeight) * 2);
		else
			groundBox.setAsBox((camera.viewportWidth) * 2, 0);
		groundBody.createFixture(groundBox, 0.0f);
	}

	@Override
	public void create() {
		// world = new World(new Vector2(0, -100), true);
		world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera();
		camera.viewportHeight = 320;
		camera.viewportWidth = 480;
		camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);
		camera.update();
		createWall(0, 5, false);
		createWall(0, camera.viewportHeight - 5, false);
		createWall(5, 0, true);
		createWall(camera.viewportWidth - 5, 0, true);

		StandStillStrategy still = new StandStillStrategy();
		RandomStrategy random = new RandomStrategy();
		MovementStrategy ramming = new RammingStrategy();
		FleeingStrategy flee = new FleeingStrategy();
		MixedStrategy circling = new MixedStrategy();
		// CirclingStrategy strategy4 = new CirclingStrategy();
		// SpiralStrategy strategy6 = new SpiralStrategy();
		int startX = 20;
		int startY = 300;
		target = new MovingBody(300, 100, 0, 0, 30, random);
		for (int i = 0; i < 1000; i++) {
			startX = (int) (600 * Math.random());
			startY = (int) (600 * Math.random());
			body = new MovingBody(startX, startY, 0.1f, 0, 350, random);
			//
			body.setTarget(target);
			target.setTarget(body);
		}
		debugRenderer = new Box2DDebugRenderer();

		createConnectedBody();
		// BodyDef circleBodyDef = new BodyDef();
		// circleBodyDef.type = BodyType.DynamicBody;
		// circleBodyDef.position.set(-7, 2);
		// circleBody = world.createBody(circleBodyDef);
		// circleBody.createFixture(circleFixtureDef);

	}

	private void createConnectedBody() {
		BodyDef rectangleBodyDef = new BodyDef();
		rectangleBodyDef.type = BodyType.DynamicBody;
		rectangleBodyDef.position.set(100, 200);
		rectangleBody = world.createBody(rectangleBodyDef);
		PolygonShape rectangleBodyShape = new PolygonShape();
		rectangleBodyShape.setAsBox(10f, 7.5f);
		FixtureDef rectangleBodyFixtureDef = new FixtureDef();
		rectangleBodyFixtureDef.shape = rectangleBodyShape;
		rectangleBodyFixtureDef.restitution = 0.8f;
		rectangleBodyFixtureDef.density = 1.0f;
		rectangleBodyFixtureDef.friction = 0.0f;
		rectangleBody.createFixture(rectangleBodyFixtureDef);

		/********************** CREATING THE SECOND BODY (CIRCLE BODY) ************/

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(7.5f);
		circleShape.setPosition(new Vector2(0, 15)); // Provide the relative location with respect to the center
		FixtureDef circleFixtureDef = new FixtureDef();
		circleFixtureDef.shape = circleShape;
		circleFixtureDef.restitution = 0.8f;
		circleFixtureDef.density = 1.0f;
		circleFixtureDef.friction = 0.0f;

		// Alternatively look at subclasses of Joint
		rectangleBody.createFixture(circleFixtureDef);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		debugRenderer.render(world, camera.combined);
		target.update();
		body.update();
		rectangleBody.applyForceToCenter(10 * rectangleBody.getMass(), 0, true);
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	class MovingBody {

		@Getter
		private Body body;
		@Getter
		private BodyDef bodyDef;

		private float acceleration;
		private float maxForce;

		private MovementStrategy movementStrategy;
		@Getter
		@Setter
		private Vector2 desiredVelocity = new Vector2(0, 0);
		@Getter
		@Setter
		private MovingBody target;

		MovingBody(int x, int y, float vx, float vy, float acceleration, MovementStrategy movementStrategy) {
			this.acceleration = acceleration;
			this.movementStrategy = movementStrategy;
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(x, y);
			bodyDef.linearVelocity.set(vx, vy);
			// bodyDef.angularVelocity = 3;
			body = world.createBody(bodyDef);
			CircleShape dynamicCircle = new CircleShape();
			dynamicCircle.setRadius(.5f);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = dynamicCircle;
			fixtureDef.density = 1.0f;
			fixtureDef.friction = 0.0f;
			fixtureDef.restitution = 1;
			body.createFixture(fixtureDef);
			maxForce = body.getMass() * acceleration;
		}

		public void update() {
			// if (target != null)
			// movementStrategy.setTarget(target.getBody().getPosition());
			// desiredVelocity = movementStrategy.desiredVelocity(entity);
			if (target != null) {
				desiredVelocity = movementStrategy.desiredVelocity(body.getPosition(), target.getBody().getPosition());
			}
			else
				desiredVelocity = movementStrategy.desiredVelocity(body.getPosition(), null);
			if (movementStrategy instanceof StandStillStrategy) {
				desiredVelocity = body.getLinearVelocity().scl(-1);
			}
			body.applyForceToCenter(desiredVelocity.scl(maxForce), true);
			Vector2 vel = body.getLinearVelocity();
			if (vel.len() > MAX_VELOCITY) {
				float scl = vel.len() / MAX_VELOCITY;
				vel.scl(MAX_VELOCITY / vel.len());
				body.setLinearVelocity(vel);
			}
		}

		private void setDesiredVelocity() {
			// desiredVelocity = body.getPosition().sub(target.body.getPosition());
			desiredVelocity = target.body.getPosition().sub(body.getPosition());
			desiredVelocity.nor().scl(maxForce);
		}
	}

	abstract static class MovementStrategy {
		abstract Vector2 desiredVelocity(Vector2 position, Vector2 target);
	}

	static class RandomStrategy
			extends MovementStrategy {
		Vector2 desiredVelocity(Vector2 position, Vector2 target) {
			float x = (float) (2 * Math.random() - 1);
			float y = (float) (2 * Math.random() - 1);
			return new Vector2(x, y).nor();
		}
	}

	static class RammingStrategy
			extends MovementStrategy {

		Vector2 desiredVelocity(Vector2 position, Vector2 target) {
			return target.sub(position).nor();
		}
	}

	static class MixedStrategy
			extends MovementStrategy {

		Vector2 desiredVelocity(Vector2 position, Vector2 target) {
			if (position.dst(target) > CLOSE_RANGE_THRESHOLD)
				return target.sub(position).nor();
			else
				return target.sub(position).rotate(90).nor();
		}
	}

	static class FleeingStrategy
			extends MovementStrategy {

		Vector2 desiredVelocity(Vector2 position, Vector2 target) {
			return target.sub(position).scl(-1).nor();
		}
	}

	static class StandStillStrategy
			extends MovementStrategy {

		Vector2 desiredVelocity(Vector2 position, Vector2 target) {
			return new Vector2(0, 0);
		}
	}

	public static final int CLOSE_RANGE_THRESHOLD = 50;

	/** The max velocity of any entity */
	public static final float MAX_VELOCITY = 50f;

}
