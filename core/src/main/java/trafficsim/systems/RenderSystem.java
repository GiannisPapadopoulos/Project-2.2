package trafficsim.systems;

import java.util.HashMap;

import trafficsim.components.DimensionComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.PositionComponent;
import trafficsim.components.SpriteComponent;

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
	ComponentMapper<PhysicsBodyComponent> physicsBodyMapper;
	@Mapper
	ComponentMapper<PositionComponent> positionMapper;
	@Mapper
	ComponentMapper<SpriteComponent> spriteMapper;
	@Mapper
	ComponentMapper<DimensionComponent> dimensionMapper;

	private HashMap<String, AtlasRegion> regions;
	private TextureAtlas textureAtlas;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	@SuppressWarnings("unchecked")
	public RenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(SpriteComponent.class).one(PhysicsBodyComponent.class, PositionComponent.class));
		this.camera = camera;
	}

	@Override
	protected void initialize() {
		regions = new HashMap<String, AtlasRegion>();
		textureAtlas = new TextureAtlas(Gdx.files.internal("assets/packed-textures/textures.pack"),
										Gdx.files.internal("assets/packed-textures"));
		for (AtlasRegion r : textureAtlas.getRegions()) {
			r.flip(true, false);
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
		// System.out.println(spriteComp.getSprite().getBoundingRectangle());
	}

	@Override
	protected void process(Entity e) {
		Vector2 position = getPosition(e);
		if (position != null) {
			SpriteComponent spriteComp = spriteMapper.get(e);
			Sprite sprite = spriteComp.getSprite();

			PhysicsBodyComponent physComp = physicsBodyMapper.get(e);
			// AtlasRegion spriteRegion = regions.get(spriteComp.getName());
			// float scaleX = (dimensionMapper.get(e).getLength() * BOX_TO_WORLD) / spriteRegion.getRegionWidth();
			// float scaleY = (dimensionMapper.get(e).getWidth() * BOX_TO_WORLD) / spriteRegion.getRegionHeight();
			// spriteComp.setScaleX(scaleX);
			// spriteComp.setScaleY(scaleY);

			// float posX = position.x - (spriteRegion.getRegionWidth() / 2 * spriteComp.getScaleX());
			// float posY = position.y - (spriteRegion.getRegionHeight() / 2 * spriteComp.getScaleY());

			float posX = position.x - sprite.getWidth() / 2;
			float posY = position.y - sprite.getHeight() / 2;

			// batch.draw( spriteRegion, posX, posY, 0, 0, spriteRegion.getRegionWidth(),
			// spriteRegion.getRegionHeight(),
			// spriteComp.getScaleX(), spriteComp.getScaleY(), spriteComp.getRotation());
			// spriteComp.getSprite().setBounds(posX, posY, dimensionMapper.get(e).getLength() * BOX_TO_WORLD,
			// dimensionMapper.get(e).getWidth() * BOX_TO_WORLD);
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
