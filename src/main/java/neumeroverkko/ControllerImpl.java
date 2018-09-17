package neumeroverkko;

import java.util.ArrayList;

public class ControllerImpl implements Controller {

	// TODO:
	// Import and instantiate DAO-controller

	private Main gui;
	private NeuralNetwork neuralNetwork;
	private IDXImageFileReader IDXImageFileReader;
	private final int[] LAYER_SIZES = {2, 2, 1};

	public ControllerImpl(Main gui) {
		this.gui = gui;
//		this.neuralNetwork = new NeuralNetworkImpl(LAYER_SIZES);
		this.IDXImageFileReader = new IDXImageFileReaderImpl();
	}

	@Override
	public double[] makePrediction(int[] ImageAsPixels) {
		// Waiting for neural network to be finished, returns now a random double[] for testing purposes
		return new double[] {1, 0.1, 0.2, 0.4, 0.564, 0.543, 0.143, 0.2, 0.5, 0.113};
		
//		double[] predictions = null;
//		predictions = neuralNetwork(ImageAsPixels);
//		return predictions;
	}

	@Override
	public void trainNetwork(int amountOfTrainingNumbers) {				
		for (int i = 0; i < amountOfTrainingNumbers; i++) {
			ImageAsPixelsAndLabel trainingImage;
			trainingImage = IDXImageFileReader.getSingleImageAsPixels();
			// neuralNetwork.train(trainingImage.getPixels(), trainingImage.getLabel());
			// gui.showProgress(i, amountOfTrainingNumbers);		
		}
	}
	
	@Override
	public void saveNetwork() {
//		Matrix[] weights = neuralNetwork.getWeights();
//		Matrix[] biases = neuralNetwork.getBiases();
//		DAOController.putWeightsAndBiasesToDatabase(weights, biases);
	}

	
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
