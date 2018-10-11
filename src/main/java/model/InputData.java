package model;

/**
 * General interface for any kind of input data that could be inserted to the neural network.
 */
public interface InputData {
	public double[] getInput();
	public double[] getTarget();
	public String getLabel();
	public void setLabelAndTarget(String label);
}
