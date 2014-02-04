package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import core.Road;
import core.RoadEndpoint;
import core.RoadMap;

public class RoadMapReader {
	public static RoadMap readRoadMap(String filename) throws FileNotFoundException{
		Scanner s = new Scanner(new File(filename));
		int endpoints = s.nextInt();
		int roads = s.nextInt();
		
		HashMap<String, RoadEndpoint> em = new HashMap<String, RoadEndpoint>();
		
		RoadMap rm = new RoadMap();
		
		for (int i=0; i<endpoints; i++){
			String label = s.next();
			double x = s.nextDouble();
			double y = s.nextDouble();
			em.put(label, new RoadEndpoint(y, x, label));
		}
		
		for (int i=0; i<roads; i++){
			RoadEndpoint start = em.get(s.next());
			RoadEndpoint end = em.get(s.next());
			double length = s.nextDouble();
			double speed = s.nextDouble();
			Road road = new Road(start, end, length, speed);
			rm.addRoad(road);
		}
		s.close();
		return rm;
	}
}
