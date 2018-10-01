package orm;

import javax.persistence.*;

@Entity
@Table (name= "node") 


public class Node {
	
	private int nodeId;
	private int number;
	private int layer;
	private double bias;
	

	public Node(int nodeId, int number, int layer, double bias) {
		super();
		this.nodeId = nodeId;
		this.number = number;
		this.layer = layer;
		this.bias = bias;
	}

	public Node() {
		
	}

	@Id
	@GeneratedValue
	@Column (name = "NodeId")
	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	@Column (name = "Number")
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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
	
	
	
	
	
	
}
