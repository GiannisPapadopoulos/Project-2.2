package io;

import java.io.PrintWriter;
import java.io.IOException;

public class RoadMapWriter {
	public static void writeRoadMap(String filename, core.RoadMap map) throws IOException{
		PrintWriter pw = new PrintWriter(filename);
		String line = map.getNumEndpoints() + " " + map.getNumRoads();
		pw.println(line);
		
		for (core.RoadEndpoint ep: map.getEndpoints()){
			line = ep.getLabel() + " " + ep.getX() + " " + ep.getY();
			pw.println(line);
		}
		
		for (core.Road road: map.getRoads()){
			line = road.getStart().getLabel() + " " + road.getEnd().getLabel() + " " + road.getLength() + " " + road.getSpeed();
			pw.println(line);
		}
		
		pw.flush();
		pw.close();
	}
}
