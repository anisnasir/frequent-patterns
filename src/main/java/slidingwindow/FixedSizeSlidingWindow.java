package slidingwindow;
import java.util.LinkedList;
import java.util.Optional;

import input.StreamEdge;


public class FixedSizeSlidingWindow {

	public long windowSize;
	LinkedList<StreamEdge> fifo;

	FixedSizeSlidingWindow() {
		windowSize = 0;
		fifo = new LinkedList<StreamEdge>();
	}

	public FixedSizeSlidingWindow(int wSize) {
		windowSize = wSize;
		fifo = new LinkedList<StreamEdge>();
	}

	public Optional<StreamEdge> add(StreamEdge newEdge) {
		fifo.add(newEdge);
		if (fifo.size() >= windowSize) {
			StreamEdge returnEdge = fifo.removeFirst();
			return Optional.of(returnEdge);
		} else {
			return Optional.empty();
		}
	}
}
