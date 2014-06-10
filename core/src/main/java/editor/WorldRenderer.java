package editor;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

import paramatricCurves.ParametricCurve;
import paramatricCurves.curveDefs.C_Circular;
import paramatricCurves.curveDefs.C_Linear;
import trafficsim.TrafficSimConstants;
import trafficsim.roads.CrossRoad;
import trafficsim.roads.Lane;
import trafficsim.roads.Road;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

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

	public void renderPOI(OrthographicCamera cam, PointsOfInterest POI,
			PointsOfInterest.PointOfInterest closestPOI) {
		gridRenderer.setProjectionMatrix(cam.combined);
		gridRenderer.begin(ShapeType.Filled);
		gridRenderer.setColor(Color.BLUE);
		for (int i = 0; i < POI.getPOI().size(); i++) {
			if (closestPOI != null && closestPOI == POI.getPOI().get(i))
				gridRenderer.setColor(Color.PINK);
			gridRenderer.rect(POI.getPOI().get(i).getPosition().x - 0.5f, POI
					.getPOI().get(i).getPosition().y - 0.5f, 1f, 1f);
			gridRenderer.setColor(Color.BLUE);
		}
		gridRenderer.end();
	}


}
