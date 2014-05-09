package ui.tables;

import lombok.Getter;
import lombok.Setter;
import utils.Assets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class SidePanels extends Table {

	@Getter
	private TransitionButtons transitionButtons = new TransitionButtons();
	@Getter
	private Button showhide, showhide2;
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

	private TextButton carsOnRoadLabel;
	
	private Slider carSpawningRate,speedLimitSlider;
	
	private Label cspawncompanion,slimitcompanion;	
	
	@Getter
	private Table worldStatistics,worldVariables,expandCollapse;	
	
	public SidePanels() {
		
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
		
		worldStatistics.add(carsOnRoadLabel); // cars on road number
		worldStatistics.add(new Label("Cars", Assets.skin)); // cars on road image
		worldStatistics.row();
		worldStatistics.add(new Label(Integer.toString(avtimewait) + "s", Assets.skin)); // average time waited number
		worldStatistics.add(new Label("Av. time waited", Assets.skin)); // average time waited image
		
		
		
		/** Expand - Collapse Table */		
		expandCollapse = new Table();		
		expandCollapse.add(new Label("-------", Assets.skin)); // button with image
		
		
		
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

		
		/** Adding to mainPanel */				
		Table sidepanel = new Table();
		sidepanel.add(worldStatistics).width(300).top(); /// world stats
		sidepanel.row(); 
		sidepanel.add(expandCollapse).width(300); // show hide
		sidepanel.row();
		sidepanel.add(worldVariables).width(300).padLeft(50).padRight(5); // world variables
		
				add(getTransitionButtons()).top().left();
		
		add(sidepanel).expand().top().right();
		
		debug();
	
	
	}



}
