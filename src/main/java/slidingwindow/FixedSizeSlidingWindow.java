package slidingwindow;
import java.util.LinkedList;

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
	
	public StreamEdge add(StreamEdge newEdge) {
		fifo.add(newEdge);
		if(fifo.size() >=windowSize) {
			StreamEdge returnEdge = fifo.removeFirst();
			return returnEdge;
		}else {
			return null;
		}
	}
}
