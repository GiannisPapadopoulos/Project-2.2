package editor;

import lombok.Data;

@Data
public class MouseScreenPosition {

	private int x;
	private int y;
	
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
