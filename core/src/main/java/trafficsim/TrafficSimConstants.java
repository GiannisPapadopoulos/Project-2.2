package trafficsim;

/** All constants used in the simulation are defined here */
public final class TrafficSimConstants {

	/** Constant used by world.step() in box2d. Higher values mean higher accuracy, 8 is standard */
	public static final int VELOCITY_ITERATIONS = 8;
	/** Constant used by world.step() in box2d. Higher values mean higher accuracy, 3 is standard */
	public static final int POSITION_ITERATIONS = 3;

	/** Size of the main window */
	public static final int WINDOW_WIDTH = 1280;
	public static final int WINDOW_HEIGHT = 720;

	/**
	 * Box2D Constants, used to convert between the 2 coordinate systems
	 */
	public static final float WORLD_TO_BOX = 0.01f;
	public static final float BOX_TO_WORLD = 1 / WORLD_TO_BOX;

}
