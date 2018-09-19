package neumeroverkko;

import java.util.ArrayList;

/*
 * All the neural processing is done here.
 */
public interface NeuralNetwork {

	/**
	 * Gives the network a number to make a prediction of and returns it's guess.
	 *
	 * Returns predictions as probabilities for each number 0-9.
	 * 
	 * @param imageAsPixels must contain the pixel values (0 = white, 255 = black) of a grayscale 28x28 image arranged row-wise.
	 * @return predictions as probabilities for each number 0-9.
	 */
	public double[] makePrediction(int[] imageAsPixels);
	
	
	/**
	 * Trains the network with a single image
	 */
	public void train(int[] imageAsPixels, int label);
	
	/**
	 * Trains the network with multiple images
	 */
	public void trainWithaTrainingSet(ArrayList<ImageAsPixelsAndLabel> trainingSet);

	/**
	 * Resets the network by randomizing all weights and biases.
	 */
	public void reset();
	
	public Matrix[] getWeights();
	public Matrix[] getBiases();
	public void setWeights(Matrix[] weights);
	public void setBiases(Matrix[] biases);

}
