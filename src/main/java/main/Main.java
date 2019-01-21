package main;
import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import fullydynamic.FullyDynamicExhaustiveCounting;
import fullydynamic.FullyDynamicSubgraphReservoirAlgorithm;
import fullydynamic.FullyDynamicSubgraphReservoirFinalAlgorithm;
import fullydynamic.FullyDynamicSubgraphReservoirImprovedFirstAlgorithm;
import fullydynamic.FullyDynamicSubgraphReservoirImprovedSecondAlgorithm;
import fullydynamic.FullyDynamicTriesteAlgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;

import graphpattern.ThreeNodeGraphPattern;
import incremental.IncrementalExhaustiveCounting;
import incremental.IncrementalExhaustiveCountingFourNode;
import incremental.IncrementalSubgraphReservoirAlgorithm;
import incremental.IncrementalSubgraphReservoirFinalAlgorithm;
import incremental.IncrementalSubgraphReservoirFinalAlgorithmFourNode;
import incremental.IncrementalSubgraphReservoirImprovedAlgorithm;
import incremental.IncrementalTriesteAlgorithm;
import input.StreamEdge;
import input.StreamEdgeReader;
import slidingwindow.FixedSizeSlidingWindow;
import topkgraphpattern.Pattern;
import topkgraphpattern.Subgraph;
import topkgraphpattern.TopkGraphPatterns;

/*
 * The main class to run different algorithms
 * Created By: Anis Nasir
 * Created on: 18 Oct 2018
 * Updated on: 18 Oct 2018
 */

/**
 * @author Anis
 * 
 * main method to compare different algorithm
 * Input Parameter:
 * 		simulatorType: integer
 * 		directory: string (input directory)
 * 		fileName: string (input file in the form of edge list)
 * 		windowSize: integer (for sliding window)
 * 		epsilon: parameter to calculate size of the subgraph reservoir
 * 		delta: parameter to calculate the size of the subgraph reservoir
 * 		Tk: paramreter to calculate the size of the subgraph reservoir
 * 		k: integer (parameter for the top-k algorithm)
 */

