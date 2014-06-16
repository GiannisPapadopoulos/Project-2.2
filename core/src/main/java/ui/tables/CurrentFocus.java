package ui.tables;

import trafficsim.components.DataComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import utils.Assets;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class CurrentFocus extends Table {
	
	private Entity entityToRender;
	private boolean car;
	
	private Label currentSpeed,distanceTravelled,averageSpeed,distanceRemaining; //car
	private Label totalCarsPassed; //intersection
	
	private Slider velocity; //car sliders
	private Slider spawnRates; //intersection sliders
	
	private TextButton selfDestruct; //car buttons
	private TextButton priorityTL, timedTL; //intersection buttons
	
	private SidePanels sidepanels; 
	
	
	
	public CurrentFocus(SidePanels sidepanels){
		this.sidepanels = sidepanels;
		
		currentSpeed 	  = new Label ("loading..", Assets.skin);
		distanceTravelled = new Label ("loading..", Assets.skin);
		averageSpeed	  = new Label ("loading..", Assets.skin);
		distanceRemaining = new Label ("loading..", Assets.skin);
		
		sidepanels.setCurrentFocus(this);

	}
	
	public void render(){
		
		if (entityToRender == null || !entityToRender.isActive()) {
			return;
		}

		if(car){
			
			
			/** set speed label */
			Vector2 speed = entityToRender.getComponent(PhysicsBodyComponent.class).getLinearVelocity();
			currentSpeed.setText(Integer.toString((int)Math.max(Math.abs(speed.x), Math.abs(speed.y) ) )+"m/s");
			
			/** set distance traveled */
			int t = (int) entityToRender.getComponent(DataComponent.class).getTotalDistance();
			distanceTravelled.setText(Integer.toString(t) + "m");
			
			/**set average speed*/
			averageSpeed.setText(Float.toString(entityToRender.getComponent(DataComponent.class).getAverageSpeed()));
			
			/**set Distance remaining*/
			distanceRemaining.setText(Float.toString(entityToRender.getComponent(DataComponent.class).getDistanceLeft()));
			
		}
		
		
	}
	
	private boolean isCar(Entity entity) {

		if (entity != null) {

			if (entity.getComponent(MaxSpeedComponent.class) != null) {
				// /System.out.println("car");
				return true;
			}
		}

		else {

		}

		// System.out.println("not car");

		return false;
	}

	public void setEntityToRender(Entity clickedEntity){
		if (clickedEntity.equals(entityToRender))
		{
			entityToRender = null;
		}
		else{
			entityToRender = clickedEntity;
			car = isCar(entityToRender); 
			if(car){
				// Table table = new Table();
				clearChildren();
				add(new Label("Current Velocity: ", Assets.skin));
				add(currentSpeed);
				row();
				add(new Label("Distance Traveled: ", Assets.skin));
				add(distanceTravelled);
				row();
				add(new Label("Average Speed: ", Assets.skin));
				add(averageSpeed);
				row();
				add(new Label("Distance Remaining: ", Assets.skin));
				add(distanceRemaining);
				
				sidepanels.setCurrentFocus(this);
				
			}
			else
			{
				
				System.out.println(" other");
				
			}
			
		}
			
	}
		
		
			
			
			
			
			
			
		
		
		
		
	


}
