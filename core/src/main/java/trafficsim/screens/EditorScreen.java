package trafficsim.screens;

import trafficsim.TrafficSimWorld;
import trafficsim.factories.EntityFactory;
import trafficsim.roads.Road;
import trafficsim.systems.RenderSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;

import editor.EditorData;
import editor.PointsOfInterest;
import editor.PointsOfInterest.PointOfInterest;
import editor.WorldRenderer;
import graph.Graph;

public class EditorScreen extends SuperScreen {

	private TrafficSimWorld w;

	private WorldRenderer wr;
	private EditorData ed;

	private PointsOfInterest POI;

	public EditorScreen(Screens screens) {
		super(screens);
	}

	@Override
	public void show() {
		w = new TrafficSimWorld();
		w.setSystem(new RenderSystem(getCamera()));
		w.initialize();
		EntityFactory.populateWorld(w, getScreens().getSimulationScreen()
				.getWorld().getGraph());
		ed = new EditorData(1000, 1000, -100, -100);
		wr = new WorldRenderer(ed);
		updatePOI(w.getGraph());
	}

	private void updatePOI(Graph<Road> graph) {
		POI = new PointsOfInterest(graph);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		getMousePosition().update(getCamera());

		getCamera().update();

		w.setDelta(delta);
		w.process();

		wr.renderGrid(getCamera());
		wr.renderGridUnderMouse(getCamera(), mousePosition.getGrid().getX(),
				mousePosition.getGrid().getY());

		wr.renderPOI(getCamera(), POI, getMousePosition().updateClosestPOI(POI));

		getWorldLayer().act(delta);
		getUILayer().act(delta);
		getWorldLayer().draw();
		getUILayer().draw();

	}

	@Override
	public void populateUILayer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateWorldLayer() {
		// TODO Auto-generated method stub

	}

	public void notifyEditorClicked(int button) {
		// I clicked for the first time (Start of the road)
		if (ed.getFirstClick() == null)
			// I clicked on existing POI
			if (getMousePosition().getClosestPOI() != null)
				ed.setFirstClick(getMousePosition().getClosestPOI());
			// I clicked outside existing POI structure -> create new
			else {
				ed.setFirstClick(POI.registerNewPOI(new Vector2(
						getMousePosition().getAbsolute().getX(),
						getMousePosition().getAbsolute().getY())));
			}
		// I clicked for the second time (End of the road)
		else {
			// I clicked on existing POI
			if (getMousePosition().getClosestPOI() != null)
				// Check if First POI != Second POI
				if (ed.getFirstClick() != getMousePosition().getClosestPOI())
					ed.setSecondClick(getMousePosition().getClosestPOI());
				else {
					// Do nothing...
				}
			// I clicked outside existing POI structure -> create new
			else {
				ed.setSecondClick(POI.registerNewPOI(new Vector2(
						getMousePosition().getAbsolute().getX(),
						getMousePosition().getAbsolute().getY())));
			}
		}

		if (ed.getFirstClick() != null && ed.getSecondClick() != null) {
			EntityFactory.populateWorld(
					w,
					POI.createGraphObject(ed.getFirstClick(),
							ed.getSecondClick()));
			ed.setFirstClick(null);
			ed.setSecondClick(null);
		}

	}

}
