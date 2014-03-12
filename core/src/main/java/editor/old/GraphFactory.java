package editor.old;

import graph.Edge;
import graph.Graph;
import graph.VD_Gen;
import graph.Vertex;

import java.awt.Point;
import java.util.ArrayList;

public class GraphFactory {

	public static Graph createGraph(OLD_EditorData data) {

		Graph graph = new Graph();

		// VERTICES
		boolean[][] crM = new boolean[data.getHeight()][data.getWidth()];
		for (int i = 0; i < crM.length; i++)
			for (int j = 0; j < crM[i].length; j++)
				crM[i][j] = false;
		for (int i = 0; i < data.getHeight(); i++)
			for (int j = 0; j < data.getWidth(); j++) {
				if (data.getAt(i, j) > OLD_EditorData.LAND_CROSS_ROAD_LOWER_BOUND
						&& data.getAt(i, j) < OLD_EditorData.LAND_CROSS_ROAD_UPPER_BOUND
						&& !crM[i][j]) {
					int vert_size = i;
					int hori_size = j;
					System.out.println("point: " + i + " | " + j);
					while (data.getAt(vert_size, hori_size) == data.getAt(
							vert_size + 1, hori_size)) {
						vert_size++;
					}
					while (data.getAt(vert_size, hori_size) == data.getAt(
							vert_size, hori_size + 1)) {
						hori_size++;
					}
					vert_size -= i - 1;
					hori_size -= j - 1;

					System.out.println("out: " + vert_size + " | " + hori_size);
					ArrayList<Point> vertexCells = new ArrayList<Point>();
					for (int v = i; v < i + vert_size; v++)
						for (int h = j; h < j + hori_size; h++) {
							crM[v][h] = true;
							vertexCells.add(new Point(v, h));
						}
					graph.addVertex(new Vertex(new VD_Gen(vertexCells)));
					System.out.println("added");

				}

			}

		// EDGES
		for (Vertex v : graph.getVertices()) {
			boolean[][] occupancyGrid = new boolean[data.getHeight()][data
					.getWidth()];
			int[][] costMap = new int[data.getHeight()][data.getWidth()];
			for (Point p : v.getData().getGridPositions()) {
				occupancyGrid[p.x][p.y] = true;
				costMap[p.x][p.y] = 0;
			}
			for (Point p : v.getData().getGridPositions()) {
				exploreGrid(graph, v, p.x + 1, p.y, data.getGrid(), costMap,
						occupancyGrid);
				exploreGrid(graph, v, p.x - 1, p.y, data.getGrid(), costMap,
						occupancyGrid);
				exploreGrid(graph, v, p.x, p.y + 1, data.getGrid(), costMap,
						occupancyGrid);
				exploreGrid(graph, v, p.x, p.y - 1, data.getGrid(), costMap,
						occupancyGrid);
			}
		}

		return graph;
	}

	private static void exploreGrid(Graph graph, Vertex startVertex, int x,
			int y, Integer[][] grid, int[][] costMap, boolean[][] occupancyGrid) {
		if (occupancyGrid[x][y])
			return;
		else if (grid[x][y] > OLD_EditorData.LAND_CROSS_ROAD_LOWER_BOUND
				&& grid[x][y] < OLD_EditorData.LAND_CROSS_ROAD_UPPER_BOUND) {
			for (Vertex v : graph.getVertices())
				for (Point p : v.getData().getGridPositions())
					if (p.x == x && p.y == y) {
						new Edge(v, startVertex, null);
						return;
					}
		} else if (grid[x][y] > OLD_EditorData.LAND_ROAD_SINGLE_LANE_LOWER_BOUND
				&& grid[x][y] < OLD_EditorData.LAND_ROAD_DOUBLE_LANE_UPPER_BOUND) {
			occupancyGrid[x][y] = true;
			exploreGrid(graph, startVertex, x + 1, y, grid, costMap,
					occupancyGrid);
			exploreGrid(graph, startVertex, x - 1, y, grid, costMap,
					occupancyGrid);
			exploreGrid(graph, startVertex, x, y + 1, grid, costMap,
					occupancyGrid);
			exploreGrid(graph, startVertex, x, y - 1, grid, costMap,
					occupancyGrid);

		}

	}
}
