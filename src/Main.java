import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.plaf.synth.SynthSeparatorUI;



public class Main {
	public static void main(String[] args) {
	
			
//		int graphNum=22413;
		int graphNum=1000;
		Map<Integer, ArrayList<WeightedEdge>> graphMap=new HashMap<Integer, ArrayList<WeightedEdge>>();
		Set<Integer> vertex=new HashSet<Integer>();
		try{
		 BufferedReader br=new BufferedReader(new FileReader(new File("artifical.txt")));
//		 BufferedReader br=new BufferedReader(new FileReader(new File("GraphData.txt")));
		 String line=br.readLine();
		 // read the data from file and construct a link structure.
		 // 1-->[2,3,4,5,6]
		 // 2-->[1,3,4,5,9]
		 while(line!=null){
			 if(line=="") break;
			String[] X=line.split(" ");
			int i=Integer.valueOf(X[0]);
			int j=Integer.valueOf(X[1]);
			double weight1=Double.valueOf(X[2]);	
			int weight = (int)(weight1+0.5);
			//System.out.println("i "+i);
			if(graphMap.get(i)==null){
				ArrayList<WeightedEdge> edges=new ArrayList<WeightedEdge>();
				edges.add(new WeightedEdge(j, weight));
				graphMap.put(i, edges);    //grapMap constructs  [i,((),(),(),())]
			}else{
				graphMap.get(i).add(new WeightedEdge(j, weight));
			 }
			
			if(graphMap.get(j)==null){
				ArrayList<WeightedEdge> edges=new ArrayList<WeightedEdge>();
				edges.add(new WeightedEdge(i, weight));
				graphMap.put(j, edges);
			}else{
				graphMap.get(j).add(new WeightedEdge(i, weight));
			}
			line=br.readLine();
		   }
		}catch(Exception e){
			e.printStackTrace();
		}
		int i;
	//	System.out.println(graphMap.size());
		for(i=0;i<graphMap.size();i++){
			//if(graphMap.get(i)!=null)
//			for(WeightedEdge wg:graphMap.get(i)){
//				System.out.println(i+" node: "+wg.node+" "+wg.weight);
//			/*
//			 * 0 node: 1 79.0
//               0 node: 80 67.0
//               0 node: 141 92.0
//               1 node: 0 79.0
//			 */
//			}
		}
		for(int k=0;k<1;k++){  // k is the number of experiments.
		int siteNum=4;
		for(siteNum=4;siteNum<15;siteNum++){   //siteNum is the capacity of vehicle
		for(int m=0;m<20;m++){
		System.out.println("-----------round:"+(m+1)+"  "+k+"--------------");
	    int[] ListCount={50,100,150,200,250};     //the number of request 
		double a=0.5;
		ArrayList<Request> requests=new ArrayList<Request>();
		ArrayList<ArrayList<Request>> group=new ArrayList<ArrayList<Request>>();
		// group  [[request1,request2,....],[...],[]]   requests[request1,request2,....]
		ArrayList<CandidateRequest> candidateGroup=new ArrayList<CandidateRequest>();		
		//candidateGroup [((request1,request2,...), (totalpath(1,2,3,4))),]
		ArrayList<WeightedEdge> weightEdges=new ArrayList<WeightedEdge>();
		// (node,weight):new array
		long starttime3=System.currentTimeMillis();  //k=4 250  1000    4    
		ProduceRequet2 test=new ProduceRequet2(ListCount[k], graphNum, siteNum,graphMap);
		requests=test.produce();    // Dijkstra computation for generating graph.
		
		long starttime1=System.currentTimeMillis();
		group=test.group();   //Grouping the request and pruning the GRT ahead of time.
		
		/*
        for(int i1=0;i1<group.size();i1++){
        	System.out.println("-------------------");
        	for(Request re:group.get(i1)){
        		System.out.println("Group: "+re.number+" "+re.start+" "+re.end+" "+re.passengerNumber+" "+re.path);
        	}
        }
        */
		
		CandidateGroup candidateGroup2=new CandidateGroup(group);
		candidateGroup=candidateGroup2.prun();  //path sequence matching
          
	    /*
        	for(CandidateRequest cr:candidateGroup){
        	    ArrayList<Request> re1 = cr.request;
        	    System.out.println("--------------------");
        	    for(Request re:re1)
        		System.out.println("Group: "+re.number+" "+re.start+" "+re.end+" "+re.passengerNumber+" "+re.path);
        	    System.out.println(cr.totalPath);
        	}
        */	     
		ArrayList<Request> R=new ArrayList<Request>();			
		ArrayList<Profit.pickupArray> ProfitGroup=new ArrayList<Profit.pickupArray>();
		//in pickupArray,it contains the number of sharing roads by num[] (or 1 or -1) represent the sharing roads
		Profit profit=new Profit(candidateGroup);
		ProfitGroup=profit.getProfit();
			
		OptimalGroup priceGroup=new OptimalGroup(ProfitGroup, graphNum,graphMap);
		//System.out.println(ProfitGroup.get(0).pickUp.get(0));
		priceGroup.getOptimalPrice(a);
		long endtime1=System.currentTimeMillis();
		long time1=endtime1-starttime1;    
		long time3=endtime1-starttime3;    
		long time4=starttime3-starttime1;  
			try {
				FileOutputStream fos=new FileOutputStream("time1.txt",true);
				FileOutputStream fos3=new FileOutputStream("time3.txt",true);
	            byte[] b2="\r\n".getBytes();
	            byte[] b3=String.valueOf(time1).getBytes();
	            byte[] b4=String.valueOf(time3).getBytes();
	            
	            fos.write(b3);
	            fos.write(b2);
	            
	            fos3.write(b4);
	            fos3.write(b2);
			}catch(Exception e){
				e.printStackTrace();
			}
		for(int n=0;n<1;n++){
		int countList[] ={50,100,150,200,250};
		
		long starttime2=System.currentTimeMillis();
		ArrayList<Request> result=new ArrayList<Request>();
		result.add(requests.get(0));
		Judge judge=new Judge(0.5, graphNum,graphMap);
		CandidateRequest2 candidate=judge.prun(result);
        
		Firedown firedown=new Firedown(countList[n], 0.5, 0.8,result,siteNum, requests,graphNum,graphMap,50);
		System.out.println("Price: "+candidate.price);
	    //firedown.compare(candidate);	
		 
		long endtime2=System.currentTimeMillis();
		long time2=endtime2-starttime2;
		try {
			FileOutputStream fos=new FileOutputStream("time2.txt",true);

            byte[] b2="\r\n".getBytes();
            byte[] b3=String.valueOf(time2).getBytes();
            
            fos.write(b3);
            fos.write(b2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("--------------------------------");
			}
		}
		}
   }
}
}
