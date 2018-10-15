package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import model.InputData;
import model.InputDataNumberImages;
import view.Gui;

/**
 * Creates multiple neural networks with variable hidden layer sizes and
 * produces a file with performance results. The hidden layer sizes are
 * multiplied by 2 in each iteration.
 */
public class ControllerImplLayerSizeAnalyze extends ControllerImpl {
	private int amountOfTrainingData = 60000;
	private int amountOfTestData = 10000;
	private int amountOfTestingForEachLayerSetup = 5;
	private int amountOfRetrainingForEachNetwork = 1;
	private int inputLayerSize = 784;
	private int outputLayerSize = 10;
	private int[] hiddenLayerStartingSizes = new int[] { 32 };
	private int[] hiddenLayerEndingSizes = new int[] { 512 };
	private int amountOfHiddenLayers = hiddenLayerStartingSizes.length;
	private ArrayList<InputData> trainingSet;
	private ArrayList<InputData> testingSet;
	private String fileName = "analyzeHiddenLayerAmount1.txt";

	public ControllerImplLayerSizeAnalyze() {
		super(null);
	}

	public ControllerImplLayerSizeAnalyze(Gui gui) {
		super(null);
	}

	public void startAnalyze() {
		long startTime = System.nanoTime();
		System.out.println("Fetching images from files (this might take some time)");
		trainingSet = super.getIDXImageFileReader().getMultipleImagesAsPixels(amountOfTrainingData);
		testingSet = super.getIDXImageFileReader().getTheFirstXAmountOfNumbersFromTrainingFile(amountOfTestData);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.printf("Fetching images done. Took %.2f seconds.\n", duration * Math.pow(10, -9));
		System.out.println("Training and analyzing networks (this might take some time)");
		startTime = System.nanoTime();
		System.out.println("Analyzing layer setup: " + Arrays.toString(hiddenLayerStartingSizes));
		analyzeSingleLayerSetup(hiddenLayerStartingSizes); // The first case is special
		analyze(hiddenLayerStartingSizes, amountOfHiddenLayers - 1);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.printf("Analyze complete. Took %.2f seconds.\n", duration * Math.pow(10, -9));
	}

	private int[] analyze(int[] layerSizes, int layerToGrow) {
		if (layerSizes[layerToGrow] >= hiddenLayerEndingSizes[layerToGrow]) {
			if (layerToGrow == 0) {
				return layerSizes; // End recursion
			}
			layerSizes[layerToGrow] = hiddenLayerStartingSizes[layerToGrow];
			return analyze(layerSizes, layerToGrow - 1); // Grow previous hidden layer
		} else {
			layerSizes[layerToGrow] *= 2;
			System.out.println("Analyzing layer setup: " + Arrays.toString(layerSizes));
			analyzeSingleLayerSetup(layerSizes);
			if (layerToGrow != layerSizes.length - 1) { // Grow the last hidden layer
				return analyze(layerSizes, layerSizes.length - 1);
			} else {
				return analyze(layerSizes, layerToGrow);
			}
		}
	}

	private void analyzeSingleLayerSetup(int[] hiddenLayerSizes) {
		long startTimeOfAnalyzing = System.nanoTime();
		int[] network_layer_sizes_array = new int[amountOfHiddenLayers + 2];
		network_layer_sizes_array[0] = inputLayerSize;
		for (int i = 1; i < network_layer_sizes_array.length - 1; i++) {
			network_layer_sizes_array[i] = hiddenLayerSizes[i - 1];
		}
		network_layer_sizes_array[network_layer_sizes_array.length - 1] = outputLayerSize;
		int totalAmountOfPredictionsThatGotRight = 0;
		for (int reTestLayerSetup = 0; reTestLayerSetup < amountOfTestingForEachLayerSetup; reTestLayerSetup++) {
			super.setNetwork_layer_sizes(network_layer_sizes_array);
			super.resetNetwork();
			for (int reTrain = 0; reTrain < amountOfRetrainingForEachNetwork; reTrain++) {
				super.trainNetworkTimeEfficiently(trainingSet);
			}
			int amountOfPredictionsThatGotRight = 0;
			int[] predictionsThatGotRight = new int[outputLayerSize];
			int[] totalPredictionsPerOutput = new int[outputLayerSize];
			for (InputData inputdata : testingSet) {
				inputdata = (InputDataNumberImages) inputdata;
				double[] predictions = super.makePrediction(inputdata);
				int predictionWithHighestChance = 0;
				double predictionChance = 0;
				for (int i2 = 0; i2 < predictions.length; i2++) {
					if (predictions[i2] > predictionChance) {
						predictionChance = predictions[i2];
						predictionWithHighestChance = i2;
					}
				}
				if (Integer.parseInt(inputdata.getLabel()) == predictionWithHighestChance) {
					amountOfPredictionsThatGotRight++;
					predictionsThatGotRight[predictionWithHighestChance]++;
				}
				totalPredictionsPerOutput[predictionWithHighestChance]++;
			}
			totalAmountOfPredictionsThatGotRight += amountOfPredictionsThatGotRight;
		}
		long endTimeOfAnalyzing = System.nanoTime();
		try {
			FileWriter fileWriter = new FileWriter(fileName, true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			long duration = endTimeOfAnalyzing - startTimeOfAnalyzing;
			printWriter.print("Layersizes: ");
			for (int i = 0; i < hiddenLayerSizes.length; i++) {
				printWriter.print(network_layer_sizes_array[i] + " ");
			}
			printWriter.println();
			printWriter.printf(
					"amountOfPredictionsThatGotRight: " + totalAmountOfPredictionsThatGotRight + "/"
							+ amountOfTestData * amountOfTestingForEachLayerSetup + " (%.2f %%)\n",
					(totalAmountOfPredictionsThatGotRight
							/ (double) (amountOfTestData * amountOfTestingForEachLayerSetup) * 100));
			printWriter.println();
			printWriter.printf("Took %.2f seconds", duration * Math.pow(10, -9));
			printWriter.println();
			printWriter.println();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
