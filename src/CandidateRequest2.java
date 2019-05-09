import java.util.ArrayList;


public class CandidateRequest2 {
	public ArrayList<Request> request;
	public ArrayList<Integer> totalPath;
	public ArrayList<Integer>  pickUp;
	public double price;
	
	public CandidateRequest2(ArrayList<Request> request,
			ArrayList<Integer> totalPath, ArrayList<Integer>  pickUp,double price){
		this.request=request;
		this.totalPath=totalPath;
		this.pickUp=pickUp;
		this.price=price;
	}
}
