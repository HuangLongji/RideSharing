

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class Graph {
	/*
	public Integer name;
	public List<Edge> adj;
	public double dist;
	public Vertex prev;
	
	//indicator that the point in graph is already visited!
	
	public int scratch;
	 */

	public static final double INFINITY = Double.MAX_VALUE;
	public Map<Integer, Vertex> vertexMap = new HashMap<Integer, Vertex>();
	public Map<Integer, ArrayList<WeightedEdge>> graphMap=new HashMap<Integer, ArrayList<WeightedEdge>>();

	public Graph(Map<Integer, ArrayList<WeightedEdge>> graphMap){
		this.graphMap=graphMap;
	}
	
	public void intGraph(){
	//	System.out.println(graphMap.size());
		for(Integer key:graphMap.keySet()){
			//System.out.println("key11:"+key);
			for(WeightedEdge edge:graphMap.get(key)){
		//		System.out.println(key+"-->"+edge.node+"=="+edge.weight);
				addEdge(key, edge.node, edge.weight);
			//	System.out.println("Graph: intGrap:"+" key:"+key+" edge.node:"+edge.node+" edge.weight:"+edge.weight);
			/*
			 * key11:0
                Graph: intGrap: key:0 edge.node:1 edge.weight:79.0
                Graph: intGrap: key:0 edge.node:80 edge.weight:67.0
                Graph: intGrap: key:0 edge.node:141 edge.weight:92.0
			 */
			}
		}
	}
	
	public void addEdge(Integer sourceName, Integer destName, double cost)
	{
		Vertex v = getVertex(sourceName);
		/*
		 * public Integer name;
	public List<Edge> adj;
	public double dist;
	public Vertex prev;
	
	vertex:
	public Vertex dest;
	public double cost;
		 */
		Vertex w = getVertex(destName);
		v.adj.add(new Edge(w, cost));
	}
	
	public void delEdge(Integer goalName){
		Vertex vertex=vertexMap.get(goalName);
		if(vertex!=null){
			vertexMap.remove(goalName);
		}
	}

	public ArrayList<Integer> printPath(Integer destName)
	{
		ArrayList<Integer> path=new ArrayList<Integer>();
		Vertex w = vertexMap.get(destName);
		if (w == null)
			throw new NoSuchElementException();
		else if (w.dist == INFINITY)
			System.out.println(destName + "is unreachable");
		else
		{   
			printPath(w,path);
			//System.out.println();
		}
		return path;
	}
	
	public void dijkstra(Integer startName)
	{
		//System.out.println(12);
		PriorityQueue<Path> pq = new PriorityQueue<Path>();
		Vertex start = vertexMap.get(startName);
		if (start == null)
			throw new NoSuchElementException("Start vertex not found.");
		clearAll();
		pq.add(new Path(start, 0)); //Vertex dest 与cost
		start.dist = 0;

		int nodesSeen = 0;
	   // System.out.println("startname:"+startName);
		while (!pq.isEmpty() && nodesSeen < vertexMap.size())
		{
			Path vrec = pq.remove();
			Vertex v = vrec.dest;
			if (v.scratch != 0)
				continue;
			v.scratch = 1;
			nodesSeen++;

			for (Edge e : v.adj)
			{
				if (e.dest.scratch != 0)
					continue;
				Vertex w = e.dest;
				double cvw = e.cost;

				if (cvw < 0)
					throw new GraphException("Graph has negative edges.");
			
				if (w.dist > v.dist + cvw)
				{   //System.out.println(w.name);
					w.dist = v.dist + cvw;
					w.prev = v;
					//System.out.println(w.prev.name+" "+w.name+" "+w.dist);
					pq.add(new Path(w, w.dist));
				}
			}}
		}
		//print path  pq[Path[w,w.dist].....]
	private Vertex getVertex(Integer vertexName)
	{
		Vertex v = vertexMap.get(vertexName);
		if (v == null)
		{
			v = new Vertex(vertexName);
			vertexMap.put(vertexName, v);
		}

		return v;
	}


	private void printPath(Vertex dest,ArrayList<Integer> path)
	{
		if (dest.prev != null)
		{
			printPath(dest.prev,path);
		}
		path.add(dest.name);
	}

	private void clearAll()
	{
		for (Vertex v : vertexMap.values())
			v.reset();
	}

	@SuppressWarnings("serial")
	class GraphException extends RuntimeException {
		public GraphException(String name)
		{
			super(name);
		}
	}
}
