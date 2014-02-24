package graph;

import java.awt.Point;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class VD_Gen implements VertexData {

	@Getter
	@Setter
	private ArrayList<Point> gridPositions;
	
	public VD_Gen(ArrayList<Point> gridPositions) {
		this.gridPositions = gridPositions;
	}
	
}
