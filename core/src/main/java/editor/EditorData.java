package editor;

import lombok.Data;

@Data
public class EditorData {

	private int height;
	private int width;

	public EditorData(int height, int width) {
		this.height = height;
		this.width = width;
	}
}
