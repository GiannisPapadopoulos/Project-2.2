package ui.tables;

import lombok.Getter;
import lombok.Setter;
import utils.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SidePanels extends Table {

	@Getter
	private TransitionButtons transitionButtons = new TransitionButtons();
	@Getter
	private Button showhide;
	@Getter
	@Setter
	private int rightpanelwidth;
	
	@Getter
	private Table worldStatistics,bottomPanel,corner,empty;
	

	public SidePanels() {
		
		

		showhide = new Button( Assets.skin);
		showhide.addListener(new ClickListener(){
				
			public void clicked (InputEvent event, float x, float y) {
			
	           worldStatistics.setVisible(!worldStatistics.isVisible());
	           bottomPanel.setVisible(!bottomPanel.isVisible());
	           corner.setVisible(!corner.isVisible());
	           empty.setVisible(!empty.isVisible());
	           
	           
	        }
		
			
		});
		// root table initialization

		this.setFillParent(true);

		// Labels

		// LabelAssets.skin Assets.skin = new LabelAssets.skin(Constants.white,
		// Color.WHITE);

		Label worldLabel = new Label("World", Assets.skin);
		Label statisticsLabel = new Label("Statistics:", Assets.skin);

		// Skin skin = new Skin(Gdx.files.internal("assets/ui/uiskin.atlas"));

		Label showHide = new Label("show/hide", Assets.skin);
		Label emptyLabel = new Label(" ", Assets.skin);

		// UI appearance width / heights
		rightpanelwidth = 300;

		/** UI Table Backgrounds */

		add(getTransitionButtons()).top().left();

		 worldStatistics = new Table();

		Sprite bgc = new Sprite();

		worldStatistics.setBackground(getTableBackground("right"));

		worldStatistics.add(worldLabel);
		worldStatistics.row();
		worldStatistics.add(statisticsLabel);
		worldStatistics.row();
		worldStatistics.add(emptyLabel);
		worldStatistics.row();

		// Cars on road
		Table carsOnRoad = new Table();
		Label carsOnRoadLabel = new Label("Travelling Cars: ", Assets.skin);
		Integer numberOfCurrentCars = 20;
		Label carsOnRoadNumber = new Label(numberOfCurrentCars.toString(),
				Assets.skin);
		carsOnRoad.add(carsOnRoadLabel).expandX();
		carsOnRoad.add(carsOnRoadNumber).width(100);
		worldStatistics.add(carsOnRoad);

		add(worldStatistics).width(rightpanelwidth).top(); // TODO ROOT TABLE
															// ADD

		row();
		 empty = new Table();
		empty.setBackground(getTableBackground("right"));

		// Show - Hide Table ( TODO show-hide button)
		Table showHidePanelButton = new Table();

		showHidePanelButton.add(showhide);
		add(showHidePanelButton).expand().bottom().right(); // TODO ROOT TABLE
															// ADD

		add(empty).width(rightpanelwidth).top();

		//
		// Table bottomHeading = new Table(); // done
		// bottomHeading.add(worldSimVarLabel);
		// add(bottomHeading); // TODO ROOT TABLE ADD

		row();

		bottomPanel = new Table();
		Table fourSplitPanel = new Table();
		Table leftSplit = new Table();
		Table midoneSplit = new Table();
		Table midtwoSplit = new Table();
		Table rightSplit = new Table();

		leftSplit.setBackground(getTableBackground("bottom"));

		Label carSpawnLabel = new Label("Car Spawning Rate", Assets.skin);
		Label speedLimitLabel = new Label("Speed Limit", Assets.skin);
		Slider carSpawningRate = new Slider(10, 20, 30, false, Assets.skin);
		Slider speedLimitSlider = new Slider(10, 20, 30, false, Assets.skin);

		leftSplit.add(carSpawnLabel);
		leftSplit.row();
		leftSplit.add(carSpawningRate);
		leftSplit.row();
		leftSplit.add(speedLimitLabel);
		leftSplit.row();
		leftSplit.add(speedLimitSlider);

		midoneSplit.setBackground(getTableBackground("bottom"));

		Label focusOnTargetLabel = new Label("Focus On Target", Assets.skin);
		CheckBox focusOnTarget = new CheckBox("Focus on Target", Assets.skin);
		// Label carSpawnLabel = new Label("Car Spawning Rate", Assets.skin);
		// Slider speedLimitSlider = new Slider(10, 20, 30, false, Assets.skin);

		//
		// midoneSplit.add(focusOnTargetLabel);
		// midoneSplit.row();
		midoneSplit.add(focusOnTarget);

		midtwoSplit.setBackground(getTableBackground("bottom"));
		rightSplit.setBackground(getTableBackground("bottom"));

		fourSplitPanel.add(leftSplit);
		fourSplitPanel.add(midoneSplit);
		fourSplitPanel.add(midtwoSplit);
		fourSplitPanel.add(rightSplit);

		bottomPanel.add(fourSplitPanel).fill().expandX();

		add(bottomPanel).height(200); // TODO ROOT TABLE ADD

		// CurrentFocus
		// Label currentFocusHeading = new Label("Current Focus:", Assets.skin);
		corner = new Table();
		corner.setBackground(getTableBackground("right"));
		add(corner).width(rightpanelwidth).top();
		;

		// debug();

	}

	private TextureRegionDrawable getTableBackground(String name) {
		Texture texture = new Texture("assets/ui/" + name
				+ "panelbackground.png");
		TextureRegion region = new TextureRegion(texture, 0, 0,
				texture.getWidth(), texture.getHeight());
		return new TextureRegionDrawable(region);

	}

}
