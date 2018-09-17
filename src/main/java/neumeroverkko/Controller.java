package neumeroverkko;

public interface Controller {
	
	/*
	 * Gives the network a number as pixels to make a prediction of and returns it's guess.
	 *
	 * The parameter int[] must contain the pixel values of a grayscale 28x28 image arranged row-wise.
	 *
	 * Returns predictions as probabilities for each number 0-9.
	 */
	public double[] makePrediction(int[] ImageAsPixels);
	
	/*
	 * Reads the training set and trains the network and 
	 * updates GUI to show progress.
	 */
	public void trainNetwork(int amountOfTrainingNumbers); 
	
	public void putWeightsAndBiasesToDatabase();	
	public void getWeightsAndBiasesFromDatabase();
	public void putWeightsAndBiasesToNeuralNetwork(Matrix[] weights, Matrix[] biases);
	
	

}
