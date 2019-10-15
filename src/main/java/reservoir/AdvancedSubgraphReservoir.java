package reservoir;

import java.util.List;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import struct.LabeledNode;
import struct.MapArray;
import topkgraphpattern.Subgraph;

/**
 * * @author Anis
 *
 * @param <T>
 */
public class AdvancedSubgraphReservoir<T> implements Reservoir<T> {
	private THashMap<LabeledNode, THashSet<T>> vertexSubgraphMap;
	MapArray<T> list;

	public AdvancedSubgraphReservoir() {
		vertexSubgraphMap = new THashMap<LabeledNode, THashSet<T>>();
		list = new MapArray<T>();

	}

	@Override
	public boolean add(T value) {
		if (!contains(value)) {
			Subgraph t = (Subgraph) value;
			List<LabeledNode> nodes = t.getAllVertices();
			for (LabeledNode node : nodes) {
				add(node, value);
			}

			list.add(value);
			return true;
		} else
			return false;
	}

	public void add(LabeledNode a, T value) {
		if (vertexSubgraphMap.containsKey(a)) {
			THashSet<T> set = vertexSubgraphMap.get(a);
			set.add(value);
			vertexSubgraphMap.put(a, set);
		} else {
			THashSet<T> set = new THashSet<>();
			set.add(value);
			vertexSubgraphMap.put(a, set);
		}
	}

	@Override
	public boolean contains(T value) {
		if (value == null) {
			throw new NullPointerException();
		}
		return list.contains(value);

	}

	@Override
	public T getRandom() {
		return list.getRandom();
	}

	public T deleteRandom() {
		return list.deleteRandom();
	}

	@Override
	public boolean remove(T value) {
		if (!contains(value)) {
			return false;
		}
		if (list.contains(value)) {
			list.remove(value);

			List<LabeledNode> vertices = ((Subgraph) value).getAllVertices();
			for (LabeledNode vertex : vertices) {
				remove(vertex, value);
			}
			return true;
		} else {
			return false;
		}
	}

	public void remove(LabeledNode a, T value) {
		if (vertexSubgraphMap.containsKey(a)) {
			THashSet<T> set = vertexSubgraphMap.get(a);
			set.remove(value);
			vertexSubgraphMap.put(a, set);
		}
	}

	public int size() {
		return list.size();
	}

	public THashSet<T> getAllSubgraphs(LabeledNode a) {
		if (vertexSubgraphMap.containsKey(a))
			return vertexSubgraphMap.get(a);
		else
			return new THashSet<>();
	}
	
	public THashMap<LabeledNode, THashSet<T>> getVertexSubgraphMap () {
		return vertexSubgraphMap;
	}
}
