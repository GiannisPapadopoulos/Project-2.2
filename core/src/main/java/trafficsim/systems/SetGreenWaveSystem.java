package trafficsim.systems;

import static trafficsim.TrafficSimConstants.TIMER;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import trafficsim.TrafficSimWorld;
import trafficsim.experiments.SimulationParameters;
import trafficsim.experiments.SimulationParameters.GreenWaveInfo;
import trafficsim.factories.GreenMile;
import trafficsim.roads.NavigationObject;

import com.artemis.systems.VoidEntitySystem;

@RequiredArgsConstructor
public class SetGreenWaveSystem
		extends VoidEntitySystem {

	@NonNull
	private SimulationParameters parameters;

	private boolean set = false;

	@Override
	protected void processSystem() {
		set = true;
		List<Vertex<NavigationObject>> vertexList = new ArrayList<>();
		GreenWaveInfo gwInfo = parameters.getGreenWaveInfo();
		if (gwInfo == null) {
			return;
		}
		TrafficSimWorld tfWorld = (TrafficSimWorld) world;
		for (int i = 0; i < gwInfo.size(); i++) {
			vertexList.add(tfWorld.getGraph().getVertex(gwInfo.get(i)));
		}

		GreenMile.createGreenMile(tfWorld, vertexList, vertexList.size());

	}

	@Override
	protected boolean checkProcessing() {
		return TIMER.getTime() > 1000 && !set;
	}


}
