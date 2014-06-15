package trafficsim.screens;

import static trafficsim.TrafficSimConstants.*;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import trafficsim.TrafficSimWorld;
import trafficsim.systems.InputEditorSystem;
import trafficsim.systems.InputSystem;
import trafficsim.systems.MovementSystem;
import ui.tables.SidePanels;
import utils.Stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import editor.MousePosition;

@Getter
@Setter
public abstract class SuperScreen implements Screen {

	private Screens screens;

	private OrthographicCamera camera;

	@Getter
	protected Stage UILayer;

	@Getter
	protected Stage worldLayer;

	InputMultiplexer multiplexer;

	protected MousePosition mousePosition;

	@Getter
	private SidePanels sidePanels;

	public SuperScreen(Screens screens) {
		this.screens = screens;

		this.worldLayer = new Stage();

		this.UILayer = new Stage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false, worldLayer.getSpriteBatch());
		setCamera(new OrthographicCamera(WINDOW_WIDTH * WORLD_TO_BOX,
				WINDOW_HEIGHT * WORLD_TO_BOX));

		initMultiplexer();

		if (this instanceof EditorScreen)
			this.multiplexer.addProcessor(new InputEditorSystem(getCamera(),
					(EditorScreen) this));

		else
			this.multiplexer.addProcessor(new InputSystem(this));

		this.mousePosition = new MousePosition(100, 100);
		populateCommonLayers();
	}

	protected void initMultiplexer() {
		this.multiplexer = new InputMultiplexer(UILayer, worldLayer);
	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void resize(int width, int height) {
		getUILayer().setViewport(width, height, true);
		getWorldLayer().setViewport(width, height, true);
	}

	@Override
	public void show() {
		System.out.println("called");
		// System.out.println(multiplexer.getProcessors());
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	public void switchToScreen(SuperScreen screen) {
		screens.getTrafficSimulation().setScreen(screen);
		setInputScreen(screen);
	}

	private void setInputScreen(SuperScreen screen) {
		Gdx.input.setInputProcessor(screen.getMultiplexer());
	}

	public abstract void populateUILayer();

	public abstract void populateWorldLayer();

	public void passWorld(TrafficSimWorld world) {
		sidePanels.passWorld(world);
	}

	public void setCarsUI(TrafficSimWorld world) {

		sidePanels.getCarsOnRoadLabel().setText(
				Integer.toString(world.getSystem(MovementSystem.class)
						.getTotalCars()));

	}

	public void setWaitTimeUI(TrafficSimWorld world) {

		int timewaited = (int) (Stats.mean(world.getDataGatherer().getAveragePercentageStopped())*100);
	
		sidePanels.getAverageLightTime().setText(
				Integer.toString(timewaited) + "%");
	}

	private void populateCommonLayers() {

		sidePanels = new SidePanels();

		getUILayer().addActor(sidePanels);

		for (val button : sidePanels.getTransitionButtons().getButtons())
			button.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if (actor.getName() == "Simulation")
						switchToScreen(screens.getSimulationScreen());
					else if (actor.getName() == "Statistics")
						switchToScreen(screens.getStatisticsScreen());
				}
			});

		sidePanels.getEditMode().addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {

				sidePanels.getSwitchPanel().clearChildren();
				sidePanels.getSwitchPanel().add(sidePanels.getEditorPanel());
				switchToScreen(screens.getEditorScreen());
				// sidePanels.

			}
		});

//		sidePanels.getSimTools().addListener(new ClickListener() {
//
//			public void clicked(InputEvent event, float x, float y) {
//
//				// if(screens.){
//				switchToScreen(screens.getSimulationScreen());
//				// }
//				sidePanels.getSwitchPanel().clearChildren();
//				sidePanels.getSwitchPanel().add(sidePanels.getWorldVariables());
//
//			}
//		});

		populateUILayer();
		populateWorldLayer();
	}
}
