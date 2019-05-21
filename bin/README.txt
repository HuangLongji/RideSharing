README FILE
-----------------------------------------------------------------------------------------------------------------------------
detailed description of code.
Main file is main.java. Edge.java, Path.java, WeightedEdge.java, Vertex.java and Graph.java is data structure for road network. Request.java is data-structure for request.
The directory of the needed dataset file and the generated dataset file is the current directory of this procedure. Profit.java compute the profit of request group from the list, candidateGroup. 
ProduceRequet.java is a request generator. CandidateGroup.java prunes all possible request groups that do not satisfy constraints, then get candidate request groups. OptimalGroup.java get the optimal request group according the profit of each candidata group. 
-----------------------------------------------------------------------------------------------------------------------------
The directory of generated results.
If you use the eclipse editor, you can find generated results in directory "JRE System Library".
------------------------------------------------------------------------------------------------------------------------------
Dataset filename.
if you want to use your own dataset, changing the variable, dataset filename, in line 25 of Main.java. For example, "BufferedReader br=new BufferedReader(new FileReader(new File("artifical.txt")));". 

