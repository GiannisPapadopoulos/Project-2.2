package tests;

import io.RoadMapReader;
import io.RoadMapWriter;

import java.io.IOException;

import core.Road;
import core.RoadEndpoint;
import core.RoadMap;

public class ReadAndWriteRoadMapTest {

	public static void main(String[] args){
		
		//This currently just contains a test for the reader and writer
		
		RoadMap map = new RoadMap();
		RoadEndpoint ea = new RoadEndpoint(0, 0, "A");
		RoadEndpoint eb = new RoadEndpoint(0, 5, "B");
		RoadEndpoint ec = new RoadEndpoint(5, 5, "C");
		RoadEndpoint ed = new RoadEndpoint(5, 0, "D");
		map.addRoad(new Road(ea, eb, 3, 5));
		map.addRoad(new Road(eb, ec, 5, 3));
		map.addRoad(new Road(ec, ed, 3, 5));
		map.addRoad(new Road(ed, ea, 5, 3));
		try {
			RoadMapWriter.writeRoadMap("roadmap.txt", map);
			
			RoadMap read = RoadMapReader.readRoadMap("roadmap.txt");
			for (Road r: read.getRoads()){
				System.out.println(r.getStart().getLabel() + " " + r.getEnd().getLabel() + " " + r.getWeight());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
