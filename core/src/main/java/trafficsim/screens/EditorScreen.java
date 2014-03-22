package trafficsim.screens;

import trafficsim.TrafficSimWorld;
import trafficsim.factories.EntityFactory;
import trafficsim.systems.RenderSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

import editor.EditorData;
import editor.WorldRenderer;

public class EditorScreen extends SuperScreen {

	private TrafficSimWorld w;
	
	private WorldRenderer wr;
	private EditorData ed;
	
	public EditorScreen(Screens screens) {
		super(screens);
	}
	
	@Override
	public void show() {
		w = new TrafficSimWorld();
		w.setSystem(new RenderSystem(getCamera()));
		w.initialize();
		EntityFactory.populateWorld(w, getScreens().getSimulationScreen().getWorld().getGraph());
		ed = new EditorData(1000,1000,-100,-100);
		wr = new WorldRenderer(ed);
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
		wr.renderGridUnderMouse(getCamera(), mousePosition.getGrid().getX(), mousePosition.getGrid().getY());
		
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

}
