package editor.old;


public class JunkCode {

	/*
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
	*/
}
