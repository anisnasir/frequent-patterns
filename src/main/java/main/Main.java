package main;
import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import input.StreamEdge;
import input.StreamEdgeReader;
import slidingwindow.FixedSizeSlidingWindow;
import topkgraphpattern.ExhaustiveCounting;
import topkgraphpattern.SubgraphReservoirAlgorithm;
import topkgraphpattern.TopkGraphPatterns;
import topkgraphpattern.TriesteAlgorithm;

/*
 * The main class to run different algorithms
 * Created By: Anis Nasir
 * Created on: 18 Oct 2018
 * Updated on: 18 Oct 2018
 */

public class Main {
	public static void main(String args[]) throws IOException {
		/*
		 * main method to compare different algorithm
		 * Input Parameter:
		 * 		simulatorType: integer
		 * 		directory: string (input directory)
		 * 		fileName: string (input file in the form of edge list)
		 * 		windowSize: integer (for sliding window)
		 * 		k: integer (parameter for the top-k algorithm)
		 */

		int simulatorType = Integer.parseInt(args[0]);
		String directory = args[1] ;
		String fileName = args[2];
		int windowSize = Integer.parseInt(args[3]);
		int k = Integer.parseInt(args[4]);
		
		String sep = ",";
		String inFileName = directory + fileName;

		BufferedReader in = null;

		try {
			InputStream rawin = new FileInputStream(inFileName);
			if (inFileName.endsWith(".gz"))
				rawin = new GZIPInputStream(rawin);
			in = new BufferedReader(new InputStreamReader(rawin));
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
			System.exit(1);
		}

		StreamEdgeReader reader = new StreamEdgeReader(in, sep);
		StreamEdge edge = reader.nextItem();
		FixedSizeSlidingWindow sw = new FixedSizeSlidingWindow(windowSize);
		TopkGraphPatterns topkGraphPattern = null;
		
		if(simulatorType == 0 ) {
			double delta = 0.2;
			double epsilon = 0.1;
			int Tk = 101306;
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println(size);
			topkGraphPattern = new SubgraphReservoirAlgorithm(size,k);
		}else if(simulatorType == 1) {
			int size = 1;
			topkGraphPattern = new TriesteAlgorithm(size, k );
		}else if(simulatorType == 2) {
			topkGraphPattern = new ExhaustiveCounting();
		}

		while(edge!=null) {
			topkGraphPattern.addEdge(edge);
			//System.out.println(edge);
			StreamEdge oldestEdge = sw.add(edge);
			if(oldestEdge != null) {
				topkGraphPattern.removeEdge(oldestEdge);
			}
			edge = reader.nextItem();
		}
		System.out.println(topkGraphPattern.getFrequentPatterns());
	}
}
