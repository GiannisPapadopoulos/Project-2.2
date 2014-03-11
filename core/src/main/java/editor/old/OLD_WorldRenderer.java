package editor.old;

import graph.Graph;
import graph.Vertex;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class OLD_WorldRenderer {

	private OLD_EditorData data;

	public static final int CELL_SIZE = 20; // No you won't hate me for this
	private static final int LINE_WIDTH = 1;
	private static final Color gridColor = Color.GREEN;

	private ShapeRenderer gridRenderer;

	private Graph graph_debug;

	public OLD_WorldRenderer(OLD_EditorData data) {
		this.data = data;
		this.gridRenderer = new ShapeRenderer();
		//graph_debug = GraphFactory.createGraph(data);
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
				gridRenderer.setColor(data.requestColor(i, j));
				gridRenderer.rect(i * CELL_SIZE + 1, j * CELL_SIZE + 1,
						CELL_SIZE - 2, CELL_SIZE - 2);
			}

		gridRenderer.end();
		DEBUG_render_vertices(cam);
		DEBUG_render_edges(cam);
	}

	public void DEBUG_render_vertices(OrthographicCamera cam) {
		gridRenderer.begin(ShapeType.Filled);
		gridRenderer.setColor(Color.BLACK);

		for (Vertex v : graph_debug.getVertices()) {
			int vert = 0;
			int hori = 0;
			for (Point p : v.getData().getGridPositions()) {
				vert += p.x;
				hori += p.y;
			}
			vert = (vert * CELL_SIZE) / v.getData().getGridPositions().size()
					+ CELL_SIZE / 4;
			hori = (hori * CELL_SIZE) / v.getData().getGridPositions().size()
					+ CELL_SIZE / 4;

			gridRenderer.rect(vert, hori, CELL_SIZE / 2, CELL_SIZE / 2);
		}
		gridRenderer.end();
	}

	public void DEBUG_render_edges(OrthographicCamera cam) {
		gridRenderer.begin(ShapeType.Line);
		gridRenderer.setColor(Color.BLACK);
		Gdx.gl.glLineWidth(LINE_WIDTH + 2);

		for (Vertex v1 : graph_debug.getVertices())
			for (Vertex v2 : v1.getAdjacentVertices()) {
				int vert1 = 0;
				int hori1 = 0;
				for (Point p : v1.getData().getGridPositions()) {
					vert1 += p.x;
					hori1 += p.y;
				}
				vert1 = (vert1 * CELL_SIZE)
						/ v1.getData().getGridPositions().size() + CELL_SIZE
						/ 2;
				hori1 = (hori1 * CELL_SIZE)
						/ v1.getData().getGridPositions().size() + CELL_SIZE
						/ 2;

				int vert2 = 0;
				int hori2 = 0;
				for (Point p : v2.getData().getGridPositions()) {
					vert2 += p.x;
					hori2 += p.y;
				}
				vert2 = (vert2 * CELL_SIZE)
						/ v2.getData().getGridPositions().size() + CELL_SIZE
						/ 2;
				hori2 = (hori2 * CELL_SIZE)
						/ v2.getData().getGridPositions().size() + CELL_SIZE
						/ 2;

				gridRenderer.line(vert1, hori1, vert2, hori2);

			}
		gridRenderer.end();

	}

}
