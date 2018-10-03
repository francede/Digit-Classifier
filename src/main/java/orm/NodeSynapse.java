package orm;


import javax.persistence.*;

@Entity
@Table (name= "NodeSynapse") 

public class NodeSynapse { 
	
	private int NodeSynapseId;
	private Node nodeId;
	private Synapse synapseId;
	private String dir; // Unnecessary, can be removed.
	
	public NodeSynapse() {
	}

	public NodeSynapse(int nodeSynapseId, Node nodeId, Synapse synapseId, String dir) {
		super();
		NodeSynapseId = nodeSynapseId;
		this.nodeId = nodeId;
		this.synapseId = synapseId;
		this.dir = dir;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "NodeSynapse")
	public int getNodeSynapseId() {
		return NodeSynapseId;
	}

	public void setNodeSynapseId(int nodeSynapseId) {
		NodeSynapseId = nodeSynapseId;
	}

	@ManyToOne
	@JoinColumn(name = ("NodeId"))
	public Node getNodeId() {
		return nodeId;
	}

	public void setNodeId(Node nodeId) {
		this.nodeId = nodeId;
	}

	@ManyToOne
	@JoinColumn(name = "SynapseId")
	public Synapse getSynapseId() {
		return synapseId;
	}

	public void setSynapseId(Synapse synapseId) {
		this.synapseId = synapseId;
	}

	@Column(name = "Direction")
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
	
	
	
	

}
