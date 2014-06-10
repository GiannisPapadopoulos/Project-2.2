package trafficsim.roads;

import java.util.ArrayList;

import lombok.Getter;

import com.badlogic.gdx.math.Vector2;

public class SubSystem {
	
	@Getter
	private ArrayList<ArrayList<Lane>> lanes;
	
	
	public SubSystem () {
		lanes = new ArrayList<ArrayList<Lane>>();
	}
	
	public void addSubsystem(ArrayList<Lane> subSystem) {
		lanes.add(subSystem);
	}
	
	public Lane getNextAuthority(ArrayList<Lane> authorityList, Lane currentAuthority) {
		if(authorityList.contains(currentAuthority))
			if(authorityList.indexOf(currentAuthority)<authorityList.size()-1)
				return authorityList.get(authorityList.indexOf(currentAuthority)+1);
			else
				return EndOfSubsystem.getEndOfSubsystem();
		else {
			System.out.println("CrossRoadSubystem - Something went wrong!");
			return null;
		}
	}
	
	
	
	public Lane getProperAuthority(Vector2 pos) {
		Lane closest = null;
		for(ArrayList<Lane> lane : lanes)
			for(Lane lane_elem : lane)
				if(closest==null)
					closest = lane_elem;
				else 
					if(closest.getDistance(pos)>lane_elem.getDistance(pos))
						closest = lane_elem;
		return closest;
	}
	
}
