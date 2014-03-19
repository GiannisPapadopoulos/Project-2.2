package trafficsim.screens;

import utils.Assets;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class EditorScreen extends SuperScreen {

	private Stage UILayer, worldLayer;

	public EditorScreen(Screens screens) {
		super(screens);
	}

	@Override
	public void show() {

		UILayer = new Stage();
		worldLayer = new Stage();

		InputMultiplexer multiplexer = new InputMultiplexer(UILayer, worldLayer);
		Gdx.input.setInputProcessor(multiplexer);

		Table t = new Table();
		TextButton buyButton = new TextButton("Buy", Assets.skin);

		t.add(buyButton).top().left();

		t.align(Align.top);

		t.setFillParent(true);

		UILayer.addActor(t);

	}

	@Override
	public void resize(int width, int height) {
		UILayer.setViewport(width, height, true);
		worldLayer.setViewport(width, height, true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (getScreens().getSimulationScreen() == null)
			System.out.println("yes");
		
		 
		  getScreens().getSimulationScreen().getCamera().update();
		 

		worldLayer.act(delta);
		UILayer.act(delta);
		worldLayer.draw();
		UILayer.draw();

		
		  try{ World w =
		  ((SimulationScreen)getScreens().getSimulationScreen()).getWorld();
		  w.setDelta(delta); w.process(); } catch (Exception e) {
		  
		  }
		 
	}

}
