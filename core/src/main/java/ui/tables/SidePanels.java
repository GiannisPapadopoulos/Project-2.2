package ui.tables;

import lombok.Getter;
import lombok.Setter;
import utils.Assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
				avtimewait;		

	private TextButton carsOnRoadLabel, roundabout, standardRoad, intersection;
	
	private Slider carSpawningRate,speedLimitSlider;
	
	private Label cspawncompanion,slimitcompanion;	
	
	@Getter
	private Table worldStatistics,worldVariables,expandCollapse, switchPanel, editorPanel;	
	
	public SidePanels() {
		
		TextureAtlas atlas = new TextureAtlas("assets/packed-textures/ui-textures.pack");
		
		defaultstep = 1;
		cspawnmin = 0;
		cspawnmax = 100;	
		slimitmin =	10;
		slimitmax = 120;	
		
		panelwidth = 300;

		carSpawningRate = new Slider(cspawnmin, cspawnmax, defaultstep, false, Assets.skin);
		speedLimitSlider = new Slider(slimitmin, slimitmax, defaultstep*10, false, Assets.skin);
		
		
		/** Companions (numbers next to sliders) */		
		cspawncompanion = new Label(Integer.toString((int)carSpawningRate.getValue()), Assets.skin);		
		carSpawningRate.addListener(new ChangeListener(){
			
			public void changed(ChangeEvent event, Actor actor) {
				cspawncompanion.setText(Integer.toString((int)carSpawningRate.getValue()));
				}});
		
		slimitcompanion = new Label(Integer.toString((int)speedLimitSlider.getValue()), Assets.skin);		
		speedLimitSlider.addListener(new ChangeListener(){

			public void changed(ChangeEvent event, Actor actor) {
				slimitcompanion.setText(Integer.toString((int)speedLimitSlider.getValue()));
				}});
		
		
		this.setFillParent(true);
		
		carsOnRoad = 5;		
		carsOnRoadLabel = new TextButton(Integer.toString(carsOnRoad), Assets.skin);

		avtimewait = 30;
		
		
		/** World Statistics Table */		
		worldStatistics = new Table();
		
		worldStatistics.add(createImage("carsonroad2",atlas)).size(100, 100).padTop(30).right(); // cars on road image
		worldStatistics.add(new Label(Integer.toString(carsOnRoad), Assets.skin)).left(); // cars on road number
		worldStatistics.row();
		worldStatistics.add(new Label(Integer.toString(avtimewait) + "s", Assets.skin)); // average time waited number
		worldStatistics.add(new Label("Av. time waited", Assets.skin)); // average time waited image
		
		/** Switch Panel */
		
		switchPanel = new Table();
		switchPanel.add(worldVariables);

		
		/** Expand - Collapse Table */	
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		simTools = createButton("adjust", atlas);
		editMode = createButton("editor", atlas);
		simTools.setChecked(false);
	
		buttonGroup.add(simTools);
		buttonGroup.add(editMode);
		
//		simTools.addListener(new ClickListener(){
//			
//			public void clicked(InputEvent event, float x, float y) {
//				
//				switchPanel.clearChildren();
//				switchPanel.add(worldVariables);
//				
//				}
//		    }
//			
//		);
//		
//		editMode.addListener(new ClickListener(){
//			
//			public void clicked(InputEvent event, float x, float y) {
//					
//				switchPanel.clearChildren();
//				switchPanel.add(editorPanel);
//				}			
//		});
//		

		expandCollapse = new Table();		
		expandCollapse.add(simTools).size(80); // button with image		
		expandCollapse.add(editMode).size(80);
		
		/** World Variables Table */		
		worldVariables = new Table();

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
		
		
		/** Editor Panel */
		
		editorPanel = new Table();
		
		deletingTool = createButton("deletingtool", atlas);
		carVisibility = createImage("carsonroad", atlas);
		roundabout = new TextButton("Roundabout", Assets.skin);
		standardRoad = new TextButton("Standard Road", Assets.skin);
		
		//editorPanel.add(carVisibility).size(80);
		editorPanel.add(deletingTool).size(80);
		editorPanel.row();
		editorPanel.add(roundabout);
		editorPanel.row();
		editorPanel.add(standardRoad);
		
		/** Adding to mainPanel */				
		Table sidepanel = new Table();
		
		sidepanel.add(worldStatistics).width(panelwidth).top(); /// world stats
		sidepanel.row(); 
		sidepanel.add(expandCollapse).width(panelwidth); // show hide
		sidepanel.row();
		sidepanel.add(switchPanel).width(panelwidth).padLeft(50).padRight(5); // world variables

		add(getTransitionButtons()).expand().top().left();
		
		add(new TextButton("x" , Assets.skin)).right().width(20);
		
		add(sidepanel).top().right();
		
		
		debug();
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
	
	private Button createImage(String name, TextureAtlas atlas){

		Drawable up = new TextureRegionDrawable( atlas.findRegion(name + "_normal"));
	

		ButtonStyle style = new ButtonStyle();
		
		
		style.up = up;
		

		return new Button(style);
		
		
	}



}
