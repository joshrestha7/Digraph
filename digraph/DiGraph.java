package digraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import minbinheap.EntryPair;
import minbinheap.MinBinHeap;

public class DiGraph implements DiGraph_Interface {

	HashMap<String, Vertex> vertices;
	private HashMap<Long, Edge> edges;
	private HashSet<Long> vertIds;
	private HashSet<Long> edgeIds;
	private int vNum;
	private int eNum;

	public DiGraph() {
		vertices = new HashMap<String, Vertex>();
		edges = new HashMap<Long, Edge>();
		vertIds = new HashSet<Long>();
		edgeIds = new HashSet<Long>();
		vNum = 0;
		eNum = 0;
	}

	@Override
	public boolean addNode(long idNum, String label) {
		if (idNum >= 0 && !(vertIds.contains(idNum)) && //idNum is unique and >= 0
				!(vertices.containsKey(label)) && //label is unique
				label != null) { 

			vertIds.add(idNum); 	// add idNum to hashset
			vertices.put(label, new Vertex(idNum, label)); //add vertex to hashmap
			vNum++; //increment number of vertexes
			return true;
		}
		return false;
	}

	@Override
	public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		if (idNum >= 0 && !(edgeIds.contains(idNum)) && //idNum is unique and >= 0
				vertices.containsKey(sLabel) && vertices.containsKey(dLabel) && // sLabel and dLabel are in the graph
				!(vertices.get(sLabel).outEdges.contains(dLabel))) { //there is not an edge between the two nodes

			edgeIds.add(idNum); //add idNum to hashset
			vertices.get(sLabel).outEdgeId.put(dLabel, idNum); //edge idNum is saved in the sLabel node
			vertices.get(sLabel).outEdges.add(dLabel); //dLabel is an outedge of sLabel
			vertices.get(sLabel).outNum++; //number of outedges of sLabel incremented
			vertices.get(dLabel).inEdges.add(sLabel); //sLabel is an inedge of dLabel
			vertices.get(dLabel).inNum++; //number of inedges of dLabel incremented
			edges.put(idNum, new Edge(idNum, sLabel, dLabel, weight, eLabel)); //edge added to hashmap
			eNum++;
			return true;
		}
		return false;
	}

	@Override
	public boolean delNode(String label) {
		if (vertices.containsKey(label)) { //hashmap contains vertex 
			for (String outEdge : vertices.get(label).outEdges) {
				this.delEdge(label, outEdge);
			}
			for (String inEdge : vertices.get(label).inEdges) {
				this.delEdge(inEdge, label);
			}
			vertIds.remove(vertices.get(label).idNum); // remove vertex id from hashset
			vertices.remove(label); // remove vertex from hashmap
			vNum--; //decrement number of vertexes
			return true;
		}
		return false;
	}

	@Override
	public boolean delEdge(String sLabel, String dLabel) {
		if (vertices.get(sLabel) != null && vertices.get(dLabel) != null && //sLabel and dLabel are not null
				vertices.get(sLabel).outEdges.contains(dLabel) && //edge exists between the vertexes
				vertices.containsKey(sLabel) && vertices.containsKey(dLabel)) { //sLabel and dLabel are in the hashmap
			
			edgeIds.remove(vertices.get(sLabel).outEdgeId.get(dLabel)); //remove edge id from hashset
			vertices.get(sLabel).outEdges.remove(dLabel); // dLabel is not outedge of sLabel
			vertices.get(sLabel).outNum--; //number of outedges of sLabel decremented
			vertices.get(dLabel).inEdges.remove(sLabel); // sLabel is not inedge of dLabel
			vertices.get(dLabel).inNum--; //number of inedges of dLabel is decremented
			eNum--; //number of edges decremented
			return true;
		}
		return false;
	}

	@Override
	public long numNodes() {return vNum;}

	@Override
	public long numEdges() {return eNum;}

	@Override
	public String[] topoSort() {
		HashMap<String, Vertex> vertices = this.vertices; // copy of vertices graph
		Queue<Vertex> inNumZero = new LinkedList<Vertex>();  // LL of vertexes with inNum == 0

		for (Vertex vertex : vertices.values()) { // for each vertex from vertices hashmap
			if (vertex.inNum == 0) {
				inNumZero.add(vertex); //add vertex with inNum == 0 to LL
			}
		}
		
		if (inNumZero.isEmpty()) { return null; }

		String[] topoSort = new String[vNum]; // instantiate return string [] 
		int i = 0;
		while (!(inNumZero.isEmpty())) { // while LL still has vertexes
			Vertex sVertex = inNumZero.remove(); // sVertex is the dequeued item
			topoSort[i] = sVertex.label; // add sVertex label to string []
			i++;

			for (Vertex dVertex : sVertex.outEdgeVertexes(vertices)) { // for each dVertex in sVertex outedges
				dVertex.inNum--; // dVertex inNum decrements
				if (dVertex.inNum == 0) { 
					inNumZero.add(dVertex); // if inNum == 0, add dVertex to LL
				}
			}
		}
		if (i != vNum) {
			return null; // if there is a cycle
		}
		
		return topoSort;
	}

	@Override
	public ShortestPathInfo[] shortestPath(String label) {		

		MinBinHeap pq = new MinBinHeap();
		Set<String> keys = vertices.keySet();
		ShortestPathInfo[] shortestPaths = new ShortestPathInfo[vNum];
		int i = 0;

		// For start vertex
		if (vertices.containsKey(label)) {
			vertices.get(label).dist = 0;			// Distance to itself is 0			
			pq.insert(new EntryPair(label, 0));	// Start vertex is added to PQ
			boolean first = true;

			while (pq.size() != 0) {				// While pq is not empty
				if (!first) {
					pq.delMin(); 
				}
				if (pq.getMin() != null) {
					Vertex curr = vertices.get(pq.getMin().value);		// Get min from pq
					if (curr != null) {
						if(!curr.known) {					// If curr is not known/visited
							for(Vertex neighbor : curr.outEdgeVertexes(vertices)) {		// For each neighbor
								if (neighbor != null) {
									if (!neighbor.known) {
										int dist = curr.dist + (int) edges.get(curr.outEdgeId.get(neighbor.label)).weight;
										if (dist < neighbor.dist) {
											neighbor.dist = dist;
										}
										pq.insert(new EntryPair(neighbor.label, neighbor.dist));
									}
								}
							}
							curr.known = true;
							shortestPaths[i] = new ShortestPathInfo(pq.getMin().value, pq.getMin().priority);
							keys.remove(curr.label);
							first = false;
						}
					}
					if (i < vNum && curr != null) {
						i++;	
					}
				}
			}
		}
		if (i != vNum - 1) {
			for (String key : keys) {
				shortestPaths[i] = new ShortestPathInfo(key, -1);
				i++;
			}
		}
		return shortestPaths;
	}

}