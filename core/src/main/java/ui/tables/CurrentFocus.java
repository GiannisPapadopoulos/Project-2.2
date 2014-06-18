package ui.tables;

import static trafficsim.TrafficSimConstants.TIMER;
import gnu.trove.list.TIntList;
import graph.EntityIdentificationData;
import graph.EntityIdentificationData.EntityType;
import trafficsim.components.DataComponent;
import trafficsim.components.IntersectionThroughputComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.VehiclesOnRoadComponent;
import utils.Assets;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class CurrentFocus extends Table {
	
	private Entity entityToRender;
	private EntityType entityType; // Vertex, edge or car
	
	private Label currentSpeed,distanceTravelled,averageSpeed,distanceRemaining; //car
	private Label totalCarsPassed; //intersection
	private Label vehiclesOnRoad, averageLaneSpeed; // edge
	
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

		totalCarsPassed = new Label("loading..", Assets.skin);
		vehiclesOnRoad = new Label("loading..", Assets.skin);
		averageLaneSpeed = new Label("loading..", Assets.skin);
		
		sidepanels.setCurrentFocus(this);

	}
	
	public void render(){
		
		if (entityToRender == null || !entityToRender.isActive()) {
			return;
		}

		if (entityType == EntityType.CAR) {
			
			/** set speed label */
			Vector2 speed = entityToRender.getComponent(PhysicsBodyComponent.class).getLinearVelocity();
			currentSpeed.setText(Integer.toString((int)Math.max(Math.abs(speed.x), Math.abs(speed.y) ) )+"m/s");
			
			/** set distance traveled */
			int t = (int) entityToRender.getComponent(DataComponent.class).getTotalDistance();
			distanceTravelled.setText(Integer.toString(t) + "m");
			
			/**set average speed*/
			averageSpeed.setText(String.format("%.2g%n", entityToRender.getComponent(DataComponent.class)
																		.getAverageSpeed()));
			
			/**set Distance remaining*/
			distanceRemaining.setText(String.format("%.2g%n", entityToRender.getComponent(DataComponent.class)
																			.getDistanceLeft()));
		}
		
		else if (entityType == EntityType.VERTEX) {
			float carsPerSecond = 60 * entityToRender.getComponent(IntersectionThroughputComponent.class)
												.getTotalCarsPassed() / (TIMER.getTime() / 1000f);
			totalCarsPassed.setText(String.format("%.3g%n", carsPerSecond));
		}
		else if (entityType == entityType.EDGE) {
			TIntList vehiclesOnLaneIDs = entityToRender.getComponent(VehiclesOnRoadComponent.class).getVehiclesOnLaneIDs();
			int vehicles = vehiclesOnLaneIDs.size();
			vehiclesOnRoad.setText("" + vehicles);
			float averageSpeed = 0;
			for (int i = 0; i < vehiclesOnLaneIDs.size(); i++) {
				Entity car = sidepanels.getWorld().getEntity(vehiclesOnLaneIDs.get(i));
				if (car != null) {
					averageSpeed += car.getComponent(DataComponent.class).getAverageSpeed();
				}
			}
			averageSpeed /= vehiclesOnLaneIDs.size();
			averageLaneSpeed.setText("" + averageSpeed);
		}
		
	}
	
	private EntityType getEntityType(Entity entity) {
		PhysicsBodyComponent physComp = entity.getComponent(PhysicsBodyComponent.class);
		EntityIdentificationData idData = (EntityIdentificationData) physComp.getUserData();
		return idData.getType();
	}

	public void setEntityToRender(Entity clickedEntity){
		if (clickedEntity.equals(entityToRender))
		{
			entityToRender = null;
			return;
		}
		entityToRender = clickedEntity;
		entityType = getEntityType(clickedEntity);
		clearChildren();

		if (entityType == EntityType.CAR) {
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
		}

		else if (entityType == EntityType.VERTEX) {
			add(new Label("Cars passing per minute: ", Assets.skin));
			add(totalCarsPassed);
		}

		else if (entityType == EntityType.EDGE) {
			add(new Label("Vehicles on Lane: ", Assets.skin));
			add(vehiclesOnRoad);
			row();
			add(new Label("Average speed on Lane: ", Assets.skin));
			add(averageLaneSpeed);
		}

		sidepanels.setCurrentFocus(this);
				
			
	}
		
		
			
			
			
			
			
			
		
		
		
		
	


}
