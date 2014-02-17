package trafficsim.systems;

import static trafficsim.TrafficSimConstants.BOX_TO_WORLD;
import static trafficsim.TrafficSimConstants.CAR_LENGTH;
import static trafficsim.TrafficSimConstants.CAR_WIDTH;

import java.util.HashMap;

import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;

public class RenderSystem
		extends EntityProcessingSystem {

	@Mapper
	ComponentMapper<PhysicsBodyComponent> positionMapper;
	@Mapper
	ComponentMapper<SpriteComponent> spriteMapper;

	private HashMap<String, AtlasRegion> regions;
	private TextureAtlas textureAtlas;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	@SuppressWarnings("unchecked")
	public RenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(PhysicsBodyComponent.class, SpriteComponent.class));
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
	protected void process(Entity e) {
		if (positionMapper.has(e)) {
			Vector2 position = positionMapper.getSafe(e).getPosition();
			SpriteComponent sprite = spriteMapper.get(e);

			AtlasRegion spriteRegion = regions.get(sprite.getName());

			float scaleX = (CAR_LENGTH * BOX_TO_WORLD) / spriteRegion.getRegionWidth();
			float scaleY = (CAR_WIDTH * BOX_TO_WORLD) / spriteRegion.getRegionHeight();
			sprite.setScaleX(scaleX);
			sprite.setScaleY(scaleY);

			float posX = position.x - (spriteRegion.getRegionWidth() / 2 * sprite.getScaleX());
			float posY = position.y - (spriteRegion.getRegionHeight() / 2 * sprite.getScaleY());
			batch.draw(	spriteRegion, posX, posY, 0, 0, spriteRegion.getRegionWidth(), spriteRegion.getRegionHeight(), sprite.getScaleX(),
						sprite.getScaleY(), sprite.getRotation());
			// GdxUtils.drawCentered(batch, spriteRegion, position.x, position.y);
		}
	}


	@Override
	protected void end() {
		batch.end();
	}



}
