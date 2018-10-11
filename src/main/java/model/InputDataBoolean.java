package model;
/**
 * InputData implementing class that is specialized to boolean type data
 *
 */
public class InputDataBoolean implements InputData{
	private final double[] input;
	private double[] target;
	private String label;

	public InputDataBoolean(double[] input, boolean label){
		this.input = input;
		this.target = new double[1];
		if(label){
			this.target[0] = 1;
			this.label = "true";
		}else{
			this.target[0] = 0;
			this.label = "false";
		}
	}

	public double[] getInput() {return this.input;}

	public double[] getTarget() {return this.target;}

	public String getLabel() {return this.label;}

	public void setLabelAndTarget(String label) {
		//Not implemented
	}

}
