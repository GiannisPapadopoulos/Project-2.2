package trafficsim;

import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;

/** All constants used in the simulation are defined here */
public final class TrafficSimConstants {

	/** Whether textures should be re-packed. Set to true if anything is changed */
	public static final boolean PACK = true;

	/** Frames per second */
	public static final int FPS = 60;

	/** Timestep */
	public static final float DELTA_TIME = 1f / FPS;

	/** Set to true to print the time each frame takes */
	public static final boolean DEBUG_FPS = false;

	/** Whether to do box2d debug rendering */
	public static boolean DEBUG_RENDER = false;

	/** Constant used by world.step() in box2d. Higher values mean higher accuracy, 8 is standard */
	public static final int VELOCITY_ITERATIONS = 8;
	/** Constant used by world.step() in box2d. Higher values mean higher accuracy, 3 is standard */
	public static final int POSITION_ITERATIONS = 3;

	/** Size of the main window */
	public static final int WINDOW_WIDTH = 1366;
	public static final int WINDOW_HEIGHT = 768;

	/**
	 * Box2D Constants, used to convert between the 2 coordinate systems
	 */
	public static final float WORLD_TO_BOX = .1f;
	public static final float BOX_TO_WORLD = 1 / WORLD_TO_BOX;

	/** Dimensions of car(s) */
	public static final float CAR_LENGTH = 5.0f;
	public static final float CAR_WIDTH = 2.0f;

	/** The width of a single lane, one way */
	public static final float LANE_WIDTH = 4.5f;

	/** Speed limit for simple (1-lane) roads in meters per second */
	public static final float CITY_SPEED_LIMIT = 50 / 3.6f;

	/** When braking, velocity will become brakingFactor * velocity (but also take into account the scaling factor) */
	public static final float DEFAULT_BRAKING_FACTOR = 0.98f;

	/** velocity = velocity + desired_velocity * SPEED_SCALING_FACTOR */
	public static final float SPEED_SCALING_FACTOR = 6f;

	/** The random instance used throughout the traffic simulation */
	public static final Random RANDOM = new Random(7);

	public static final StopWatch TIMER = new StopWatch();
	
	public static final boolean DEBUG_PRINT_LEVEL_0 = false;

	/** Default traffic light green interval */
	public static final int TRAFFIC_LIGHT_GREEN_INTERVAL = 5;

	/** Default traffic light orange interval */
	public static final int TRAFFIC_LIGHT_ORANGE_INTERVAL = 1;

}
