package digraph;

public class Edge {

	long idNum;
	String sLabel;
	String dLabel;
	long weight = 1;
	String eLabel;

	public Edge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		this.idNum = idNum;
		this.sLabel = sLabel;
		this.dLabel = dLabel;
		this.weight = weight;
		this.eLabel = eLabel;
	}


}
