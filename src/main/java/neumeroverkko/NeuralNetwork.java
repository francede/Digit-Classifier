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
	 * Returns predictions as probabilities for each number 0-9.
	 */
	public double[] makePrediction(int[] imageAsPixels);

	/*
	 * Trains the network with a training set. The image objects in the parameter must 
	 * contain the pixel values of a grayscale 28x28 image arranged row-wise and have a label assigned.
	 */
	public void trainWithATrainingSet(ArrayList<ImageAsPixelsAndLabel> trainingSet);

	/*
	 * Resets the neural network by randomizing all weights and biases.
	 */
	public void reset();

}
