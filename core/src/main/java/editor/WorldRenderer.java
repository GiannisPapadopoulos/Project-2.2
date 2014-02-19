package editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WorldRenderer {

	private EditorData data;

	private static final int CELL_SIZE = 20;
	private static final int LINE_WIDTH = 1;
	private static final Color gridColor = Color.GREEN;

	private ShapeRenderer gridRenderer;

	public WorldRenderer(EditorData data) {
		this.data = data;
		this.gridRenderer = new ShapeRenderer();
	}

	public void render(OrthographicCamera cam) {
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
		
		gridRenderer.begin(ShapeType.Filled);
		for (int i = 0; i < data.getHeight(); i++)
			for (int j = 0; j < data.getWidth(); j++) {
				gridRenderer.setColor(data.requestColor(i,j));
				gridRenderer.rect(i * CELL_SIZE + 1, j * CELL_SIZE +1, CELL_SIZE-2, CELL_SIZE-2);
			}

		gridRenderer.end();
	}

}
