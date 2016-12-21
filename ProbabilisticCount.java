package ProbablisticCounting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.jfree.ui.RefineryUtilities;

public class PC {

	final static int MAX_BITMAP_SIZE = 1500;
	final static String inputFile = "C:\\sem 1\\ITM\\Project\\FlowTraffic.txt";
	//String outputFile = "C:\\sem 1\\ITM\\Project\\Results.txt";
	Double fractionOfZeroes= 0.0;
	String line = "";
	int[] b = new int[MAX_BITMAP_SIZE];
	String sourceIP = "";
	String destIP = "";
	NavigableMap<String, HashSet<String>> flowList = new TreeMap<String, HashSet<String>>();
	int actualCardinality = 0;
	int estimatedCardinality = 0;
	int hashIndex = 0;
	private static HashMap<Integer,Integer> resultGraph = new HashMap<Integer,Integer>();    


	PC(){

		try{

			/* Initialize bitmap to zero*/			
			initialize_bitmap();
			
			/*Build HashMap by parsing the text file*/
			buildHashMap();
			
			/*Build bitmap with hashmap */
			buildBitMap();

		}
		catch(Exception e){
			System.out.println("Exception occured : "+e);
			e.printStackTrace();
		}
	}

	
	public void buildBitMap() {

		for (String flow: flowList.keySet()){

			actualCardinality = flowList.get(flow).size();

			for (String eachFlow : flowList.get(flow)) {
				System.out.println("Here");

				/*** ONLINE OPERATION ***/
				try{
					int index;
					
					/* Getting hash of combination of source and dest IPs*/
					index = (int) ((customHash(flow+eachFlow) & 0x7fffffff) % MAX_BITMAP_SIZE);
					
					b[(int) index] = 1;
					System.out.println(eachFlow + " " + (int) index); 
				}
				catch(Exception e){
					System.out.println("Exception occured : "+e);
					e.printStackTrace();
				}

			}	
			
			/*** OFFLINE OPERATION***/
			/* Counting fraction of zeroes in each bitmap */
			fractionOfZeroes =  countZeroes()/ (double) MAX_BITMAP_SIZE;
			
			/* Finding estimated cardinality of each flow */
			estimatedCardinality = (int)(-1 * MAX_BITMAP_SIZE * Math.log(fractionOfZeroes));
			
			/* For plotting purposes*/
			resultGraph.put(actualCardinality, estimatedCardinality);
			
			/* Initializing the bitmap again */
			initialize_bitmap();


		}
	}		     
	
	/*Custom SecureRandom Hash function using SHA1*/
	static int customHash(String name) throws NoSuchAlgorithmException {
		SecureRandom srA = SecureRandom.getInstance("SHA1PRNG");
		srA.setSeed(name.getBytes());
		return new Integer(srA.nextInt());
	}

	
	public void buildHashMap() throws IOException{

		int start = 1;
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		while((line = br.readLine()) != null) {

			/* Split each line of the file on " "	*/
			String[] columns = line.split(" ");

			/* Assign first column of the file to source IP */
			sourceIP = columns[0];
			//	String latestSource = sourceIP;

			/* Assign Destination IP */
			for(int k=1; k<columns.length;k++){				 
				if(!(columns[k].equalsIgnoreCase(""))){
					destIP = columns[k];
					break;
				}
			}
			
			/* For first line of the text file*/
			if(start ==1){
				HashSet<String> newFlow = new HashSet<String>();
				newFlow.add(destIP);
				flowList.put(sourceIP, newFlow);
				start = 0;
			}
			
			/* Checks if sourceIP exists in last stored key value*/
			else if(flowList.lastKey().equals(sourceIP)){
				flowList.get(sourceIP).add(destIP);

			}

			/* Adds new flow to hashmap*/
			else{
				HashSet<String> newFlow = new HashSet<String>();
				newFlow.add(destIP);
				flowList.put(sourceIP, newFlow);
			}

		}


		br.close();
	}

	/* Initialize bitmap to zero*/
	public void initialize_bitmap(){

		for(int i=0; i<MAX_BITMAP_SIZE; i++){
			b[i] = 0; // Initialize the bitmap with 0			
		}
	}


	/*** OFFLINE OPERATION ***/
	public int countZeroes(){

		int noOfZeroes = 0;

		/* Calculate number of zeroes in the bitmap*/
		for(int i=0 ; i<MAX_BITMAP_SIZE ; i++){
			if (b[i] == 0){
				noOfZeroes++;
			}
		}

		return noOfZeroes;
		
	}

	public static void main (String[] x){

		PC pc = new PC();
		
		/* Chart Plotting*/
		ScatterPlot chart = new ScatterPlot("ITM Project 1", "Probabilistic Counting", resultGraph);
		chart.pack( );          
		RefineryUtilities.centerFrameOnScreen( chart );          
		chart.setVisible( true ); 
	}
}



