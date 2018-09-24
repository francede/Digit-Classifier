package neumeroverkko;

public class InputDataNumberImages implements InputData{

	private final double[] input;
	private double[] target;
	private String label;

	public InputDataNumberImages(double[] inputAsPixels, int label) {
		this.label = Integer.toString(label);
		this.input = new double[inputAsPixels.length];
		for(int i = 0; i < inputAsPixels.length; i++){
			this.input[i] = inputAsPixels[i] / 255.0;
		}
		this.target = new double[10];
		this.target[label] = 1.0;
	}

	public InputDataNumberImages(double[] inputAsPixels) {
		this.target = null;
		this.label = null;
		this.input = new double[inputAsPixels.length];
		for(int i = 0; i < inputAsPixels.length; i++){
			this.input[i] = inputAsPixels[i] / 255.0;
		}
	}

	public double[] getInput() {return this.input;}

	public double[] getTarget() {return this.target;}

	public String getLabel() {return this.label;}

	public void setLabelAndTarget(String label){
		int index;
		try{
			index = Integer.parseInt(label);
			target = new double[10];
			target[index] = 1.0;
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
		}
	}

}
