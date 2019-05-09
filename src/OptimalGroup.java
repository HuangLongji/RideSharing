import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;


public class OptimalGroup {
	public ArrayList<Profit.pickupArray> candidateProfitGroup=new ArrayList<Profit.pickupArray>();
	public ArrayList<Price> profitGroup=new ArrayList<OptimalGroup.Price>();
	ArrayList<WeightedEdge> weightEdges=new ArrayList<WeightedEdge>();
	public int GraphNum;
	public static Map<Integer, ArrayList<WeightedEdge>> graphMap;
	public OptimalGroup(ArrayList<Profit.pickupArray> candidateProfitGroup,int GraphNum,Map<Integer, ArrayList<WeightedEdge>> graphMap){
		this.candidateProfitGroup=candidateProfitGroup;
		this.GraphNum=GraphNum;
		this.graphMap=graphMap;
	}	
	public void getOptimalPrice(double a){
		double maxPrice=0;
		double maxDnPrice=0;
		ArrayList<Request> optimal=new ArrayList<Request>();
		
		
		for(int i=0;i<candidateProfitGroup.size();i++){
			double dnPrice=0;  //nosharing
			double drPrice=0;  //sharing
			double drPrice1=0;  
			double totalPrice=0;
			
			dnPrice=getDn(candidateProfitGroup.get(i).linkPath);
			
			for(int j=0;j<candidateProfitGroup.get(i).pickUp.size();j++){
				if(candidateProfitGroup.get(i).pickUp.get(j)!=1&&candidateProfitGroup.get(i).pickUp.get(j)!=0){
					drPrice1=drPrice1+getDr(candidateProfitGroup.get(i).linkPath.get(j), candidateProfitGroup.get(i).linkPath.get(j+1));
				}
			}
			drPrice=a*drPrice1;
			totalPrice=dnPrice+drPrice;
		//	System.out.println("totalPrice: "+totalPrice);
			if(totalPrice>maxPrice){
				maxDnPrice=dnPrice;
				maxPrice=totalPrice;
				optimal=candidateProfitGroup.get(i).request;
			}
		}
		for(Request ss:optimal){
		//	System.out.print("number "+ss.number+" ");
		}
//		System.out.println("optimal price="+maxPrice);
		try{  //dnPrice no sharing  maxPrice all
			double singlePrice=0;
			for(int m=0;m<optimal.size();m++){
				singlePrice=singlePrice+getDn(optimal.get(m).path);
			} //optimal group profit,if no sharing
			System.out.println("single price ="+singlePrice);
		FileOutputStream fos=new FileOutputStream("DriverRecommend.txt",true);
		byte[] b1=String.valueOf(maxPrice).getBytes();  //recommmand to driver the maxprice
		byte[] b2="\r\n".getBytes();
		fos.write(b1);
		fos.write(b2);
		FileOutputStream fos1=new FileOutputStream("requestNumber.txt",true);
		int requestNumber=optimal.size();  //the optimal group contains the number of request
		byte[] b3=String.valueOf(requestNumber).getBytes();
		fos1.write(b3);
		fos1.write(b2);
		FileOutputStream fos2=new FileOutputStream("Dn2.txt",true);
		byte[] b4=String.valueOf(maxDnPrice).getBytes(); //the max nosharing price
		fos2.write(b4);
		fos2.write(b2);
		FileOutputStream fos3=new FileOutputStream("single2.txt",true);
		byte[] b5=String.valueOf(singlePrice).getBytes();//if no sharing,the fee
		fos3.write(b5);
		fos3.write(b2);
		FileOutputStream fos4=new FileOutputStream("seatNumber2.txt",true);
		int seatNumber=0;
		for(int m=0;m<requestNumber;m++){
			System.out.println("this is the passengerNumber:"+optimal.get(m).passengerNumber);
			seatNumber=seatNumber+optimal.get(m).passengerNumber;
		}
		byte[] b6=String.valueOf(seatNumber).getBytes();
		fos4.write(b6);
		fos4.write(b2);
		}catch(Exception e){
			e.printStackTrace();
		}
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
		/*
		 *  1 (node:4,weight:9),(node:3,weight:10)  path:1 4 5
		 */
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
	public ArrayList<Integer> getDrPath(ArrayList<Integer> linkpath,ArrayList<Integer> pickUpArray){
		ArrayList<Integer> drPath=new ArrayList<Integer>();
		int flag1=0;
		int flag2=0;
		for(int i=0;i<pickUpArray.size();i++){
			if(pickUpArray.get(i)!=1){
				flag1=i;
				break;
			}
		}
		
		for(int j=flag1+1;j<pickUpArray.size();j++){
			if(pickUpArray.get(j)==1||pickUpArray.get(j)==0){
				flag2=j;
				break;
			}
		}
		
		for(int w=flag1;w<flag2+1;w++){
			drPath.add(linkpath.get(w));
		}
//		System.out.println(drPath);
		return drPath;
		
	}
	class Price{
		ArrayList<Request> request;
		ArrayList<Integer> linkPath;
		double price;
		
		public Price(ArrayList<Request> request,ArrayList<Integer> linkPath,double price){
			this.request=request;
			this.linkPath=linkPath;
			this.price=price;
		}
	}
}
