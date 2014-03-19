package trafficsim.factories;

import trafficsim.TrafficSimWorld;
import trafficsim.components.Acceleration;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.SpriteComponent;
import trafficsim.components.TrafficLightComponent;
import trafficsim.components.TrafficLightComponent.Status;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * Contains methods to create the different entities used in the world and add the required components
 * 
 * @author Giannis Papadopoulos and primarily Maarten Weber
 */
public class EntityFactory {

	/**
	 * Creates a car with the given position and acceleration
	 * 
	 * @param name Must be the same as the name of the texture file
	 */
	public static Entity createCar(TrafficSimWorld world, Vector2 position, float acceleration, String name) {
		Entity car = world.createEntity();
		PhysicsBodyComponent physComp = PhysicsBodyFactory.createCarBody(world.getBox2dWorld(), position);
		car.addComponent(physComp);

		SpriteComponent sprite = new SpriteComponent(name, 1, 1, 0);
		car.addComponent(sprite);

		car.addComponent(new Acceleration(acceleration));

		return car;
	}
	public static Entity createTrafficLight(TrafficSimWorld world, Vector2 position, int timerG, int timerO, int timerR, String name, Status status){
		Entity trafficLight = world.createEntity();
		PhysicsBodyComponent physComp = PhysicsBodyFactory.createTrafficLightPhys(world.getBox2dWorld(), position);
		trafficLight.addComponent(physComp);
		
		SpriteComponent sprite = new SpriteComponent(name, 1, 1, 0);
		trafficLight.addComponent(sprite);
		
		TrafficLightComponent lightComp =new TrafficLightComponent(timerG, timerO, timerR, status);
		trafficLight.addComponent(lightComp);
	
		
		return trafficLight;
	}

}
