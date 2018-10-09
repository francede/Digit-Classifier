package orm;

import javax.persistence.*;

@Entity
@Table (name= "node") 


public class Node {
	
	private int nodeId;
	private int nodeOrdinal;
	private int layer;
	private double bias;
	

	public Node(int nodeId, int number, int layer, double bias) {
		super();
		this.nodeId = nodeId;
		this.nodeOrdinal = number;
		this.layer = layer;
		this.bias = bias;
	}

	public Node() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "NodeId")
	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	@Column (name = "NodeOrdinal")
	public int getNodeOrdinal() {
		return nodeOrdinal;
	}

	public void setNodeOrdinal(int nodeOrdinal) {
		this.nodeOrdinal = nodeOrdinal;
	}

	@Column (name = "Layer")
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	@Column (name = "Bias")
	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	@Override
	public String toString() {
		return "Node [nodeId=" + nodeId + ", nodeOrdinal=" + nodeOrdinal + ", layer=" + layer + ", bias=" + bias + "]";
	}
	

	
	
	
	
	
	
}