public class Main {
	public static void main(String args[]) throws IOException {	
		//extract all parameters from the input
		int simulatorType = Integer.parseInt(args[0]);
		String directory = args[1] ;
		String fileName = args[2];
		int windowSize = Integer.parseInt(args[3]);
		double epsilon = Double.parseDouble(args[4]);
		double delta = Double.parseDouble(args[5]);
		int Tk = Integer.parseInt(args[6]);
		int k = Integer.parseInt(args[7]);
		System.out.println("simulator type: " + simulatorType + " window size: "+ windowSize + " epsilon: "+ epsilon + " delta: " + delta + " Tk: "+ Tk);

		String sep = ",";
		String inFileName = directory + fileName;

		//input file reader
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

		//declare object of the algorithm interface
		TopkGraphPatterns topkGraphPattern = null;
		long startTime = System.currentTimeMillis();

		if(simulatorType == 0 ) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new FullyDynamicSubgraphReservoirAlgorithm(size,k);
		}else if(simulatorType == 1) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			//int size = 1270176; //this one is the max from youtube dataset
			//int size = 988471; //this one is the max from patent dataset
			topkGraphPattern = new FullyDynamicTriesteAlgorithm(size, k );
		}else if(simulatorType == 2) {
			topkGraphPattern = new FullyDynamicExhaustiveCounting();
		}else if(simulatorType == 3) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new IncrementalSubgraphReservoirAlgorithm(size, k);
		}else if(simulatorType == 4) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			//int size = 988471; //this one is the max from patent dataset
			topkGraphPattern = new IncrementalTriesteAlgorithm(size, k );
		}else if (simulatorType == 5) {
			topkGraphPattern = new IncrementalExhaustiveCounting();
		}else if (simulatorType == 6) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new IncrementalSubgraphReservoirImprovedAlgorithm(size, k);
		}else if (simulatorType == 7) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new FullyDynamicSubgraphReservoirImprovedFirstAlgorithm(size, k);
		}else if (simulatorType == 8) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new FullyDynamicSubgraphReservoirImprovedSecondAlgorithm(size, k);
		}else if (simulatorType == 9) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new IncrementalSubgraphReservoirFinalAlgorithm(size, k);
		} else if (simulatorType == 10) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new FullyDynamicSubgraphReservoirFinalAlgorithm(size, k);
		} else if (simulatorType == 11) {
			topkGraphPattern = new IncrementalExhaustiveCountingFourNode();
		} else if (simulatorType == 12) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new IncrementalSubgraphReservoirFinalAlgorithmFourNode(size, k);
		} else if (simulatorType == 13) {
			topkGraphPattern = new FullyDynamicExhaustiveCounting();
		} else if (simulatorType == 14) {
			double epsilonk = (4+epsilon)/(epsilon*epsilon);
			double Tkk = Math.log(Tk/delta);
			int size = (int) (Tkk*epsilonk);
			System.out.println("size of the reservoir: " + size);
			topkGraphPattern = new IncrementalSubgraphReservoirFinalAlgorithmFourNode(size, k);
		}


		/*
		 * read from the edge list
		 * each line in the file represents a tuple of the form
		 * <source-id,source-label,dest-id,dest-label,edge-label>
		 */
		
		
		long edgeCount = 1;
		long PRINT_AFTER = 100000;
		while(edge != null) {
			topkGraphPattern.addEdge(edge);
			//System.out.println("+ " + edge);

			//slide the window and get the last item if the window is full
			if(isFullyDynamicAlgorithm(simulatorType))  {
				Optional<StreamEdge> oldestEdge = sw.add(edge);
				if(oldestEdge.isPresent()) {
					//System.out.println("- " + oldestEdge);
					topkGraphPattern.removeEdge(oldestEdge.get());
				}

			}
			edge = reader.nextItem();
			edgeCount++;
			
			if(edgeCount % PRINT_AFTER == 0) {
				System.out.println(String.format("%dk edges read in %d secs", (edgeCount/1000), ((System.currentTimeMillis() - startTime)/1000)));
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("execution time: " + (endTime-startTime)/(double)1000 + " secs.");
		//create the output file name
		String outFileName = "output_logs/output_"+fileName+"_"+windowSize+"_"+epsilon+"_"+delta+"_"+Tk+"_"+k;

		if(simulatorType == 0)
			outFileName = outFileName+"_fully-dynamic-subgraph-reservoir.log";
		else if (simulatorType == 1)
			outFileName = outFileName+"_fully-dynamic-trieste-reservoir.log";
		else if(simulatorType == 2)
			outFileName = outFileName+"_fully-dynamic-exhaustive-counting.log";
		else if(simulatorType == 3)
			outFileName = outFileName+"_incremental-subgraph-reservoir.log";
		else if(simulatorType == 4)
			outFileName = outFileName+"_incremental-trieste-reservoir.log";
		else if(simulatorType == 5)
			outFileName = outFileName+"_incremental-exhaustive-counting.log";
		else if(simulatorType == 6)
			outFileName = outFileName+"_incremental-subgraph-improved-reservoir.log";
		else if(simulatorType == 7)
			outFileName = outFileName+"_fully-dynamic-subgraph-improved-first-reservoir.log";
		else if(simulatorType == 8)
			outFileName = outFileName+"_fully-dynamic-subgraph-improved-second-reservoir.log";
		else if(simulatorType == 9)
			outFileName = outFileName+"_incremental-subgraph-final-reservoir.log";
		else if(simulatorType == 10)
			outFileName = outFileName+"_fully-dynamic-subgraph-final-reservoir.log";
		else if(simulatorType == 11)
			outFileName = outFileName+"_incremental-exhaustive-four-node.log";
		else if(simulatorType == 12)
			outFileName = outFileName+"_incremental-subgraph-final-four-node.log";
		else if(simulatorType == 13)
			outFileName = outFileName+"_fully-dynamic-exhaustive-four-node.log";
		else if(simulatorType == 14)
			outFileName = outFileName+"_fully-dynamic-subgraph-final-four-node-reservoir.log";

		BufferedWriter bw = null;
		FileWriter fw = null;

		fw = new FileWriter(outFileName);
		bw = new BufferedWriter(fw);

		printMap(topkGraphPattern.getFrequentPatterns(),bw);
		bw.flush();
		bw.close();
		System.out.println(topkGraphPattern.getNumberofSubgraphs());
	}
	public static void printMap(HashMap<Pattern,Integer> mp, BufferedWriter bw) throws IOException{
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<ThreeNodeGraphPattern,Integer> pair = (Map.Entry<ThreeNodeGraphPattern,Integer>)it.next();
			bw.write(pair.getKey() + " " + pair.getValue()+ "\n");
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	private static boolean isFullyDynamicAlgorithm(int simulatorType) {
		return simulatorType == 0 || simulatorType == 1 || simulatorType == 2 || simulatorType == 7 || simulatorType == 8 || simulatorType == 10 || simulatorType == 13 || simulatorType == 14;
	}
}
