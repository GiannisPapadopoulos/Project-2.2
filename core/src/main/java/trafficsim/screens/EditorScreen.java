package trafficsim.screens;

import trafficsim.TrafficSimWorld;
import trafficsim.systems.RenderSystem;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class EditorScreen extends SuperScreen {

	private TrafficSimWorld w;
	
	public EditorScreen(Screens screens) {
		super(screens);
	}
	
	@Override
	public void show() {
		w = new TrafficSimWorld();
		w.setSystem(new RenderSystem(getCamera()));
		//TODO
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		getCamera().update();

		
		World w = ((SimulationScreen) getScreens().getSimulationScreen())
				.getWorld();
		w.setDelta(delta);
		w.getSystem(RenderSystem.class).process();
		
		

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
