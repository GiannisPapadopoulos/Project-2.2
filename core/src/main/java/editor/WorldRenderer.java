package editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WorldRenderer {

	private EditorData data;

	public static final float CELL_SIZE = 3f;
	private static final float LINE_WIDTH = 0.1f;
	private static final Color gridColor = Color.GREEN;

	private ShapeRenderer gridRenderer;

	public WorldRenderer(EditorData data) {
		this.data = data;
		this.gridRenderer = new ShapeRenderer();
	}

	public void renderWorld(OrthographicCamera cam) {

	}

	public void renderGrid(OrthographicCamera cam) {
		gridRenderer.setProjectionMatrix(cam.combined);
		gridRenderer.begin(ShapeType.Line);
		Gdx.gl.glLineWidth(LINE_WIDTH);
		gridRenderer.setColor(gridColor);

		float c1 = 0, c2 = 0, c3 = 0, c4 = 0;

		for (int i = 0; i < data.getHeight() + 1; i++) {
			c1 = data.getW_shift() * CELL_SIZE;
			c2 = i * CELL_SIZE + data.getH_shift() * CELL_SIZE;
			c3 = data.getWidth() * CELL_SIZE + data.getW_shift() * CELL_SIZE;
			c4 = i * CELL_SIZE + data.getH_shift() * CELL_SIZE;
			gridRenderer.line(c1, c2, c3, c4);
		}

		for (int j = 0; j < data.getWidth() + 1; j++) {
			c1 = j * CELL_SIZE + data.getW_shift() * CELL_SIZE;
			c2 = data.getH_shift() * CELL_SIZE;
			c3 = j * CELL_SIZE + data.getW_shift() * CELL_SIZE;
			c4 = data.getHeight() * CELL_SIZE + data.getH_shift() * CELL_SIZE;
			gridRenderer.line(c1, c2, c3, c4);
		}
		gridRenderer.end();
	}

	public void renderGridUnderMouse(OrthographicCamera cam, int x, int y) {
		gridRenderer.setProjectionMatrix(cam.combined);
		gridRenderer.begin(ShapeType.Filled);
		gridRenderer.setColor(Color.RED);
		gridRenderer.rect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2,
				CELL_SIZE - 2);
		gridRenderer.end();
	}

}
