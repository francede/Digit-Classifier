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
	private final int[] NETWORK_LAYER_SIZES = {784, 16, 16, 10};
	private double learningRate = 1;

	public ControllerImpl(Gui gui) {
		this.gui = gui;
		this.neuralNetwork = new NeuralNetworkImpl(NETWORK_LAYER_SIZES);
		this.neuralNetwork.setLearningRate(learningRate);
		this.neuralNetwork.reset();
		this.IDXImageFileReader = new IDXImageFileReaderImpl();
		this.DAOController = new DAOControllerImpl();
	}

	@Override
	public double[] makePrediction(double[] imageAsPixels) {
		double[] predictions = null;
		InputData inputData = new InputDataNumberImages(imageAsPixels);
		Matrix matrix = neuralNetwork.makePrediction(inputData);
		predictions = Matrix.matrixToArray(matrix);
		printPixelsOfOneImage(inputData);
		return predictions;
	}
	
	public double[] makePrediction(InputData inputData) {
		double[] predictions = null;
		Matrix matrix = neuralNetwork.makePrediction(inputData);
		predictions = Matrix.matrixToArray(matrix);
		return predictions;
	}


	private void printPixelsOfOneImage(InputData inputData) {
		InputData singleImage = IDXImageFileReader.getSingleImageAsPixels();
		System.out.println("Pixels of image from file:");
		for (double pixel : singleImage.getInput()) {
			System.out.println(pixel);
		}
		System.out.println("Pixels of image from drawn number:");
		for (double pixel : inputData.getInput()) {
			System.out.println(pixel);
		}		
	}

	@Override
	public void trainNetwork(int amountOfTrainingImages) {
		ArrayList<InputData> trainingSet;
		int amountOfImagesProcessedAtaTime = 10;
		for (int i = 0, j = 0; i <= amountOfTrainingImages; i++, j++) {
			if (i % 100 == 0) {
				System.out.println("ControllerImpl: images processed " + i);
			}
			if (i % 10000 == 0) {
				if (learningRate - 0.2 <= 0) {
					learningRate *= 0.5;
				} else {
					learningRate -= 0.2;
				}				
				neuralNetwork.setLearningRate(learningRate);
			}
			
			if (j == amountOfImagesProcessedAtaTime | i == amountOfTrainingImages) {
				trainingSet = IDXImageFileReader.getMultipleImagesAsPixels(j);
				neuralNetwork.trainWithaTrainingSet(trainingSet);
//				gui.showProgress(i, amountOfTrainingImages);
				j = 0;
			}
		}
	}

	public void setLearningRate(double learningRate) {
		neuralNetwork.setLearningRate(learningRate);
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
		neuralNetwork.reset();
	}
	
	public IDXImageFileReader getIDXImageFileReader() {
		return IDXImageFileReader;
	}

}
