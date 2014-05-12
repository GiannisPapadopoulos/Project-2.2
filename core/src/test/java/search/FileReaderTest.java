package search;

import graph.FileRead;
import graph.Graph;
import graph.GraphFactory;

import org.junit.Test;

import trafficsim.roads.Road;

public class FileReaderTest {

	@Test
	public void test() {
		Graph<Road> graph = GraphFactory.createManhattanGraph(10, 10, 60, -300, -200);
		FileRead.fileWrite(graph, "tja");
		Graph readGraph = FileRead.readFile("tja");
		

		}
	
}
