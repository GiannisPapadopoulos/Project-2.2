package ui.tables;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import trafficsim.TrafficSimConstants;
import trafficsim.TrafficSimWorld;
import trafficsim.systems.AbstractToggleStrategy;
import trafficsim.systems.GroupedTrafficLightSystem;
import trafficsim.systems.ManageSpawnRateChangeSystem;
import trafficsim.systems.ManageSpeedLimitChangeSystem;
import utils.Assets;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class SidePanels extends Table {

	@Getter
	private TransitionButtons transitionButtons = new TransitionButtons();
	@Getter
	private Button showhide, showhide2, simTools, editMode,carVisibility,deletingTool;
	@Getter
	@Setter
	private int cspawnmax,
				cspawnmin,
				defaultstep,
				slimitmax,
				slimitmin,
				carsOnRoad,
				panelwidth,
				avtimewait,
				currentSimPageIndex;		

	@Getter
	private TextButton roundabout, standardRoad, intersection, showhidetext,simleft, simright;
	@Getter
	private Label averageLightTime,carsOnRoadLabel,avgspeed,carsreached, timeElapsed , fps;
	
	@Getter
	@Setter
	private TrafficSimWorld world;
	
	private Slider carSpawningRate,speedLimitSlider;
	
	private Label cspawncompanion,slimitcompanion;	
	private BitmapFont black;
	
	@Getter
	private Table worldStatistics,worldVariables,expandCollapse, switchPanel, editorPanel,sidepanel,hide,fourthPage;	
	
	private ArrayList<Table> simPageList;
	private TextButton weightedTL;
	private TextButton timedTL;
	private TextButton thirdTL;
	private ButtonGroup traffbg;
	
	public SidePanels() {
		currentSimPageIndex=0;
		
		simPageList = new ArrayList<Table>();
		this.world = new TrafficSimWorld();
		TextureAtlas atlas = new TextureAtlas("assets/packed-textures/ui-textures.pack");
	//	TextStyle style = new TextStyle();
		black = new BitmapFont(Gdx.files.internal("assets/myanmar_black.fnt"));
		LabelStyle style = new LabelStyle();
		style.font = black;
		defaultstep = 1;
		cspawnmin = 0;
		cspawnmax = 20;

		slimitmin =	10;
		slimitmax = 120;	
		
		panelwidth = 300;

		carSpawningRate = new Slider(cspawnmin+1, cspawnmax, defaultstep, false, Assets.skin);
		speedLimitSlider = new Slider(slimitmin, slimitmax, defaultstep*10, false, Assets.skin);
		
		speedLimitSlider.setValue(TrafficSimConstants.CITY_SPEED_LIMIT);
		
		
		/** Companions (numbers next to sliders) */
		cspawncompanion = new Label(Integer.toString((int) carSpawningRate.getValue()), Assets.skin);
		carSpawningRate.addListener(new ChangeListener() {

			public void changed(ChangeEvent event, Actor actor) {
				cspawncompanion.setText(Integer.toString((int) carSpawningRate.getValue()));
				TrafficSimConstants.spawnRate = carSpawningRate.getValue() * 1000;
				world.getSystem(ManageSpawnRateChangeSystem.class).setSpawnRateModified(true);
			}
		});

		slimitcompanion = new Label(Integer.toString((int) speedLimitSlider.getValue()), Assets.skin);
		speedLimitSlider.addListener(new ChangeListener(){

			public void changed(ChangeEvent event, Actor actor) {
				slimitcompanion.setText(Integer.toString((int) speedLimitSlider.getValue()));

				float newSpeedLimit = speedLimitSlider.getValue() / 3.6f;
				TrafficSimConstants.setCITY_SPEED_LIMIT(newSpeedLimit);
				ManageSpeedLimitChangeSystem system = world.getSystem(ManageSpeedLimitChangeSystem.class);
				system.setSpeedLimitModified(true);
			}
		});
		
	
		
		
		this.setFillParent(true);
		
	//	carsOnRoad = world.getSystem(MovementSystem.class).;		
		carsOnRoadLabel = new Label(Integer.toString(0), Assets.skin);
		averageLightTime = new Label("not updated",Assets.skin);
		avgspeed = new Label("not updated",Assets.skin);
		fps = new Label("not updated", Assets.skin);
		carsreached = new Label("not updated", Assets.skin);
		timeElapsed = new Label("not updated", Assets.skin);
		avtimewait = 30;
		
		
		/** World Statistics Table */
		Label worldStatsLabel = new Label("World Statistics",style);
		
		worldStatistics = new Table();
		worldStatistics.add(worldStatsLabel);
		worldStatistics.row();
		worldStatistics.add(new Label("Cars On Road", Assets.skin)).left(); // cars on road image
		worldStatistics.add(carsOnRoadLabel); // cars on road number
		worldStatistics.row();
		worldStatistics.add(new Label("Average % of route waited: ", Assets.skin)).left(); // average time waited image
		worldStatistics.add(averageLightTime); // average time waited number
	
	

		
		/** Expand - Collapse Table */	
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		simTools = createButton("adjust", atlas);
		editMode = createButton("editor", atlas);
		simTools.setChecked(false);
	
		buttonGroup.add(simTools);
		buttonGroup.add(editMode);
	
		simTools.addListener(new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) {
				
				switchPanel.clearChildren();
				switchPanel.add(worldVariables);
				
				}
		    }
			
		);
		
		expandCollapse = new Table();		
		expandCollapse.add(simTools).size(80); // button with image		
		expandCollapse.add(editMode).size(80);
		expandCollapse.row();
		
	Label worldVariablesLabel = new Label("Simulation Tools",style);
		

		simleft = new TextButton("<", Assets.skin);
		
		simleft.addListener(new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) {
				
				prevSimPage();
				
				}
		    }
			
		);
		
		simright = new TextButton(">", Assets.skin);
		
		simright.addListener(new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) {
				
				nextSimPage();
				
				}
		    }
			
		);
		Table simt = new Table();

		
		
		expandCollapse.add(simleft);
		expandCollapse.add(worldVariablesLabel);
		expandCollapse.add(simright);
		
		/** World Variables Table */		
		worldVariables = new Table();
		worldVariables.row();
		worldVariables.add(new Label("Car Spawning Rate", Assets.skin)); // first adjusting tool
		worldVariables.row();
		worldVariables.add(carSpawningRate).left();
		worldVariables.add(cspawncompanion).width(90).left();
		worldVariables.row();
		
		worldVariables.add(new Label("Speed Limit", Assets.skin)); // second adjusting tool
		worldVariables.row();
		worldVariables.add(speedLimitSlider).left();
		worldVariables.add(slimitcompanion).width(90).left();
		worldVariables.row();
		
		
		/** Switch Panel */
		
		switchPanel = new Table();
		switchPanel.add(worldVariables);
		
		/** Second Page Sim Panel */
		
		weightedTL = new TextButton("Priority Traffic Lights", Assets.skin);
		timedTL = new TextButton("Timed Traffic Lights", Assets.skin);
		thirdTL = new TextButton("Third Traffic Lights", Assets.skin);
		
		weightedTL.setChecked(true);
		
		traffbg = new ButtonGroup();
		
		traffbg.add(weightedTL);
		traffbg.add(timedTL);
		traffbg.add(thirdTL);
		
		weightedTL.addListener(new TrafficButtonListener(AbstractToggleStrategy.priorityToggleStrategy));
		timedTL.addListener(new TrafficButtonListener(AbstractToggleStrategy.basicToggleStrategy));

		Table secondPage = new Table();
		
		
		secondPage.add(new Label("Traffic Light Systems",style));
		secondPage.row();
		secondPage.add(weightedTL);
		secondPage.row();
		secondPage.add(timedTL);
		secondPage.row();
		secondPage.add(thirdTL);
		
		/** Third Page Sim Panel*/
		
		Table thirdPage = new Table();
		
		
		thirdPage.add(new Label("Time Elapsed", Assets.skin));
		thirdPage.add(timeElapsed);
		thirdPage.row();
		
		thirdPage.add(new Label("Average Speed: ", Assets.skin));
		thirdPage.add(avgspeed);
		thirdPage.row();
		thirdPage.add(new Label("FPS ", Assets.skin));
		thirdPage.add(fps);
		thirdPage.row();
		thirdPage.add(new Label("Reached Destination: ", Assets.skin));
		thirdPage.add(carsreached);

		
	
		/** Fourth Page Sim Panel (Current Focus) */ 
		
		fourthPage = new Table();
		fourthPage.add(new Label("test", Assets.skin));
		
		
		/** Editor Panel */
		
		editorPanel = new Table();
		
		deletingTool = createButton("deletingtool", atlas);
		carVisibility = createImage("carsonroad", atlas);
		roundabout = new TextButton("Roundabout", Assets.skin);
		standardRoad = new TextButton("Standard Road", Assets.skin);
		
		
		editorPanel.add(deletingTool).size(80);
		editorPanel.row();
		editorPanel.add(roundabout);
		editorPanel.row();
		editorPanel.add(standardRoad);
		
		/** Adding to mainPanel */				
		sidepanel = new Table();
		
		hide = new Table();
		
		showhidetext = new TextButton(">", Assets.skin);
		
		hide.add(showhidetext);
		
		hide.addListener(new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) {
				
				sidepanel.setVisible(!sidepanel.isVisible());
				
				if(sidepanel.isVisible()){
					
					showhidetext.setText(">");
					
				}
				else 
					showhidetext.setText("<");				
				}
		    }
			
		);
		
		//sim page lists initialised
		simPageList.add(worldVariables);
		simPageList.add(secondPage);
		simPageList.add(thirdPage);
		
		Drawable background = new TextureRegionDrawable(atlas.findRegion("bg"));
		
		sidepanel.setBackground(background);
	
		sidepanel.add(worldStatistics).width(panelwidth).top();/// world stats
		
		sidepanel.row(); 
		sidepanel.add(expandCollapse); // show hide
		sidepanel.row();
		sidepanel.add(switchPanel).width(panelwidth);//.padLeft(50).padRight(5); // world variables

		add(getTransitionButtons()).expand().top().left();
		
		//add(new TextButton("x" , Assets.skin)).right().width(20);
		
		add(sidepanel).top().right();
		add(hide).top().right();
	
		debug();
	}
	public void setCurrentFocus(Table table) {
		if (simPageList.size() >= 4)
			simPageList.remove(3);
		simPageList.add(table);
		
	}
	
	private Button createButton(String name, TextureAtlas atlas){

		Drawable up = new TextureRegionDrawable( atlas.findRegion(name + "_normal"));
		Drawable hover = new TextureRegionDrawable( atlas.findRegion(name + "_pressed"));

		ButtonStyle style = new ButtonStyle();
		
		style.over = hover;
		style.up = up;
		style.checked = hover;

		return new Button(style);
		
	}
	
	public void passWorld(TrafficSimWorld world){
		//this.world = world;
		
	}
	
	
	private Button createImage(String name, TextureAtlas atlas){

		Drawable up = new TextureRegionDrawable( atlas.findRegion(name + "_normal"));

		ButtonStyle style = new ButtonStyle();
		
		style.up = up;
		return new Button(style);

	}
	
	private void nextSimPage(){
		if(currentSimPageIndex < simPageList.size()-1){
		
		currentSimPageIndex++;
		
		switchPanel.clearChildren();
		switchPanel.add(simPageList.get(currentSimPageIndex));
		}
				
		
	}
	
private void prevSimPage(){
	
		if(currentSimPageIndex > 0){
			currentSimPageIndex--;
		
		
		switchPanel.clearChildren();
		switchPanel.add(simPageList.get(currentSimPageIndex));
				
		}
	}
	
	@AllArgsConstructor
	private class TrafficButtonListener
			extends ClickListener {
		
		private AbstractToggleStrategy toggleStrategy;

		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (world != null) {
				GroupedTrafficLightSystem trafficSystem = world.getSystem(GroupedTrafficLightSystem.class);
				if (trafficSystem != null) {
					trafficSystem.setToggleStrategy(toggleStrategy);
				}
			}
			else {
				System.out.println("World is not set");
			}
	    }
	}

	public void setFourthPanelEntity(Entity entity) {
		
		
	}





}
