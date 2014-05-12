package trafficsim;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import trafficsim.data.DataGatherer;
import trafficsim.roads.Road;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

@Getter
/**
 * The artemis world. Keeps a reference to the box2d world used for the physics
 * 
 * @author Giannis Papadopoulos
 * 
 */
public class TrafficSimWorld
		extends World {

	/** Box 2D World Used For Internal Physics Representation And Calculation */
	private com.badlogic.gdx.physics.box2d.World box2dWorld;

	/** Map from vertex IDs to corresponding entity IDs */
	private TIntIntMap vertexToEntityMap;


	/** Map from vertex IDs to corresponding entity IDs */
	private TIntIntMap edgeToEntityMap;

	/** The graph representing the roads and intersections */
	@Setter
	private Graph<Road> graph;

	/** For gathering data related to the simulation */
	private DataGatherer dataGatherer;
	
	private List<Vertex<Road>> spawnPoints;

	public TrafficSimWorld() {
		super();
		box2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
		vertexToEntityMap = new TIntIntHashMap();
		edgeToEntityMap = new TIntIntHashMap();
		dataGatherer = new DataGatherer();
		spawnPoints= new ArrayList<>();
	}

}
