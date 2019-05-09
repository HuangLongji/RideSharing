import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



public class ProduceRequet2 {
	public int RequestNum;   
	public int GraphNum;     
	public static int siteNumber;   
	
	ArrayList<Request> requests=new ArrayList<Request>();
//	Map<Integer, ArrayList<WeightedEdge>> graphMap=new HashMap<Integer, ArrayList<WeightedEdge>>();
	public static ArrayList<Integer> Vertices=new ArrayList<Integer>();
	public Map<Integer, ArrayList<WeightedEdge>> graphMap;
	
	public ProduceRequet2(int RequestNum,int GraphNum,int siteNumber,Map<Integer, ArrayList<WeightedEdge>> graphMap){
		this.RequestNum=RequestNum;
		this.GraphNum=GraphNum;
		this.siteNumber=siteNumber;
		this.graphMap=graphMap;
	}
	
	public ArrayList<Request> produce(){
		FileOutputStream fos=null;
		int n=0;  
		ArrayList<Integer> pathArrayList=new ArrayList<Integer>();
		Graph graph=new Graph(graphMap);
		graph.intGraph();    //add the vertex of graphMap to the Vertex
		//initialize the graph 
		
		for(int i=0;i<RequestNum;i++){
			Random random1=new Random();
			int x=Math.abs(random1.nextInt())%GraphNum;        
			int y=Math.abs(random1.nextInt())%GraphNum;       			
			int z=Math.abs(random1.nextInt())%(siteNumber-1)+1;    

			if(x!=y){
				ArrayList<Integer> path=new ArrayList<Integer>();
				
				graph.dijkstra(x);
				path=graph.printPath(y);
				
				
				if(path.size()>0){
			    requests.add(new Request(n, z, x, y,path));
			   // System.out.println("n: "+n+"z passengernum: "+z+" x "+x+" y "+y);
			   // for(Integer i1:path){
				//	System.out.println("path:print: "+i1);}
			 //   System.out.println("end!"); 
			 //   n++; 
				}
			}
			
		}
		
		try {
			fos=new FileOutputStream("request.txt");
			for(int k=0;k<requests.size();k++){
				byte[] b1=String.valueOf(requests.get(k).number).getBytes();
				byte[] b2=String.valueOf(requests.get(k).start).getBytes();
				byte[] b3=String.valueOf(requests.get(k).end).getBytes();
				byte[] b6=String.valueOf(requests.get(k).passengerNumber).getBytes();
				byte[] b4=" ".getBytes();
				byte[] b5="\r\n".getBytes();
				
				fos.write(b1);
				fos.write(b4);
				fos.write(b2);
				fos.write(b4);
				fos.write(b3);
				fos.write(b4);
				fos.write(b6);
				fos.write(b5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return requests;
	}
	
	
	//pruning algorithm
	/*
	 * request: num passengernum start end path
	 */
	static ArrayList<ArrayList<Request>> getSubsets(ArrayList<Request> set,int index){
		ArrayList<ArrayList<Request>> allsubsets;
		if(set.size()==index){
			allsubsets = new ArrayList<ArrayList<Request>>();
			allsubsets.add(new ArrayList<Request>());
		}else {	
			allsubsets = getSubsets(set, index+1);
			Request item = set.get(index);
			ArrayList<ArrayList<Request>> moresubsets = new ArrayList<ArrayList<Request>>();
			for(ArrayList<Request> s:allsubsets){
				int sum=0;
				ArrayList<Request> newSubset = new ArrayList<Request>();
				newSubset.addAll(s);
				newSubset.add(item);
				for(Request m:newSubset){
					sum=sum+m.passengerNumber;
				}
				if(sum<=siteNumber){
				moresubsets.add(newSubset);
				}
				else {
					continue;
				}
			}
			if(moresubsets.size()>0)
			   allsubsets.addAll(moresubsets);
		}
		return allsubsets;
	}
	
	//grouping in advance
	public ArrayList<ArrayList<Request>> group(){
		Comparator<Request> comparator=new Comparator<Request>() {
			
			public int compare(Request s1,Request s2){
				if(s1.passengerNumber>s2.passengerNumber)
					return 1;
				else if(s1.passengerNumber==s2.passengerNumber)
					return 0;
				else
					return -1;
			}
		};
		
		Collections.sort(requests,comparator);
		ArrayList<ArrayList<Request>> groups=getSubsets(requests, 0);
		
		/*
		int i=0;
		System.out.println(groups.size());
		for(i=0;i<groups.size();i++)
			System.out.println("group: "+groups.get(i).size());
			for(int j=0;j<groups.get(i).size();j++){
				System.out.println("zz");
				System.out.println(groups.get(i).get(j).passengerNumber);
			}
			*/
		return groups;
	}
}

