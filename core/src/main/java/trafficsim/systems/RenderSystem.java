package trafficsim.systems;

import java.util.HashMap;

import lombok.Getter;
import trafficsim.components.DimensionComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.PositionComponent;
import trafficsim.components.RouteComponent;
import trafficsim.components.SpriteComponent;
import trafficsim.components.TrafficLightComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class RenderSystem
		extends EntityProcessingSystem {
	@Mapper
	ComponentMapper<TrafficLightComponent> trafficLightMapper;
	@Mapper
	ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	ComponentMapper<PositionComponent> positionMapper;
	@Mapper
	ComponentMapper<SpriteComponent> spriteMapper;
	@Mapper
	ComponentMapper<DimensionComponent> dimensionMapper;
	@Mapper
	ComponentMapper<RouteComponent> routeMapper;

	private HashMap<String, AtlasRegion> regions;
	private TextureAtlas textureAtlas;
	@Getter
	private SpriteBatch batch;
	private OrthographicCamera camera;

	@SuppressWarnings("unchecked")
	public RenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(SpriteComponent.class).one(PhysicsBodyComponent.class, PositionComponent.class));
		this.camera = camera;
		
		OrthographicCamera guicam = new OrthographicCamera();
		float ratew = guicam.viewportWidth/camera.viewportWidth;  //<--- you should calculate these 2 only once.
		float rateh = guicam.viewportHeight/camera.viewportHeight;
	
	}

	@Override
	protected void initialize() {
		regions = new HashMap<String, AtlasRegion>();
		textureAtlas = new TextureAtlas(Gdx.files.internal("assets/packed-textures/textures.pack"),
										Gdx.files.internal("assets/packed-textures"));
		for (AtlasRegion r : textureAtlas.getRegions()) {
			regions.put(r.name, r);
		}

		batch = new SpriteBatch();

	}

	@Override
	protected void begin() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void inserted(Entity e) {
		SpriteComponent spriteComp = spriteMapper.get(e);
		AtlasRegion spriteRegion = regions.get(spriteComp.getName());
		Sprite sprite = new Sprite(spriteRegion);
		sprite.setSize(dimensionMapper.get(e).getLength(), dimensionMapper.get(e).getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		if (physicsBodyMapper.has(e)) {
			sprite.setRotation(physicsBodyMapper.get(e).getAngle() * MathUtils.radDeg);
		}
		spriteComp.setSprite(sprite);
		spriteComp.setSet(true);
	}

	@Override
	protected void process(Entity e) {
		PhysicsBodyComponent physComp = physicsBodyMapper.get(e);
		if (routeMapper.has(e)) {
			RouteComponent routeComp = routeMapper.get(e);
			if (routeComp.isSet()) {
				Vector2 first = new Vector2(706.75f, -150);
				boolean inv = routeComp.isSet() && first.epsilonEquals(routeComp.getWayPoints().get(0), 0.01f)
								&& routeComp.getWayPointIndex() >= 65 && routeComp.getWayPointIndex() <= 75;
				if(inv) {
					// System.out.println("Now you see me, now you don't!");
					return;
				}
			}
			else {
				// System.out.println("unt");
			}
		}

		Vector2 position = getPosition(e);
		if (position != null) {
			 SpriteComponent spriteComp = spriteMapper.get(e);
			if (!spriteComp.isSet()) {
				inserted(e);
			}

			Sprite sprite = spriteComp.getSprite();

			float posX = position.x - sprite.getWidth() / 2;
			float posY = position.y - sprite.getHeight() / 2;

			sprite.setPosition(posX, posY);
			if (physicsBodyMapper.has(e))
				sprite.setRotation(physComp.getAngle() * MathUtils.radDeg);
			spriteComp.getSprite().draw(batch);
		}
	}


	private Vector2 getPosition(Entity e) {
		if (physicsBodyMapper.has(e))
			return physicsBodyMapper.get(e).getPosition();
		else if (positionMapper.has(e))
			return positionMapper.get(e).getPosition();
		return null;
	}


	@Override
	protected void end() {
		batch.end();
	}

}
