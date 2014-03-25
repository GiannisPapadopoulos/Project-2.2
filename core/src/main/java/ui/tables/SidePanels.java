package ui.tables;

import lombok.Getter;
import utils.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class SidePanels extends Table {
	
	@Getter
	private TransitionButtons transitionButtons = new TransitionButtons();
	
	public SidePanels(){
		
		
				//root table initialization 
				
				this.setFillParent(true);
			
				// Labels
			
				//LabelAssets.skin Assets.skin = new LabelAssets.skin(Constants.white, Color.WHITE);
				
				Label worldLabel = new Label("World", Assets.skin);
				Label statisticsLabel = new Label("Statistics:", Assets.skin);
				
				Label buttonIndicator = new Label("(buttons-sliders here)", Assets.skin);
				Label worldSimVarLabel = new Label("World Simulation:", Assets.skin);
				Label bottomLabel = new Label("World Simulation Variables:", Assets.skin);
				Label showHide =  new Label("show/hide", Assets.skin);
				Label emptyLabel = new Label(" ",Assets.skin);
				
				
				//UI appearance width / heights
				int worldstatswidth = 300;
				
				
				add(getTransitionButtons()).top().left();
				

				
				Table worldStatistics = new Table();
				
				Sprite bgc = new Sprite();
				Texture texture = new Texture("assets/ui/table-background.png");
		        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());   
				
				worldStatistics.setBackground(new TextureRegionDrawable(region));
				
				worldStatistics.add(worldLabel);
				worldStatistics.row();
				worldStatistics.add(statisticsLabel);
				worldStatistics.row();
				worldStatistics.add(emptyLabel);
				worldStatistics.row();
				
				
				//Cars on road
				Table carsOnRoad = new Table();
				Label carsOnRoadLabel = new Label("Travelling Cars: ",Assets.skin);
				Integer numberOfCurrentCars = 20; 
				Label carsOnRoadNumber = new Label(numberOfCurrentCars.toString(), Assets.skin);
				carsOnRoad.add(carsOnRoadLabel).expandX();
				carsOnRoad.add(carsOnRoadNumber).width(100);
				worldStatistics.add(carsOnRoad);
//				
//				
				
				
//				worldStatistics.row();
//				worldStatistics.add(addressLabel);
//				worldStatistics.add(addressText).width(100);
				
				add(worldStatistics).width(worldstatswidth).top(); // TODO ROOT TABLE ADD
//				worldStatistics.row();
//				worldStatistics.row();
//				worldStatistics.add();
				
				row();
				

				//Show - Hide Table ( TODO show-hide button)	
				Table showHidePanelButton = new Table();
				showHidePanelButton.add(showHide); 
				add(showHidePanelButton).expand().bottom().right(); // TODO ROOT TABLE ADD
				row(); 

				Table bottomHeading = new Table(); // done
				bottomHeading.add(worldSimVarLabel);
				add(bottomHeading); 	// TODO ROOT TABLE ADD
    
				row();
			    
			    Table bottomPanel = new Table();
			    
			    bottomPanel.add(buttonIndicator).top();
			    add(bottomPanel).height(200); // TODO ROOT TABLE ADD
			    
			    
			    //CurrentFocus
			    Label currentFocusHeading = new Label("Current Focus:", Assets.skin);
			    add(currentFocusHeading).top();
			     
			    
			    debug();
		
	}


}
