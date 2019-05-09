import java.util.ArrayList;


public class CandidateRequest{
	public ArrayList<Request> request;
	public ArrayList<Integer> totalPath;
	
	public CandidateRequest(ArrayList<Request> request,
			ArrayList<Integer> totalPath){
		this.request=request;
		this.totalPath=totalPath;
	}
}
