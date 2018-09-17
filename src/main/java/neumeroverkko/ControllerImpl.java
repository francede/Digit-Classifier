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
		ArrayList<ImageAsPixelsAndLabel> trainingSet;
		int numberOfImagesProcessedAtOnce = 10;
		int imagesProcessed = 0;
		do {
			if (imagesProcessed + numberOfImagesProcessedAtOnce > amountOfTrainingNumbers)
				numberOfImagesProcessedAtOnce = amountOfTrainingNumbers - imagesProcessed;
			trainingSet = IDXImageFileReader.getMultipleImagesAsPixels(numberOfImagesProcessedAtOnce);
			// neuralNetwork.trainWithATrainingSet(trainingSet);
			// gui.showProgress(i, amountOfTrainingNumbers);
			imagesProcessed += numberOfImagesProcessedAtOnce;
		} while (imagesProcessed < amountOfTrainingNumbers);
	}

	@Override
	public void putWeightsAndBiasesToDatabase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getWeightsAndBiasesFromDatabase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void putWeightsAndBiasesToNeuralNetwork(Matrix[] weights, Matrix[] biases) {
		// TODO Auto-generated method stub

	}

}
