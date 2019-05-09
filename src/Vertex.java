

import java.util.LinkedList;
import java.util.List;

public class Vertex {   

	public Integer name;
	public List<Edge> adj;
	public double dist;
	public Vertex prev;
	//indicating that whether the vertex has been already visited.

	public int scratch;
	
	public Vertex(int nm)
	{
		name = nm;
		adj = new LinkedList<Edge>();
		reset();
	}
	
	public void reset()
	{
		dist = Graph.INFINITY;
		prev = null;
		scratch = 0;
	}
}

