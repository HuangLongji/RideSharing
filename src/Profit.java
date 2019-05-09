import java.util.ArrayList;


public class Profit {
	public ArrayList<CandidateRequest> candidateGroup;
	public ArrayList<pickupArray> candidateProfitGroup=new ArrayList<pickupArray>();
	public Profit(ArrayList<CandidateRequest> candidateGroup){
		this.candidateGroup=candidateGroup;
	}
	
	public ArrayList<pickupArray> getProfit(){
		int m=1;
		for(CandidateRequest set:candidateGroup){
			ArrayList<Integer> linkPath=new ArrayList<Integer>();
			ArrayList<Request> requests=new ArrayList<Request>();
			linkPath=set.totalPath;  //total path after planning and integrating
			requests=set.request;
			ArrayList<Integer> pickUp=new ArrayList<Integer>();
			int[] numSet=new int[linkPath.size()];
			for(int i=0;i<requests.size();i++){
				int x=requests.get(i).start;
				int y=requests.get(i).end;
				int index1=linkPath.indexOf(x);
				int index2=linkPath.indexOf(y);
				if(index1==-1 || index2==-1)
				    {System.out.println("can't find!");continue;}
				for(int j=index1;j<linkPath.size();j++){
					numSet[j]=numSet[j]+1;
				}
				for(int k=index2;k<linkPath.size();k++){
					numSet[k]=numSet[k]-1;
				}
				
			}
			m++;
			for(int n=0;n<numSet.length;n++){
				pickUp.add(numSet[n]);
			}
			
			candidateProfitGroup.add(new pickupArray(requests, linkPath, pickUp));		
		}
		
		
		return candidateProfitGroup;
	}
	
	public class pickupArray{
		ArrayList<Request>  request;
		ArrayList<Integer>  linkPath;
		ArrayList<Integer>  pickUp;
		
		public pickupArray(ArrayList<Request> request,ArrayList<Integer>  linkPath,ArrayList<Integer> pickUp){
			this.request=request;
			this.linkPath=linkPath;
			this.pickUp=pickUp;
		}
	}
}