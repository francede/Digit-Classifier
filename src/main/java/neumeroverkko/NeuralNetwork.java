package neumeroverkko;

import java.util.ArrayList;

/*
 * All the neural processing is done here.
 */
public interface NeuralNetwork {

	/*
	 * Gives the network a number to make a prediction of and returns it's guess.
	 *
	 * The parameter int[] must contain the pixel values of a grayscale 28x28 image arranged row-wise.
	 *
	 * The parameter int[] may contain the actual number the pixels represent as it's last element.
	 *
	 * The returning double[] contains predictions as probabilities for each number 0-9.
	 */
	public double[] givePrediction(int[] imageAsPixels);

	/*
	 * Trains the network with a training set. The parameter int[] must contain the pixel values
	 * of a grayscale 28x28 image arranged row-wise and the last element must be the actual
	 * number the pixels represent.
	 */
	public void trainWithATrainingSet(ArrayList<int[]> trainingSet);

	/*
	 * Resets the neural network by randomizing all weights and biases.
	 */
	public void reset();

}
