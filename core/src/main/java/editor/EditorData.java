package editor;

import java.util.HashMap;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;

public class EditorData {
	
	private static final int LAND_EMPTY = 1;
	
	private static final int LAND_BORDER = -1;
	
	private static final int LAND_ROAD_LOWER_BOUND = 100;
	private static final int LAND_ROAD_UPPER_BOUND = 999;
	
	private static final int LAND_ROAD_STANDARD = 101;
	private static final int LAND_ROAD_LARGE_AVENUE = 102;
	private static final int LAND_ROAD_STREET = 103;
	private static final int LAND_ROAD_HIGHWAY = 104;
	
	
	

	@Getter
	private int height;
	@Getter
	private int width;
	
	private Integer[][] grid;
	
	private HashMap<Integer,Color> colorMap;
	
	public EditorData(int height, int width) {
		this.height = height;
		this.width = width;
		grid = new Integer[height][width];
		colorMap = new HashMap<Integer,Color>();
		initColorMap();
		initGrid();		
	}
	
	private void initGrid() {

		// ESENTIAL CODE - BASIC GRID INIT
		for(int i =0;i<height;i++)
			for(int j = 0;j<width;j++)
				grid[i][j] = LAND_EMPTY;
		
		
		// JUST FOR DEBUG
		for(int i = 0;i<2;i++)
			for(int j = 0;j<width;j++)
				grid[(height/2)+i][j] = LAND_ROAD_LARGE_AVENUE;
		for(int j = 0;j<2;j++)
			for(int i=0;i<height;i++)
				grid[i][j+(width/2)] = LAND_ROAD_LARGE_AVENUE;
		// JUST FOR DEBUG

		
		// ESENTIAL CODE - CREATES BORDER LAYER
		for(int i=0;i<height;i=i+height-1)
			for(int j=0;j<width;j++)
				grid[i][j] = LAND_BORDER;
		for(int j=0;j<width;j=j+width-1)
			for(int i=0;i<height;i++)
				grid[i][j] = LAND_BORDER;
	}
	
	private void initColorMap() {
		colorMap.put(LAND_EMPTY, Color.LIGHT_GRAY);
		colorMap.put(LAND_ROAD_LARGE_AVENUE, Color.ORANGE);
		colorMap.put(LAND_BORDER, Color.DARK_GRAY);
	}
	
	public int getGridElement(int V_coord, int H_coord) {
		return grid[V_coord][H_coord];
	}
	
	public void setGridElement(int V_coord, int H_coord, int value) {
		grid[V_coord][H_coord] = value;
	}
	
	public Color requestColor(int V_coord, int H_coord) {
		if(colorMap.containsKey(grid[V_coord][H_coord]))
			return colorMap.get(grid[V_coord][H_coord]);
		else {
			System.out.println("Color for a given grid IDentifier undefined!! Grid ID: "+ grid[V_coord][H_coord]);
			return null;
		}
			
	}
}
