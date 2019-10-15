package input;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Reads a stream of StreamItems from a file.
 */
public class StreamEdgeReader {
	private BufferedReader in;
	private String sep;

	public StreamEdgeReader(BufferedReader input, String sep) {
		this.in = input;
		this.sep = sep;
	}

	public StreamEdge nextItem() throws IOException {
		String line = null;
		try {
			line = in.readLine();

			if (line == null || line.length() == 0)
				return null;

			if (line.startsWith("#"))
				return null;

			String[] tokens = line.split(sep);
			if (tokens.length < 5)
				return null;

			int src = Integer.parseInt(tokens[0]);
			int srcLabel = Integer.parseInt(tokens[1]);
			int dest = Integer.parseInt(tokens[2]);
			int dstLabel = Integer.parseInt(tokens[3]);

			return new StreamEdge(src, srcLabel, dest, dstLabel);

		} catch (IOException e) {
			System.err.println("Unable to read from file");
			throw e;
		}

	}
}
