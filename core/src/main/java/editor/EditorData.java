package editor;

import lombok.Getter;

public class EditorData {

	@Getter
	private int height;
	@Getter
	private int width;
	
	private Integer[][] grid;
	
	public EditorData(int height, int width) {
		grid = new Integer[height][width];
	}
	
	public int getGridElement(int V_coord, int H_coord) {
		return grid[V_coord][H_coord];
	}
	
	public void setGridElement(int V_coord, int H_coord, int value) {
		grid[V_coord][H_coord] = value;
	}
}
