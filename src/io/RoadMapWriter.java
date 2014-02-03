package io;

import java.io.PrintWriter;
import java.io.IOException;

public class RoadMapWriter {
	public static void writeRoadMap(String filename, core.RoadMap map) throws IOException{
		PrintWriter pw = new PrintWriter(filename);
		
		for (core.RoadEndpoint ep: map.getEndpoints()){
			String line = ep.getLabel() + " " + ep.getX() + " " + ep.getY();
			pw.println(line);
		}
		
		pw.println();
		for (core.Road road: map.getRoads()){
			String line = road.getStart().getLabel() + " " + road.getEnd().getLabel() + " " + road.getLength() + " " + road.getSpeed();
		}
	}
}
