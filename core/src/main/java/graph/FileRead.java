package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import trafficsim.roads.Road;
import trafficsim.roads.Road.Direction;

import com.badlogic.gdx.math.Vector2;

public class FileRead {

	public static void fileWrite(Graph<Road> graph, String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);

			int vertexCounter = graph.getVertexList().size();
			int edgeCounter = graph.getEdgeList().size();

			writer.write(vertexCounter + "\n");
			writer.write(edgeCounter + "\n");

			for (Vertex<Road> currentVertex : graph.getVertexIterator()) {
				Road road = currentVertex.getData();
				writeRoadData(writer, road);

			}

			List<Edge<Road>> edgeList = graph.getEdgeList();
			
			for (Edge<Road> currentEdge : graph.getEdgeIterator()) {
				int edgeA = currentEdge.getAdjacentVertices().get(0);

				int edgeB = currentEdge.getAdjacentVertices().get(1);
				writer.write(edgeA + " ");

				writer.write(edgeB + " " );
				
				Road road = currentEdge.getData();
				writeRoadData(writer, road);
				
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void writeRoadData(FileWriter writer, Road road){
		try {
			writer.write(road.getPointA().x + " " + road.getPointA().y
					+ " ");
			writer.write(road.getPointB().x + " " + road.getPointB().y
					+ " ");
			writer.write(road.getDirection() + " ");
			writer.write((road.getNumLanes() + " "));
			writer.write(road.getSpeedLimit() + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static Graph<Road> readFile(String fileName){
		//Creates a FileReader Object
	      Scanner fr;
		try {
			fr = new Scanner(new File(fileName));
			  int amountOfVertexes = fr.nextInt();
		      int amountOfEdges = fr.nextInt();
		      Direction tja;
		      
		      Graph<Road> graph = new Graph<Road>();
		      
		      for (int i = 0; i<amountOfVertexes;i++) {
				Road road = readRoadOrSomething(fr);
				graph.addVertex(road);

		      }
		      
		      for (int i = 0; i <amountOfEdges;i++){
		    	  int edgeA = fr.nextInt();

					int edgeB = fr.nextInt();
					Road road = readRoadOrSomething(fr);
					graph.addEdge(road, graph.getVertex(edgeA), graph.getVertex(edgeB), road.getDirection() == Direction.BOTH);
		      }

		      fr.close();
		      return graph;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    return null;
	}

	private static Road readRoadOrSomething(Scanner fr) {
		Direction tja;
		float pointAX = fr.nextFloat();
		float pointAY = fr.nextFloat();
		Vector2 pointA = new Vector2(pointAX,pointAY);
		float pointBX = fr.nextFloat();
		float pointBY = fr.nextFloat();
		Vector2 pointB = new Vector2(pointBX, pointBY);
		String direction = fr.next();
		int numberOfLanes = fr.nextInt();
		float speedLimit = fr.nextFloat();
		if(direction == "BOTH"){
			tja = Direction.BOTH;
		}
		else if(direction == "UPSTREAM"){
			tja = Direction.UPSTREAM;
		}
		else{
			tja = Direction.DOWNSTREAM;
		}

		
		Road road = new Road(pointA,pointB,numberOfLanes,tja,speedLimit);
		return road;
	}
}