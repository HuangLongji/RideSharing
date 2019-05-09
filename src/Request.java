import java.util.ArrayList;


public class Request {
	public int number;   //request number
	public int passengerNumber;   //passenger number
	public int start;  //start point of request
	public int end;   //end point of request
	public ArrayList<Integer> path;
	
	public Request(int number,int passengerNumber,int start,int end,ArrayList<Integer> path){
		this.number=number;
		this.passengerNumber=passengerNumber;
		this.start=start;
		this.end=end;
		this.path=path;
	}
}
