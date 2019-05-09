import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GenRequest {

	public static void main(String[] args) throws IOException {
		
	int n1=0;
    BufferedReader br=null;
	try {
		br = new BufferedReader(new FileReader(new File("requestconst.txt")));
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    String line=null;
	try {
		line = br.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    while(line!=null){
    line=br.readLine();
	String[] X=line.split(" ");
	n1++;
	int i=Integer.valueOf(X[0]);
	int j=Integer.valueOf(X[1]);
	int m=Integer.valueOf(X[2]);
	double weight=Double.valueOf(X[3]);
	System.out.println(i+" "+j+" "+m+" "+weight);
    }
		//    requestNum   GraphNum
		/*
     int requestNum = 100;
     int GraphNum = 1000;
     ArrayList<Request> requests=new ArrayList<Request>();
     int n=0;
     FileOutputStream fos=null;
		try {
			fos=new FileOutputStream("D:\\data\\requestconst.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
     for(int i=0;i<requestNum;i++){
			Random random1=new Random();
			int x=Math.abs(random1.nextInt())%GraphNum;        
			int y=0;
			if(x>500) y=x-10;
			else y=x+10;
			   
			int z=Math.abs(random1.nextInt())%3+1;
			
				byte[] b1=String.valueOf(n).getBytes();
				n++; 
				byte[] b2=String.valueOf(x).getBytes();
				byte[] b3=String.valueOf(y).getBytes();
				byte[] b6=String.valueOf(z).getBytes();
				byte[] b4=" ".getBytes();
				byte[] b5="\r\n".getBytes();
				
				try {
					fos.write(b1);
					fos.write(b4);
				fos.write(b2);
				fos.write(b4);
				fos.write(b3);
				fos.write(b4);
				fos.write(b6);
				fos.write(b5);
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
      }
     try {
		fos.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
*/	}

	}

