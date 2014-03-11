package editor;

import lombok.Data;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

@Data
public class MouseGridPosition {

	private EditorData editorData;

	private int x;
	private int y;

	private Status status;

	public MouseGridPosition(EditorData editorData) {
		this.editorData = editorData;
	}

	public void update(OrthographicCamera cam, MouseScreenPosition pos) {

		Vector3 mousePos = new Vector3(pos.getX(), pos.getY(), 0);

		System.out.println("=======================");
		System.out.format("before unproject %s   \r\n", mousePos);

		cam.unproject(mousePos);

		System.out.format("after unproject %s  \r\n", mousePos);

		mousePos.scl(1f / WorldRenderer.CELL_SIZE);

		System.out.format("after grid transform %s  \r\n", mousePos);

		if (mousePos.x < 0)
			x = (int)mousePos.x-1;
		else
			x = (int) mousePos.x;
		if (mousePos.y < 0)
			y = (int)mousePos.y-1;
		else
			y = (int) mousePos.y;

		if (x < 0 || y < 0 || x > editorData.getWidth() - 1
				|| y > editorData.getHeight() - 1)
			status = Status.OUT_GRID;
		else
			status = Status.IN_GRID;

		System.out
				.format("MOUSE GRID COORDINATES: %d %d %s \r\n", x, y, status);

	}

	public enum Status {
		IN_GRID, OUT_GRID
	}

	public void getScreenToGridInfo() {
	}

}
