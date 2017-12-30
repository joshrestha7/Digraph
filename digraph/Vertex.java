package digraph;

import java.util.HashMap;
import java.util.HashSet;

public class Vertex {

	long idNum;
	String label;
	HashMap<String, Long> outEdgeId;
	HashSet<String> inEdges;
	HashSet<String> outEdges;
	int inNum;
	int outNum;
	boolean known;
	int dist;
	
	public Vertex(long idNum, String label) {
		this.idNum = idNum;
		this.label = label;
		this.inEdges = new HashSet<String>();
		this.outEdges = new HashSet<String>();
		this.outEdgeId = new HashMap<String, Long>();
		inNum = inEdges.size();
		outNum = outEdges.size();
		known = false;
		dist = 2147483647;
	}
	
	public HashSet<Vertex> outEdgeVertexes(HashMap<String, Vertex> vertices) {
		HashSet<Vertex> outEdgeVertexs = new HashSet<Vertex>();
		
		for (String outEdge : outEdges) {
			Vertex temp = vertices.get(outEdge);
			outEdgeVertexs.add(temp);
		}
		
		return outEdgeVertexs;
		
	}
}