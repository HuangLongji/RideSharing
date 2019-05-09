import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Firedown {
	public double T;
	public double Tmin;
	public double r=0.1;
	public int NumPassenger;
	public ArrayList<Request> Passengerrequests;
	double para=0.5;
	public int GraphNum;
	public static CandidateRequest2 optiamlGroup;
	public Map<Integer, ArrayList<WeightedEdge>> graphMap;
	public int Ncount;
	
	public ArrayList<Request> newResult;
	public static ArrayList<Integer> listIndex=new ArrayList<Integer>();
	
	public Firedown(double T,double Tmin,double r,
			ArrayList<Request> newResult,int NumPassenger,ArrayList<Request> Passengerrequests,int GraphNum
			,Map<Integer, ArrayList<WeightedEdge>> graphMap,int Ncount){
		this.newResult=newResult;
		this.T=T;
		this.r=r;
		this.Tmin=Tmin;
		this.NumPassenger=NumPassenger;
		this.Passengerrequests=Passengerrequests;
		this.GraphNum=GraphNum;
		this.graphMap=graphMap;
		this.Ncount=Ncount;
	}

	public CandidateRequest2 compare(CandidateRequest2 candidate){
		Judge judge=new Judge(para, GraphNum,graphMap);
		double currentPrice=0;	
        ArrayList<CandidateRequest2> totalResults=new ArrayList<CandidateRequest2>();
		double dE=candidate.price-currentPrice;
//		System.out.println("dE"+dE);
		while(T>Tmin){
			int count=Ncount;
			while(count>0){
		if(dE>0){				
			currentPrice=candidate.price;
			optiamlGroup=new CandidateRequest2(candidate.request, candidate.totalPath
					, candidate.pickUp, currentPrice);
			candidate=produceRequest(optiamlGroup);
			totalResults.add(optiamlGroup);
		}else if(Math.exp(dE/T)>Math.random()){
			optiamlGroup=new CandidateRequest2(candidate.request, candidate.totalPath
					, candidate.pickUp, candidate.price);
			currentPrice=candidate.price;
			candidate=produceRequest(optiamlGroup);
			totalResults.add(optiamlGroup);
		}else{
			Random pRandom=new Random();
			int x;
			x=Math.abs(pRandom.nextInt())%Passengerrequests.size();
			ArrayList<Request> xXRequests=new ArrayList<Request>();
			for(Request number:Passengerrequests){
				if(number.number==x){
					System.out.println("number:"+number.number);
					xXRequests.add(number);
				}			
		   }
			candidate=judge.prun(xXRequests);
		}	
		count--;
			     }
		T=r*T;
		        }
		double maxP=totalResults.get(0).price;
		CandidateRequest2 finGroup=totalResults.get(0);
		for(CandidateRequest2 dd:totalResults){
			if(dd.price>maxP){
				maxP=dd.price;
				finGroup=dd;
			}
		}
		for(Request re:finGroup.request){
			System.out.print(re.number+" ");
		}
		System.out.println();
		System.out.println("Group price:"+finGroup.price);
		try {
			FileOutputStream fos=new FileOutputStream("result.txt",true);
			byte[] b1=String.valueOf(finGroup.price).getBytes();
			byte[] b2="\r\n".getBytes();
			fos.write(b1);
			fos.write(b2);
			FileOutputStream fos1=new FileOutputStream("resultNumber.txt",true);
			int requestNumber=finGroup.request.size();
			byte[] b3=String.valueOf(requestNumber).getBytes();
			fos1.write(b3);
			fos1.write(b2);
			FileOutputStream fos2=new FileOutputStream("Dn.txt",true);
			double maxDnprice=Judge.getDn(finGroup.totalPath);
			byte[] b4=String.valueOf(maxDnprice).getBytes();
			fos2.write(b4);
			fos2.write(b2);
			FileOutputStream fos3=new FileOutputStream("single.txt",true);
			double singlePrice=0;
			for(Request st:finGroup.request){
				singlePrice+=judge.getDn(st.path);
			}
			byte[] b5=String.valueOf(singlePrice).getBytes();
			fos3.write(b5);
			fos3.write(b2);
			FileOutputStream fos4=new FileOutputStream("seatNumber.txt",true);
			int seatNumber=0;
			for(int m=0;m<finGroup.request.size();m++){
				seatNumber=seatNumber+finGroup.request.get(m).passengerNumber;
			}
			byte[] b6=String.valueOf(seatNumber).getBytes();
			fos4.write(b6);
			fos4.write(b2);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return optiamlGroup;
	}
	public CandidateRequest2 produceRequest(CandidateRequest2 candidate){
		Judge judge = new Judge(para, GraphNum,graphMap);
		int numberP=0;
		CandidateRequest2 change=null;
		ArrayList<Integer> list=new ArrayList<Integer>();
		ArrayList<Request> newRequests=new ArrayList<Request>();
		ArrayList<CandidateRequest2> allArrayList=new ArrayList<CandidateRequest2>();
		for(Request request:candidate.request){
			numberP+=request.passengerNumber;
			list.add(request.number);
			newRequests.add(request);
			
		}
		if(numberP<=NumPassenger){                
			for(int i=0;i<Passengerrequests.size();i++){
				if(list.indexOf(Passengerrequests.get(i).number)<0&&
						(numberP+Passengerrequests.get(i).passengerNumber)<=NumPassenger){
					newRequests.add(Passengerrequests.get(i));
					ArrayList<Request> can=new ArrayList<Request>();
					for(Request s:newRequests){
						can.add(s);
					}
					change=judge.prun(can);
					if(change!=null){
						allArrayList.add(change);
					}
					newRequests.remove(newRequests.size()-1);
				}
			}
		}else{
			ArrayList<Request> deRequests=new ArrayList<Request>();
			deRequests=decreaseRequest(candidate);
			candidate=judge.prun(deRequests);
		}
		if(allArrayList.size()>0){
			Random random1=new Random();
			int index=random1.nextInt(allArrayList.size());
			return allArrayList.get(index);
		}else{
//			totalResults.add(candidate);
			Random random1=new Random();
			int x;
			  x=Math.abs(random1.nextInt())%Passengerrequests.size();
			ArrayList<Request> xXRequests=new ArrayList<Request>();
			for(Request number:Passengerrequests){
				if(number.number==x){
//					System.out.println("number:"+number.number);
					xXRequests.add(number);
				}
			}
			change=judge.prun(xXRequests);	
			return change;
		}		
	}
	public CandidateRequest2 changeRequest(CandidateRequest2 candidate){
		System.out.println("Entrance 3");
		Judge judge=new Judge(para, GraphNum,graphMap);
		ArrayList<CandidateRequest2> allArrayList=new ArrayList<CandidateRequest2>();
		CandidateRequest2 change=null;
		ArrayList<Integer> index=new ArrayList<Integer>();
		ArrayList<Request> newRequests=new ArrayList<Request>();
		int numberP=0;
		ArrayList<Integer> list=new ArrayList<Integer>();
		for(Request request:candidate.request){
			numberP+=request.passengerNumber;
			list.add(request.number);
			newRequests.add(request);
		}
		int n=0;
		          System.out.println("list"+list);
				  for(int i=0;i<Passengerrequests.size();i++){
					  ArrayList<Request> requests=new ArrayList<Request>();
					  n++;
					  if(list.indexOf(i)<0&&listIndex.indexOf(i)<0&&
							  (numberP+Passengerrequests.get(i).passengerNumber)<NumPassenger){
						  for(Request number:Passengerrequests){
								if(number.number==i){
									newRequests.add(number);
								}
							}
						  for(int j=0;j<newRequests.size();j++){
							  requests.add(newRequests.get(j));
						  }
						  change=judge.prun(requests); 
						 
						  if(change!=null){
							  allArrayList.add(change);
						  }
						  newRequests.remove(newRequests.size()-1);
					  }
				  }
				  if(allArrayList.size()>0){
					  for(CandidateRequest2 f:allArrayList){
						  System.out.println(f.totalPath+"  "+f.price);
					  }
				  }
			if(allArrayList.size()==0){
				System.out.println("listindex"+listIndex);
				Random random1=new Random();
				int x;
				do{
				  x=Math.abs(random1.nextInt())%Passengerrequests.size();
				  System.out.println("x="+x);
				}while(listIndex.indexOf(x)>=0);
				ArrayList<Request> xXRequests=new ArrayList<Request>();
				for(Request number:Passengerrequests){
					if(number.number==x){
						System.out.println("number:"+number.number);
						xXRequests.add(number);
					}
				}
				change=judge.prun(xXRequests);	
				allArrayList.add(change);
			}
			int current=0;
			CandidateRequest2 retuRequest=null;
			for(CandidateRequest2 can:allArrayList){
				double now=can.price;
				if(now>current){
					retuRequest=can;
				}
			}
		System.out.println(retuRequest.totalPath+"  "+retuRequest.price);
		return retuRequest;
	}
	public ArrayList<Request> decreaseRequest(CandidateRequest2 candidate){
		ArrayList<Request> newRequests=new ArrayList<Request>();
		for(Request request:candidate.request){
			newRequests.add(request);
		}
		
		newRequests.remove(0);	
		if(newRequests.size()==0){
			Random random1=new Random();
			int x;
			x=Math.abs(random1.nextInt())%NumPassenger;
			for(Request number:Passengerrequests){
				if(number.number==x){
					newRequests.add(number);
				}
			}
		}
		return newRequests;
	}	
}
