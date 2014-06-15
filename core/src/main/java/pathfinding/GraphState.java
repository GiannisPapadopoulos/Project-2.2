package pathfinding;

import graph.Vertex;
import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;
import lombok.ToString;
import search.SearchState;
import trafficsim.roads.NavigationObject;

@ToString
@Getter
@AllArgsConstructor
public class GraphState
		extends SearchState {

	@Delegate
	private Vertex<NavigationObject> vertex;

	@Override
	public int hashCode() {
		return vertex.getID();
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() != GraphState.class) {
			return false;
		}
		Vertex<NavigationObject> other = ((GraphState) o).getVertex();
		// TODO was this necessary?
		// if (other.getData().getClass() != NavigationObject.class) {
		// return false;
		// }
		return this.getVertex().getID() == other.getID();
	}

}
