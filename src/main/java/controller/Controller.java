package controller;

import java.util.ArrayList;

import javafx.concurrent.Task;

public interface Controller {

	/**
	 * Gives the network a number to make a prediction of and returns it's guess.
	 *
	 * @param imageAsPixels must contain the pixel values (0 = white, 255 = black) of a grayscale 28x28 image arranged row-wise.
	 * @return predictions as probabilities for each number 0-9.
	 */
	public double[] makePrediction(double[] ImageAsPixels);

	/**
	 * Reads the training set, trains the network with them and
	 * updates GUI to show progress.
	 */
	public Task trainNetwork(int amountOfTrainingImages);

	/**
	 * Resets the neural network by randomizing all the weights and biases
	 */
	public void resetNetwork();

	/**
	 * Saves the network (weights and biases) to the database
	 */
	public void saveNetwork();

	/**
	 * Loads the network (weights and biases) from the database
	 */
	public void loadNetwork();



}
