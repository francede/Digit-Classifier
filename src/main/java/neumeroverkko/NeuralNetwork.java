package neumeroverkko;

/*
 * All the neural processing is done here.
 */
public interface NeuralNetwork {
	
	/*
	 * The parameter int[] must contain the pixel values of a grayscale 28x28 image arranged row-wise.
	 * 
	 * The parameter int[] may contain the actual number the pixels represent as it's last element.
	 * 
	 * The returning double[] contains predictions for each number 0-9.
	 */
	public double[] givePrediction(int[] imageAsPixels);
	
	/*
	 * Trains the network by as many iterations as wanted, with a maximum of 60000.
	 */
	public void trainWithTrainingSet(int iterations);

}
