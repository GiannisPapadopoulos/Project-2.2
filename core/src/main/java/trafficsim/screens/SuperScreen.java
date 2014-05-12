package trafficsim.screens;

import static trafficsim.TrafficSimConstants.WINDOW_HEIGHT;
import static trafficsim.TrafficSimConstants.WINDOW_WIDTH;
import static trafficsim.TrafficSimConstants.WORLD_TO_BOX;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import utils.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import editor.MousePosition;

@Getter
@Setter
public abstract class SuperScreen implements Screen {

	private Screens screens;

	private OrthographicCamera camera;

	@Getter
	private Stage UILayer;

	@Getter
	private Stage worldLayer;

	@Getter
	InputMultiplexer multiplexer;

	@Getter
	MousePosition mousePosition;

	public SuperScreen(Screens screens) {
		this.screens = screens;
		this.UILayer = new Stage();
		this.worldLayer = new Stage();
		setCamera(new OrthographicCamera(WINDOW_WIDTH * WORLD_TO_BOX,
				WINDOW_HEIGHT * WORLD_TO_BOX));
		
		initMultiplexer();
		
		// if (this instanceof EditorScreen)
		// this.multiplexer.addProcessor(new InputEditorSystem(getCamera(), (EditorScreen) this));
		// else
		// this.multiplexer.addProcessor(new InputSystem(this));
		
		populateCommonLayers();
		this.mousePosition = new MousePosition(100, 100);
	}

	protected void initMultiplexer() {
		this.multiplexer = new InputMultiplexer(UILayer, worldLayer);
	}

	@Override
	public void resize(int width, int height) {
		getUILayer().setViewport(width, height, true);
		getWorldLayer().setViewport(width, height, true);
	}

	@Override
	public void show() {
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

	private void populateCommonLayers() {
		Table t = new Table();
		ArrayList<TextButton> buttons = new ArrayList<TextButton>();

		TextButton simButton = new TextButton("Simulation", Assets.skin);
		buttons.add(simButton);
		simButton.setName("Simulation");
		TextButton ediButton = new TextButton("Editor", Assets.skin);
		buttons.add(ediButton);

		ediButton.setVisible(true);
		ediButton.setName("Editor");
		TextButton staButton = new TextButton("Statistics", Assets.skin);
		buttons.add(staButton);
		staButton.setName("Statistics");

		t.add(simButton).top().left();
		t.add(ediButton).top().left();
		t.add(staButton).top().left();

		t.align(Align.top);

		t.setFillParent(true);

		getUILayer().addActor(t);

		for (val button : buttons)
			button.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if (actor.getName() == "Simulation")
						switchToScreen(screens.getSimulationScreen());
					else if (actor.getName() == "Editor")
						switchToScreen(screens.getEditorScreen());
					else if (actor.getName() == "Statistics")
						switchToScreen(screens.getStatisticsScreen());
				}
			});

		populateUILayer();
		populateWorldLayer();
	}
}
