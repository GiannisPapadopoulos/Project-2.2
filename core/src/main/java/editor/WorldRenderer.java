package editor;

import java.util.HashMap;

import graph.Graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WorldRenderer {

	private EditorData data;


	public static final int CELL_SIZE = 20;
	private static final int LINE_WIDTH = 1;
	private static final Color gridColor = Color.GREEN;

	private ShapeRenderer gridRenderer;

	private SpriteBatch batch;
	private TextureAtlas textureAtlas;
	private HashMap<String, AtlasRegion> regions;


	public WorldRenderer(EditorData data) {
		this.data = data;
		this.gridRenderer = new ShapeRenderer();
		this.textureAtlas = new TextureAtlas(Gdx.files.internal("assets/textures-editor/pack"), Gdx.files.internal("assets/textures-editor"));
		this.batch = new SpriteBatch();

		regions = new HashMap<String, AtlasRegion>();
		for (AtlasRegion r : textureAtlas.getRegions()) {
			System.out.println(r.name);
			regions.put(r.name, r);
		}

	}

	public void renderWorld(OrthographicCamera cam) {
		gridRenderer.setProjectionMatrix(cam.combined);
		gridRenderer.begin(ShapeType.Line);
		Gdx.gl.glLineWidth(LINE_WIDTH);
		gridRenderer.setColor(gridColor);

		for (int i = 0; i < data.getHeight() + 1; i++)
			gridRenderer.line(0, i * CELL_SIZE, data.getWidth() * CELL_SIZE, i
					* CELL_SIZE);

		for (int j = 0; j < data.getWidth() + 1; j++)
			gridRenderer.line(j * CELL_SIZE, 0, j * CELL_SIZE, data.getHeight()
					* CELL_SIZE);
		gridRenderer.end();

	}

	public void renderGridUnderMouse(OrthographicCamera cam, int x, int y) {
		gridRenderer.setProjectionMatrix(cam.combined);
		gridRenderer.begin(ShapeType.Filled);
		gridRenderer.setColor(Color.RED);
		gridRenderer.rect(x * CELL_SIZE+1, y * CELL_SIZE+1, CELL_SIZE-2, CELL_SIZE-2);
		gridRenderer.end();
		
		

		AtlasRegion spriteRegion = regions.get("crossroad1");
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(spriteRegion, x*CELL_SIZE-spriteRegion.originalHeight/2+CELL_SIZE/2,y*CELL_SIZE-spriteRegion.originalWidth/2+CELL_SIZE/2);
		batch.end();

	}

}
