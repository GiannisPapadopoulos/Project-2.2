package editor.traffic_objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrossRoad {

	private int gridVertPos;
	private int gridHoriPos;

	private double orientation;
	private double size;	
	
	private ConnectionStrategy connectionStrategy;
	
	
	
}
