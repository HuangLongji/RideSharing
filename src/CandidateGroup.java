import java.util.ArrayList;

public class CandidateGroup {
	public ArrayList<ArrayList<Request>> group;
	public ArrayList<CandidateRequest> candidateGroup=new ArrayList<CandidateRequest>();
	
	public CandidateGroup(ArrayList<ArrayList<Request>> group){
		this.group=group;
	}
	
	public ArrayList<CandidateRequest> prun(){
		group.remove(0);
		for(ArrayList<Request> request1:group){
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
//				System.out.println("globe="+globe);
			}
			if(globe==true){
				candidateGroup.add(new CandidateRequest(request1, pathGroup.get(0)));
				//this arraylist contains request and the path stored in the pathGroup[0]
//				System.out.println(pathGroup.get(0));
			}
		}
			
		return candidateGroup;
	}
	
	public link matchGroup(ArrayList<Integer> A,ArrayList<Integer> B){
		link M=new link(false, new ArrayList<Integer>());
		M=match(A,B);
		if(M.flag==false){
			M=match(B, A);
		}
		return M;
	}
	
	public link match(ArrayList<Integer> a,ArrayList<Integer> b){
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
//					System.out.println("3:test0!");
					X.flag=true;
					ArrayList<Integer> path=new ArrayList<Integer>();
					path.addAll(a);
					for(int i=a.size()-index;i<b.size();i++){
						path.add(b.get(i));
					}
					X.linkPath.addAll(path);
				}else {
//					System.out.println("test1!");
				}
			}
		}else {
//			System.out.println("test2!");
		}
		
		return X;
	}
	
	public class link{
		boolean flag;
		ArrayList<Integer> linkPath;
		
		public link(boolean flag,ArrayList<Integer> linkPath){
			this.flag=flag;
			this.linkPath=linkPath;
		}
	}
}
