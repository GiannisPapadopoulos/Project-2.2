package editor;

import java.util.HashMap;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;

public class EditorData {

	protected static final int LAND_EMPTY = 1;

	protected static final int LAND_BORDER = -1;

	protected static final int LAND_ROAD_LOWER_BOUND = 100;
	protected static final int LAND_ROAD_UPPER_BOUND = 199;
	
	public static final int LAND_ROAD_SINGLE_LANE_LOWER_BOUND = 100;
	public static final int LAND_ROAD_SINGLE_LANE_UPPER_BOUND = 149;
	
	public static final int LAND_ROAD_DOUBLE_LANE_LOWER_BOUND = 150;
	public static final int LAND_ROAD_DOUBLE_LANE_UPPER_BOUND = 199;
	
	

	protected static final int LAND_ROAD_STANDARD = 101;
	protected static final int LAND_ROAD_STREET = 102;
	
	protected static final int LAND_ROAD_LARGE_AVENUE = 151;
	protected static final int LAND_ROAD_HIGHWAY = 152;

	public static final int LAND_CROSS_ROAD_LOWER_BOUND = 200;
	public static final int LAND_CROSS_ROAD_UPPER_BOUND = 299;
	
	protected static final int LAND_CROSS_ROAD_ST = 201;
	protected static final int LAND_CROSS_ROAD_LA = 202;
	protected static final int LAND_CROSS_ROAD_HYBRID = 202;

	@Getter
	private int height;
	@Getter
	private int width;
	@Getter
	private Integer[][] grid;

	private HashMap<Integer, Color> colorMap;

	public EditorData(int height, int width) {
		this.height = height;
		this.width = width;
		grid = new Integer[height][width];
		colorMap = new HashMap<Integer, Color>();
		initColorMap();
		initGrid();
	}

	private void initGrid() {

		// ESENTIAL CODE - BASIC GRID INIT
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				grid[i][j] = LAND_EMPTY;

		// JUST FOR DEBUG
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < width; j++)
				grid[(height / 2) + i][j] = LAND_ROAD_LARGE_AVENUE;
		for (int j = 0; j < 2; j++)
			for (int i = 0; i < height; i++)
				grid[i][j + (width / 2)] = LAND_ROAD_LARGE_AVENUE;
		
		for(int i = 0;i<width;i++)
			grid[3*height/4][i] = LAND_ROAD_STANDARD;
		for(int i = 0;i<height;i++)
			grid[i][3*width/4] = LAND_ROAD_STANDARD;
		for(int i = 0;i<width;i++)
			grid[height/4][i] = LAND_ROAD_STANDARD;
		for(int i = 0;i<height;i++)
			grid[i][width/4] = LAND_ROAD_STANDARD;
		
		
		
		
		place22CrossRoad(height / 2, width / 2, LAND_CROSS_ROAD_LA);
		
		place21CrossRoad(height /2, width /4, 1, LAND_CROSS_ROAD_HYBRID);
		place21CrossRoad(height /2, 3*width /4, 1, LAND_CROSS_ROAD_HYBRID);
		

		place21CrossRoad(height /4, width /2, 0, LAND_CROSS_ROAD_HYBRID);
		place21CrossRoad(3*height /4, width /2, 0, LAND_CROSS_ROAD_HYBRID);
		
		place11CrossRoad(height/4, width/4, LAND_CROSS_ROAD_ST);
		place11CrossRoad(3*height/4, width/4, LAND_CROSS_ROAD_ST);
		place11CrossRoad(height/4, 3*width/4, LAND_CROSS_ROAD_ST);
		place11CrossRoad(3*height/4, 3*width/4, LAND_CROSS_ROAD_ST);
		
		
		// JUST FOR DEBUG

		// ESENTIAL CODE - CREATES BORDER LAYER
		for (int i = 0; i < height; i = i + height - 1)
			for (int j = 0; j < width; j++)
				grid[i][j] = LAND_BORDER;
		for (int j = 0; j < width; j = j + width - 1)
			for (int i = 0; i < height; i++)
				grid[i][j] = LAND_BORDER;
	}

	private void initColorMap() {
		colorMap.put(LAND_EMPTY, Color.LIGHT_GRAY);
		colorMap.put(LAND_ROAD_LARGE_AVENUE, Color.ORANGE);
		colorMap.put(LAND_BORDER, Color.DARK_GRAY);
		colorMap.put(LAND_ROAD_STANDARD, Color.MAGENTA);
		colorMap.put(LAND_CROSS_ROAD_LA, Color.RED);
		colorMap.put(LAND_CROSS_ROAD_HYBRID, Color.RED);
		colorMap.put(LAND_CROSS_ROAD_ST, Color.RED);
	}

	public int getAt(int V_coord, int H_coord) {
		return grid[V_coord][H_coord];
	}

	public void setAt(int V_coord, int H_coord, int value) {
		grid[V_coord][H_coord] = value;
	}

	public Color requestColor(int V_coord, int H_coord) {
		if (colorMap.containsKey(grid[V_coord][H_coord]))
			return colorMap.get(grid[V_coord][H_coord]);
		else {
			System.out
					.println("Color for a given grid IDentifier undefined!! Grid ID: "
							+ grid[V_coord][H_coord]);
			return null;
		}
	}

	public void place22CrossRoad(int v_c, int h_c, int crossRoadCode) {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++)
				grid[v_c + i][h_c + j] = crossRoadCode;
	}

	public void place21CrossRoad(int v_c, int h_c, int dir, int crossRoadCode) {
		grid[v_c][h_c] = crossRoadCode;
		if (dir == 0)
			grid[v_c][h_c + 1] = crossRoadCode;
		else if (dir == 1)
			grid[v_c + 1][h_c] = crossRoadCode;

	}

	public void place11CrossRoad(int v_c, int h_c, int crossRoadCode) {
		grid[v_c][h_c] = crossRoadCode;
	}

}
