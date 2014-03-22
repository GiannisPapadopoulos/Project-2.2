package editor;

import lombok.Data;
import trafficsim.TrafficSimConstants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

@Data
public class MousePosition {

	/**
	 * Represents x/y position of mouse based on absolute coordinates. [0,0] is
	 * located in origin. X coord - vertical Y coord - horizontal
	 * 
	 */
	private Coordinates absolute;

	/**
	 * Represents x/y position of mouse on a current screen. (Relative position)
	 * X coord - vertical Y coord - horizontal
	 */
	private Coordinates screen;

	/**
	 * Represents x/y position of mouse in grid coordinates system. [0,0] is
	 * located in the bottom left corner of the grid. X coord - vertical Y coord
	 * - horizontal
	 */
	private Coordinates grid;

	private Status inOutGridStatus;

	private int gridWidth, gridHeight;

	public MousePosition(int gridWidth, int gridHeight) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;

		absolute = new Coordinates();
		screen = new Coordinates();
		grid = new Coordinates();

	}

	@Data
	public class Coordinates {
		private int x, y;
	}

	public enum Status {
		IN_GRID, OUT_GRID
	}

	public void update(OrthographicCamera cam) {
		updateScreenCoordinates();
		updateAbsoluteCoordinates(cam);
		updateGridCoordinates(cam);

		if (TrafficSimConstants.DEBUG_PRINT_LEVEL_0) {
			System.out.println("Screen: " + screen.x + " | " + screen.y);
			System.out.println("Absolute: " + absolute.x + " | " + absolute.y);
			System.out.println("Grid: " + grid.x + " | " + grid.y);
			System.out.println(inOutGridStatus);
		}

	}

	private void updateScreenCoordinates() {
		screen.x = Gdx.input.getX();
		screen.y = Gdx.input.getY();
	}

	private void updateAbsoluteCoordinates(OrthographicCamera cam) {
		Vector3 mousePos = new Vector3(screen.x, screen.y, 0);

		if (TrafficSimConstants.DEBUG_PRINT_LEVEL_0) {
			System.out.println("=======================");
			System.out.format("before unproject %s   \r\n", mousePos);
		}

		cam.unproject(mousePos);

		if (TrafficSimConstants.DEBUG_PRINT_LEVEL_0) {
			System.out.format("after unproject %s  \r\n", mousePos);
		}
		absolute.x = (int) mousePos.x;
		absolute.y = (int) mousePos.y;

	}

	private void updateGridCoordinates(OrthographicCamera cam) {
		Vector3 mousePos = new Vector3(screen.x, screen.y, 0);

		if (TrafficSimConstants.DEBUG_PRINT_LEVEL_0) {
			System.out.println("=======================");
			System.out.format("before unproject %s   \r\n", mousePos);
		}

		cam.unproject(mousePos);

		if (TrafficSimConstants.DEBUG_PRINT_LEVEL_0) {
			System.out.format("after unproject %s  \r\n", mousePos);
		}

		mousePos.scl(1f / WorldRenderer.CELL_SIZE);

		if (TrafficSimConstants.DEBUG_PRINT_LEVEL_0) {
			System.out.format("after grid transform %s  \r\n", mousePos);
		}

		if (mousePos.x < 0)
			grid.x = (int) mousePos.x - 1;
		else
			grid.x = (int) mousePos.x;
		if (mousePos.y < 0)
			grid.y = (int) mousePos.y - 1;
		else
			grid.y = (int) mousePos.y;

		if (grid.x < 0 || grid.y < 0 || grid.x > gridWidth - 1
				|| grid.y > gridHeight - 1)
			inOutGridStatus = Status.OUT_GRID;
		else
			inOutGridStatus = Status.IN_GRID;

		if (TrafficSimConstants.DEBUG_PRINT_LEVEL_0) {
			System.out.format("MOUSE GRID COORDINATES: %d %d %s \r\n", grid.x,
					grid.y, inOutGridStatus);
		}
	}

}
