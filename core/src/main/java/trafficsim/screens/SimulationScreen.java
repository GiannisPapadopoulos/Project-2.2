package trafficsim.screens;

import static trafficsim.TrafficSimConstants.*;
import lombok.Getter;
import lombok.Setter;
import trafficsim.TrafficSimWorld;
import trafficsim.data.DataGatherer;
import trafficsim.experiments.InitializeWorld;
import trafficsim.experiments.PredefinedParameters;
import ui.tables.CurrentFocus;
import ui.tables.InfoPop;
import utils.ExportData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import editor.WorldRenderer;

/**
 * The main screen of the simulation
 * 
 * @author Giannis Papadopoulos
 * 
 */
public class SimulationScreen extends SuperScreen {

	@Getter
	// So it's accessible by EditorScreen
	@Setter
	// So it's mutable by EditorScreen
	private TrafficSimWorld world;

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true, false, true, false, true, true);

	private boolean firstTimeSimulationRun = true;
	@Getter
	@Setter
	private InfoPop pop;
	@Getter
	@Setter
	private CurrentFocus focus;

	private WorldRenderer wr;

	// If data have been exported
	private boolean exported = false;

	// Export data after this many seconds
	private int secsToSave = 300;

	@Getter
	@Setter
	// TODO it's not perfectly functional
	private boolean paused;

	public SimulationScreen(Screens screens) {
		super(screens);
	}

	@Override
	public void show() {

		if (world != null)
			world.dispose();
		world = new TrafficSimWorld();
		InitializeWorld.init(world, PredefinedParameters.roundaboutSimpleGraph, this);
	}


	public void initMultiplexer() {
		this.multiplexer = new InputMultiplexer(UILayer, worldLayer);
	}


	@Override
	public void render(float delta) {
		if (isPaused()) {
			return;
		}
		long start;
		if(DEBUG_FPS)
			start = TIMER.getTime();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		getCamera().update();
		world.setDelta(delta);


		world.process();
		if (DEBUG_RENDER)
			debugRenderer.render(world.getBox2dWorld(), getCamera().combined);

		// wr.renderDEBUG(getCamera(), world.getGraph());

		getWorldLayer().act(delta);
		getUILayer().act(delta);
		getWorldLayer().draw();
		getUILayer().draw();
		
		if (DEBUG_TABLES)
			Table.drawDebug(getUILayer());

		if (DEBUG_FPS) {
			long frameTime = TIMER.getTime() - start;
			if (frameTime > 1.1 * DELTA_TIME * 1000)
				System.out.println(frameTime + " milliseconds ");
		}
		pop.render();
		if (focus != null)
			focus.render();
		else {
			// System.out.println("focus is null");
		}
		super.setWaitTimeUI(world);
		super.setCarsUI(world);
		super.setAverageSpeed(world);
		
		if (!exported && TIMER.getTime() > 1.0 * secsToSave * 1000) {
			DataGatherer dataGatherer = world.getDataGatherer();
			ExportData.writeToFile(dataGatherer, "data/simulationData");
			exported = true;

			System.out.println(dataGatherer.getAverageDistanceTravelled().size() + " cars have reached destination");
		}
	}
	

	@Override
	public void populateUILayer() {

	}

	@Override
	public void populateWorldLayer() {

	}

}