package controller;

import java.util.ArrayList;
import java.util.Arrays;

import model.InputData;
import model.InputDataNumberImages;
import model.Matrix;
import model.NeuralNetwork;
import model.NeuralNetworkImpl;
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
	private final int[] NETWORK_LAYER_SIZES = {784, 8, 8, 10};

	public ControllerImpl(Gui gui) {
		this.gui = gui;
		this.neuralNetwork = new NeuralNetworkImpl(NETWORK_LAYER_SIZES);
		this.IDXImageFileReader = new IDXImageFileReaderImpl();
		this.DAOController = new DAOControllerImpl();
	}

	@Override
	public double[] makePrediction(double[] imageAsPixels) {
		double[] predictions = null;
		InputData inputData = new InputDataNumberImages(imageAsPixels);
		Matrix matrix = neuralNetwork.makePrediction(inputData);
		predictions = Matrix.matrixToArray(matrix);
		//double[] predictions = new double[] {0.1, 0.4, 0.5, 1.0, 0.4, 0.2, 0.3, 0.9, 0.8, 0.4};
		return predictions;
	}

	@Override
	public void trainNetwork(int amountOfTrainingImages) {
		ArrayList<InputData> trainingSet;
		int amountOfImagesProcessedAtaTime = 10;
		for (int i = 0, j = 0; i <= amountOfTrainingImages; i++, j++) {
			if (j == amountOfImagesProcessedAtaTime | i == amountOfTrainingImages) {
				trainingSet = IDXImageFileReader.getMultipleImagesAsPixels(j);
				neuralNetwork.trainWithaTrainingSet(trainingSet);
				//gui.showProgress(i, amountOfTrainingImages);
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
