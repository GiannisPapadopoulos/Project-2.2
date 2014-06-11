package editor;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import lombok.Data;

@Data
public class EditorData {

	public static ArrayList<Vector2> debugPoints = new ArrayList<Vector2>();
	public static ArrayList<Vector2> debugPoints2 = new ArrayList<Vector2>();
	
	private int height;
	private int width;
	private int h_shift;
	private int w_shift;
	
	private PointsOfInterest.PointOfInterest firstClick;
	private PointsOfInterest.PointOfInterest secondClick;

	public EditorData(int height, int width, int h_shift, int w_shift) {
		this.height = height;
		this.width = width;
		this.h_shift = h_shift;
		this.w_shift = w_shift;
		
	}
}
