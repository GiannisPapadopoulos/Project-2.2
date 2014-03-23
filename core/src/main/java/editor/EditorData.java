package editor;

import lombok.Data;

@Data
public class EditorData {

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
