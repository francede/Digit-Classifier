package controller;

import java.util.ArrayList;

import model.InputDataNumberImages;
import model.Matrix;
import model.NeuralNetwork;
import orm.DAOController;
import orm.DAOControllerImpl;
import view.Gui;

public class ControllerImpl implements Controller {

	// TODO:
	// Import and instantiate DAO-controller

	private Gui gui;
	private NeuralNetwork neuralNetwork;
	private IDXImageFileReader IDXImageFileReader;
	private DAOController DAOController;
	private final int[] NETWORK_LAYER_SIZES = { 2, 2, 1 };

	public ControllerImpl(Gui gui) {
		this.gui = gui;
//		this.neuralNetwork = new NeuralNetworkImpl(LAYER_SIZES);
		this.IDXImageFileReader = new IDXImageFileReaderImpl();
		this.DAOController = new DAOControllerImpl();
	}

	@Override
	public double[] makePrediction(int[] ImageAsPixels) {
		// Waiting for neural network to be finished, returns now a random double[] for testing purposes
		return new double[] {1, 0.1, 0.2, 0.4, 0.564, 0.543, 0.143, 0.2, 0.5, 0.113};
<<<<<<< HEAD:src/main/java/neumeroverkko/ControllerImpl.java

=======
>>>>>>> 21705c3bbf5238417871ad8aa85965ae7f14e7da:src/main/java/controller/ControllerImpl.java

//		double[] predictions = null;
//		predictions = neuralNetwork(ImageAsPixels);
//		return predictions;
	}

	@Override
<<<<<<< HEAD:src/main/java/neumeroverkko/ControllerImpl.java
=======

>>>>>>> 21705c3bbf5238417871ad8aa85965ae7f14e7da:src/main/java/controller/ControllerImpl.java
	public void trainNetwork(int amountOfTrainingImages) {
		ArrayList<InputDataNumberImages> trainingSet;
		int amountOfImagesProcessedAtaTime = 10;
		for (int i = 0, j = 0; i <= amountOfTrainingImages; i++, j++) {
			if (j == amountOfImagesProcessedAtaTime | i == amountOfTrainingImages) {
				trainingSet = IDXImageFileReader.getMultipleImagesAsPixels(j);
//				neuralNetwork.trainWithaTrainingSet(trainingSet);
//				gui.showProgress(i, amountOfTrainingNumbers);
				j = 0;
			}
		}
	}

	@Override
	public void saveNetwork() {
//		Matrix[] weights = neuralNetwork.getWeights();
//		Matrix[] biases = neuralNetwork.getBiases();
//		DAOController.putWeightsAndBiasesToDatabase(weights, biases);
	}

<<<<<<< HEAD:src/main/java/neumeroverkko/ControllerImpl.java

=======
>>>>>>> 21705c3bbf5238417871ad8aa85965ae7f14e7da:src/main/java/controller/ControllerImpl.java
	@Override
	public void loadNetwork() {
		Matrix[] weights = null;
		Matrix[] biases = null;
//		weights = DAOController.getWeightsFromDatabase
//		biases = DAOController.getBiasesFromDatabase
//		neuralNetwork.setWeights(weights);
//		neuralNetwork.setBiases(biases);

	}

	@Override
	public void resetNetwork() {
		// neuralNetwork.reset();
	}

}