package orm;

import javax.persistence.*;

@Entity
@Table(name = "synapse")

public class Synapse {
	
	private int synapseId;
	private int layer;
	private double weight;
	
	public Synapse() {
	}

	public Synapse(int synapseId, int layer, double weight) {
		this.synapseId = synapseId;
		this.layer = layer;
		this.weight = weight;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SynapseId")
	public int getSynapseId() {
		return synapseId;
	}

	public void setSynapseId(int synapseId) {
		this.synapseId = synapseId;
	}

	@Column(name = "Layer")
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	@Column(name = "Weight")
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	

}
