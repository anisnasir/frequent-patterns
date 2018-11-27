package struct;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import input.StreamEdge;

public class Path {
	Set<StreamEdge> path;

	Path() {
		path = new TreeSet<StreamEdge>();
	}

	public Set<StreamEdge> getPath() {
		return path;
	}

	public void setPath(Set<StreamEdge> path) {
		this.path = path;
	}

	public void addEdge(StreamEdge edge) {
		path.add(edge);
	}

}
