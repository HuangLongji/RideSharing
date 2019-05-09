import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Judge {
	public static ArrayList<WeightedEdge> weightEdges=new ArrayList<WeightedEdge>();
	public static double para;
	public static int GraphNum;
	public static CandidateRequest2 candidateRequest;
	public static Map<Integer, ArrayList<WeightedEdge>> graphMap;
	public Judge(double para,int GraphNum,Map<Integer, ArrayList<WeightedEdge>> graphMap){
		this.para=para;
		this.GraphNum=GraphNum;
		this.graphMap=graphMap;
	}
	public static CandidateRequest2 prun(ArrayList<Request> request1){		    
			ArrayList<ArrayList<Integer>> pathGroup=new ArrayList<ArrayList<Integer>>();
			for(int i=0;i<request1.size();i++){
				pathGroup.add(request1.get(i).path);
			}
			boolean globe=true;
			int length=pathGroup.size();
			for(int k=0;k<length-1;k++){
				boolean local=false;
				for(int j=1;j<pathGroup.size();j++){
					link M=matchGroup(pathGroup.get(0), pathGroup.get(j));
//					System.out.println("link"+M.linkPath);
					if(M.flag==true){
						local=true;
						pathGroup.remove(0);
						pathGroup.remove(j-1);
						pathGroup.add(M.linkPath);
						break;
					}
					
				}
				if(local==false){
					globe=false;
					break;
				}
			}
		if(globe==true){
			ArrayList<Integer> linkPath=new ArrayList<Integer>();
			ArrayList<Request> requests=new ArrayList<Request>();
			linkPath=pathGroup.get(0);
			requests=request1;
			ArrayList<Integer> pickUp=new ArrayList<Integer>();
			int[] numSet=new int[linkPath.size()];
			for(int i=0;i<requests.size();i++){
				int x=requests.get(i).start;
				int y=requests.get(i).end;
				int index1=linkPath.indexOf(x);
				int index2=linkPath.indexOf(y);
				for(int j=index1;j<linkPath.size();j++){
					numSet[j]=numSet[j]+1;
				}
				for(int k=index2;k<linkPath.size();k++){
					numSet[k]=numSet[k]-1;
				}
			}
	        for(int i=0;i<numSet.length;i++){
	        
	        	pickUp.add(numSet[i]);
	        }
	        double price=getOptimalPrice(linkPath,pickUp);
			candidateRequest=new CandidateRequest2(requests, linkPath, pickUp,price);
		}
	    
		if(globe==false){
			return null;
		}else{
			return candidateRequest;
		}
		
	}
	
	public static link matchGroup(ArrayList<Integer> A,ArrayList<Integer> B){
//		System.out.println("A"+A+"B"+B);
		link M=new link(false, new ArrayList<Integer>());
		M=matching(A,B);
		if(M.flag==false){
			M=matching(B, A);
		}

		return M;
	}
	public static link matching(ArrayList<Integer> a,ArrayList<Integer> b){
//		System.out.println("a"+a+" "+"b"+b);
		link X=new link(false, new ArrayList<Integer>());
		int index=a.indexOf(b.get(0));
//		System.out.println("index"+index);
		if(index!=-1&&index!=(a.size()-1)){
			boolean local=true;
			if(a.size()-b.size()-index>=0){
				for(int i=0;i<b.size();i++){
					if(a.get(index+i)-b.get(i)!=0){
						local=false;
					}
				}
				if(local==true){
					X.flag=true;
					X.linkPath.addAll(a);
				}else{
//					System.out.println("test!");
				}
			}else{
//				System.out.println("Entrance");
				boolean globe=true;
				for(int i=0;i<a.size()-index;i++){
					if(b.get(i)-a.get(i+index)!=0){
						globe=false;
					}
				}
//				System.out.println("globe"+globe);
				if(globe==true){
					X.flag=true;
					ArrayList<Integer> path=new ArrayList<Integer>();
					path.addAll(a);
					for(int i=a.size()-index;i<b.size();i++){
						path.add(b.get(i));
					}
					X.linkPath.addAll(path);
				}
			}
		}else {
//			System.out.println("5:test!");
		}
		
		return X;
	}
	
	public static link match(ArrayList<Integer> a,ArrayList<Integer> b){
		System.out.println("a"+a+" "+"b"+b);
		link X=new link(false, new ArrayList<Integer>());
		int index=a.indexOf(b.get(0));
		if(index!=-1){
			if(a.size()-b.size()-index>=0){
				if((b.get(b.size()-1)-a.get(index+b.size()-1))==0){
//					System.out.println("1:test!");
					X.flag=true;
					X.linkPath.addAll(a);
				}else {
//					System.out.println("2:test!");
				}
			}else{
				if((a.get(a.size()-1)-b.get(a.size()-index-1))==0
						&&index!=a.size()-1){
//					System.out.println("3:test!");
					X.flag=true;
					ArrayList<Integer> path=new ArrayList<Integer>();
					path.addAll(a);
					for(int i=a.size()-index;i<b.size();i++){
						path.add(b.get(i));
					}
					X.linkPath.addAll(path);
				}else {
//					System.out.println("4:test!");
				}
			}
		}else {
//			System.out.println("5:test!");
		}
		
		return X;
	}
	
	public static class link{
		boolean flag;
		ArrayList<Integer> linkPath;
		
		public link(boolean flag,ArrayList<Integer> linkPath){
			this.flag=flag;
			this.linkPath=linkPath;
		}
	}
	public static double getOptimalPrice(ArrayList<Integer> totalPath,ArrayList<Integer> pickUp){
		ArrayList<Request> optimal=new ArrayList<Request>();			
		double dnPrice=0;
		double drPrice=0;
		double drPrice1=0;
		double totalPrice=0;
		dnPrice=getDn(totalPath);
		for(int j=0;j<pickUp.size();j++){
			if(pickUp.get(j)!=1&&pickUp.get(j)!=0){
				drPrice1=drPrice1+getDr(totalPath.get(j), totalPath.get(j+1));
			}
				
		}
//		System.out.println();
		
			drPrice=para*drPrice1;
//			System.out.println("Dn="+dnPrice+"  Dr="+drPrice);
			totalPrice=dnPrice+drPrice;
			
			return totalPrice;
	}
	public static double getDr(Integer x,Integer y){
		double dr=0;
			for(WeightedEdge dn:graphMap.get(x)){
				if(dn.node==y){
					dr=dn.weight;
					break;
				}
			}
			return dr;
	}
	public static double getDn(ArrayList<Integer> path){
		double total=0;
		for(int i=0;i<path.size()-1;i++){
			for(WeightedEdge dn:graphMap.get(path.get(i))){
				if(dn.node==path.get(i+1)){
					total+=dn.weight;
					continue;
				}
			}
		}
		return total;
	}
}
