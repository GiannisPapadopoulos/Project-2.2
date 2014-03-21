package trafficsim.factories;

import static trafficsim.TrafficSimConstants.WORLD_TO_BOX;
import trafficsim.components.PhysicsBodyComponent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author Giannis Papadopoulos
 */
public class PhysicsBodyFactory {

	public static PhysicsBodyComponent createCarBody(World box2dWorld, Vector2 position) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y).scl(WORLD_TO_BOX);

		Body body = box2dWorld.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(3f, 2f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 1;
		body.createFixture(fixtureDef);

		return new PhysicsBodyComponent(body);
	}

	public static PhysicsBodyComponent createTrafficLightPhys(World box2dWorld, Vector2 position) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(position.x, position.y).scl(WORLD_TO_BOX);

		Body body = box2dWorld.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.2f, .2f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 1;

		return new PhysicsBodyComponent(body);
	}

}
