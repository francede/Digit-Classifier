package controller;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.concurrent.Task;
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

	private int[] network_layer_sizes = {784, 16, 16, 10};

	private double learningRate = 1;

	public ControllerImpl(Gui gui) {
		this.gui = gui;
		this.neuralNetwork = new NeuralNetworkImpl(network_layer_sizes);
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
	public Task trainNetwork(int amountOfTrainingImages) {
		int amountOfImagesProcessedAtaTime = 10;
		return new Task() {
			ArrayList<InputData> trainingSet;
            @Override
            protected Object call() throws Exception {
            	for (int i = 0, j = 0; i <= amountOfTrainingImages; i++, j++) {
            		if (this.isCancelled()) {
        				break;
        			}
            		if (i % 10000 == 0) {
        				if (learningRate - 0.2 <= 0) {
        					learningRate *= 0.5;
        				} else {
        					learningRate -= 0.2;
        				}
        				neuralNetwork.setLearningRate(learningRate);
        			}
        			if (i % 100 == 0) {
        				System.out.println("ControllerImpl: images processed " + i);
        			}
        			if (j == amountOfImagesProcessedAtaTime | i == amountOfTrainingImages) {
        				trainingSet = IDXImageFileReader.getMultipleImagesAsPixels(j);
        				neuralNetwork.trainWithaTrainingSet(trainingSet);
        				updateProgress(i, amountOfTrainingImages);
        				j = 0;
        			}
        		}
            	learningRate = 1.0;
            	return true;
            }
        };
	}

	/**
	 * Trains the network with a trainingset that is initialized in another class.
	 * This is useful if you want to train different networks quickly as the
	 * initialization (reading from a file) takes a lot of time.
	 * @param trainingSet
	 */
	public void trainNetworkTimeEfficiently(ArrayList<InputData> trainingSet) {
		int amountOfImagesProcessedAtaTime = 10;
		ArrayList<InputData> partial_trainingSet = new ArrayList<InputData>();
    	for (int i = 0, j = 0; i < trainingSet.size(); i++, j++) {
    		if (i % 10000 == 0) {
				if (learningRate - 0.2 <= 0) {
					learningRate *= 0.5;
				} else {
					learningRate -= 0.2;
				}
				neuralNetwork.setLearningRate(learningRate);
			}
			partial_trainingSet.add(trainingSet.get(i));
			if (j == amountOfImagesProcessedAtaTime | i == trainingSet.size()) {
				neuralNetwork.trainWithaTrainingSet(partial_trainingSet);
				j = 0;
				partial_trainingSet.clear();
			}
		}
    	learningRate = 1.0;
	}

	public void setLearningRate(double learningRate) {
		neuralNetwork.setLearningRate(learningRate);
	}

	public void setNetwork_layer_sizes(int[] network_layer_sizes) {
		this.network_layer_sizes = network_layer_sizes;
		this.neuralNetwork = new NeuralNetworkImpl(network_layer_sizes);
		this.neuralNetwork.setLearningRate(learningRate);
		this.neuralNetwork.reset();
	}

	@Override
	public void saveNetwork() {
		Matrix[] weights = neuralNetwork.getWeights();
		Matrix[] biases = neuralNetwork.getBiases();
		DAOController.putWeightsToDatabase(weights);
		DAOController.putBiasesToDatabase(biases);
	}

	@Override
	public void loadNetwork() {
		Matrix[] weights = null;
		Matrix[] biases = null;
		weights = DAOController.getWeightsFromDatabase();
		biases = DAOController.getBiasesFromDatabase();
		neuralNetwork.setWeights(weights);
		neuralNetwork.setBiases(biases);
	}

	@Override
	public void resetNetwork() {
		neuralNetwork.reset();
	}

	public IDXImageFileReader getIDXImageFileReader() {
		return IDXImageFileReader;
	}

}
