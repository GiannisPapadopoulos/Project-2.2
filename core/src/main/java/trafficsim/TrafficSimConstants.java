package trafficsim;

/** All constants used in the simulation are defined here */
public final class TrafficSimConstants {

	/** Whether textures should be re-packed. Set to true if anything is changed */
	public static final boolean PACK = false;

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
	public static final float WORLD_TO_BOX = 0.1f;
	public static final float BOX_TO_WORLD = 1 / WORLD_TO_BOX;

	/** Dimensions of car(s) */
	public static final float CAR_LENGTH = 5.0f;
	public static final float CAR_WIDTH = 2.0f;

	/** The width of a single lane, one way */
	public static final float LANE_WIDTH = 3f;

}
