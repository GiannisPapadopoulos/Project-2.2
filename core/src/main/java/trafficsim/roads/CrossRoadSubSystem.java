package trafficsim.roads;

import java.util.ArrayList;

public class CrossRoadSubSystem {
	
	private ArrayList<ArrayList<Lane>> lanes;
	
	
	public CrossRoadSubSystem () {
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
	
}
