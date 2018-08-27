package struct;

import java.util.ArrayList;
import java.util.List;

import input.StreamEdge;

public class Path {
	List<StreamEdge> path;

	Path() {
		path = new ArrayList<StreamEdge>();
	}

	public List<StreamEdge> getPath() {
		return path;
	}

	public void setPath(List<StreamEdge> path) {
		this.path = path;
	}

	public void addEdge(StreamEdge edge) {
		path.add(edge);
	}

}
