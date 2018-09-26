package model;

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
	 * @throws MatrixException
	 */
	public Matrix makePrediction(InputData input);


	/**
	 * Trains the network with a single image
	 */
	public void train(InputData trainData);

	/**
	 * Trains the network with multiple images
	 */
	public void trainWithaTrainingSet(ArrayList<InputData> trainingSet);

	/**
	 * Resets the network by randomizing all weights and biases.
	 */
	public void reset();

	public Matrix[] getWeights();
	public Matrix[] getBiases();
	public void setWeights(Matrix[] weights);
	public void setBiases(Matrix[] biases);

}
