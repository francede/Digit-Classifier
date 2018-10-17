package controller;

import java.util.ArrayList;
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
	private int[] networkLayersizes = {784, 288, 48, 10};

	private double learningRate = 1;

	public ControllerImpl(Gui gui) {
		this.gui = gui;
		this.neuralNetwork = new NeuralNetworkImpl(networkLayersizes);
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

	@Override
	public double[] makePrediction(InputData inputData) {
		double[] predictions = null;
		Matrix matrix = neuralNetwork.makePrediction(inputData);
		predictions = Matrix.matrixToArray(matrix);
		return predictions;
	}

	/**
	 * Prints the pixel values to the console from the file and from the drawn image (range: 0-1).
	 * Useful if you want to compare the actual pixel values.
	 * @param inputData
	 */
	private void printPixelsOfOneImage(InputData inputData) {
		InputData singleImage = IDXImageFileReader.getSingleImageAsPixels();
		System.out.println("Pixels of image from file:");
		for (double pixel : singleImage.getInput()) {
			System.out.println(pixel);
		}
		System.out.println("Pixels of the drawn image:");
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
	 * This is useful if you want to train multiple networks quickly as
	 * reading images from a file takes a lot of time.
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

	public void setNetworkLayerSizes(int[] networkLayerSizes) {
		this.networkLayersizes = networkLayerSizes;
		this.neuralNetwork = new NeuralNetworkImpl(networkLayerSizes);
		this.neuralNetwork.setLearningRate(learningRate);
		this.neuralNetwork.reset();
	}

	@Override
	public void saveNetwork() {
		DAOController.deleteAllDataInDatabase();
		Matrix[] weights = neuralNetwork.getWeights();
		Matrix[] biases = neuralNetwork.getBiases();
		DAOController.putWeightsToDatabase(weights);
		DAOController.putBiasesToDatabase(biases);
	}

	@Override
	public void loadNetwork() {
		Matrix[] weights = null;
		Matrix[] biases = null;
		weights = DAOController.getWeightsFromDatabase(this.networkLayersizes);
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
