package editor;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

import paramatricCurves.ParametricCurve;
import trafficsim.TrafficSimConstants;
import trafficsim.roads.CrossRoad;
import trafficsim.roads.CrossRoadTransition;
import trafficsim.roads.Lane;
import trafficsim.roads.NavigationObject;
import trafficsim.roads.Road;
import trafficsim.roads.RoadTransition;

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

	public void renderDEBUG(OrthographicCamera cam,
			Graph<NavigationObject> graph) {
		gridRenderer.setProjectionMatrix(cam.combined);
		gridRenderer.begin(ShapeType.Line);
		gridRenderer.setColor(Color.RED);

		

		for (Vertex v : graph.getVertexList())
			for (RoadTransition rt : ((CrossRoad) v.getData())
					.getCrSubSystems().keySet()) {
				ArrayList<ArrayList<Lane>> lanes = ((CrossRoad) v.getData())
						.getCrSubSystems().get(rt).getLanes();
				for (int i = 0; i < lanes.size(); i++)
					for (int j = 0; j < lanes.get(i).size(); j++) {
						for (Float d : lanes.get(i).get(j).getTrajectory()
								.getR_t().getDiscreteCover(30)) {
							ParametricCurve pc = lanes.get(i).get(j)
									.getTrajectory();
							gridRenderer.rect(pc.getPoint(d).x-TrafficSimConstants.LANE_WIDTH/2,
									pc.getPoint(d).y-TrafficSimConstants.LANE_WIDTH/2,
									TrafficSimConstants.LANE_WIDTH,
									TrafficSimConstants.LANE_WIDTH);
						}
					}

			}

		gridRenderer.setColor(Color.PINK);
		for (Edge e : graph.getEdgeList())
			for (CrossRoadTransition ct : ((Road) e.getData()).getRSubSystems()
					.keySet()) {
				ArrayList<ArrayList<Lane>> lanes = ((Road) e.getData())
						.getRSubSystems().get(ct).getLanes();
				for (int i = 0; i < lanes.size(); i++)
					for (int j = 0; j < lanes.get(i).size(); j++) {
						for (Float d : lanes.get(i).get(j).getTrajectory()
								.getR_t().getDiscreteCover(100)) {
							ParametricCurve pc = lanes.get(i).get(j)
									.getTrajectory();
							gridRenderer.rect(pc.getPoint(d).x-TrafficSimConstants.LANE_WIDTH/2,
									pc.getPoint(d).y-TrafficSimConstants.LANE_WIDTH/2,
									TrafficSimConstants.LANE_WIDTH,
									TrafficSimConstants.LANE_WIDTH);
						}
					}
			}
		
		float blueSize = 0.3f;
		float orangeSize = 0.5f;
		gridRenderer.setColor(Color.BLUE);
		for(Vector2 v: EditorData.debugPoints)
			gridRenderer.rect(v.x-blueSize/2, v.y-blueSize/2, blueSize, blueSize);
		
		gridRenderer.setColor(Color.ORANGE);
		for(Vector2 v: EditorData.debugPoints2)
			gridRenderer.rect(v.x-orangeSize/2, v.y-orangeSize/2, orangeSize, orangeSize);
			
		
		gridRenderer.end();
	}

}
