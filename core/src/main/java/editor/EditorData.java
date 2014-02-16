package editor;

import lombok.Getter;

public class EditorData {
	
	private static final int LAND_EMPTY = 1;

	@Getter
	private int height;
	@Getter
	private int width;
	
	private Integer[][] grid;
	
	public EditorData(int height, int width) {
		this.height = height;
		this.width = width;
		grid = new Integer[height][width];
		for(int i =0;i<height;i++)
			for(int j = 0;j<width;j++)
				grid[i][j] = LAND_EMPTY;
	}
	
	public int getGridElement(int V_coord, int H_coord) {
		return grid[V_coord][H_coord];
	}
	
	public void setGridElement(int V_coord, int H_coord, int value) {
		grid[V_coord][H_coord] = value;
	}
}
