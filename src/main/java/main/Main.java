package main;
import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import input.StreamEdge;
import input.StreamEdgeReader;
import slidingwindow.FixedSizeSlidingWindow;
import topkgraphpattern.ExhaustiveCounting;
import topkgraphpattern.GraphPattern;
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
		double epsilon = Double.parseDouble(args[4]);
		double delta = Double.parseDouble(args[5]);
		int Tk = Integer.parseInt(args[6]);
		int k = Integer.parseInt(args[7]);
		System.out.println("window size: "+ windowSize + " epsilon: "+ epsilon + " delta: " + delta + " Tk: "+ Tk);

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
			//System.out.println("+ " + edge);
			StreamEdge oldestEdge = sw.add(edge);
			if(oldestEdge != null) {
				//System.out.println("- " + oldestEdge);
				topkGraphPattern.removeEdge(oldestEdge);
			}

			edge = reader.nextItem();
		}

		String outFileName = "output_logs/output_"+fileName+"_"+windowSize+"_"+epsilon+"_"+delta+"_"+Tk+"_"+k;

		if(simulatorType == 0)
			outFileName = outFileName+"_subgraph-reservoir.log";
		else if (simulatorType == 1)
			outFileName = outFileName+"_edge-reservoir.log";
		else if(simulatorType == 2)
			outFileName = outFileName+"_exhaustive-counting.log";

		BufferedWriter bw = null;
		FileWriter fw = null;

		fw = new FileWriter(outFileName);
		bw = new BufferedWriter(fw);

		printMap(topkGraphPattern.getFrequentPatterns(),bw);
		bw.flush();
		bw.close();
		System.out.println(topkGraphPattern.getNumberofSubgraphs());
	}
	public static void printMap(THashMap<GraphPattern,Integer> mp, BufferedWriter bw) throws IOException{
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			TMap.Entry<GraphPattern,Integer> pair = (TMap.Entry<GraphPattern,Integer>)it.next();
			bw.write(pair.getKey() + " " + pair.getValue()+ "\n");
			it.remove(); // avoids a ConcurrentModificationException
		}
	}


}
