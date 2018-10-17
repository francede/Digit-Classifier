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
 * produces a file with performance results.
 * 
 * By default, the hidden layer sizes are multiplied by 2 in each iteration.
 * This can be changed by setting the layersize increase operator and layersize
 * increase amount.
 * 
 * To choose how many hidden layers there will be, simply change the arrays
 * {@link #hiddenLayerStartingSizes} and {@link #hiddenLayerEndingSizes}. The length of the arrays will determine the amount of hidden layers.
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
	private int amountOfHiddenLayers;
	private LayerSizeIncreaseOperators layerSizeIncreaseOperator = LayerSizeIncreaseOperators.MULTIPLY;
	private int layerSizeIncreaseAmount = 2;
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
		amountOfHiddenLayers = hiddenLayerStartingSizes.length;
		try {
			isHiddenLayerStartingAndEndingSizesTheSame();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		try {
			FileWriter fileWriter = new FileWriter(fileName, true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("--------------------------------------------");
			printWriter.println("Analysis setup: ");
			printWriter.println("Amount of training data: " + amountOfTrainingData);
			printWriter.println("Amount of test data: " + amountOfTestData);
			printWriter.println("Amount of testing for each layer setup: " + amountOfTestingForEachLayerSetup);
			printWriter.println("Amount of retraining for each network: " + amountOfRetrainingForEachNetwork);
			printWriter.println();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		analyzeSingleLayerSetup(hiddenLayerStartingSizes); // The first setup must be dealt separately because the
															// recursion skips it
		analyze(hiddenLayerStartingSizes, amountOfHiddenLayers - 1);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.printf("Analyze complete. Took %.2f seconds.\n", duration * Math.pow(10, -9));
	}

	private void isHiddenLayerStartingAndEndingSizesTheSame() throws Exception {
		if (hiddenLayerEndingSizes.length != hiddenLayerStartingSizes.length) {
			throw new Exception("Hidden layer starting and ending size arrays are not the same lenght");
		}
	}

	private int[] analyze(int[] layerSizes, int layerToGrow) {
		if (layerSizes[layerToGrow] >= hiddenLayerEndingSizes[layerToGrow]) {
			if (layerToGrow == 0) {
				return layerSizes; // End recursion
			}
			layerSizes[layerToGrow] = hiddenLayerStartingSizes[layerToGrow];
			return analyze(layerSizes, layerToGrow - 1); // Grow previous hidden layer
		} else {
			layerSizes[layerToGrow] = increaseLayerSize(layerSizes[layerToGrow]);
			System.out.println("Analyzing layer setup: " + Arrays.toString(layerSizes));
			analyzeSingleLayerSetup(layerSizes);
			if (layerToGrow != layerSizes.length - 1) { // Grow the last hidden layer
				return analyze(layerSizes, layerSizes.length - 1);
			} else {
				return analyze(layerSizes, layerToGrow);
			}
		}
	}

	private int increaseLayerSize(int i) {
		switch (layerSizeIncreaseOperator) {
		case MULTIPLY:
			i *= layerSizeIncreaseAmount;
			break;
		case SUM:
			i += layerSizeIncreaseAmount;
		}
		return i;
	}

	private void analyzeSingleLayerSetup(int[] hiddenLayerSizes) {
		long startTimeOfAnalyzing = System.nanoTime();
		int[] networkLayerSizes = new int[amountOfHiddenLayers + 2];
		networkLayerSizes[0] = inputLayerSize;
		for (int i = 1; i < networkLayerSizes.length - 1; i++) {
			networkLayerSizes[i] = hiddenLayerSizes[i - 1];
		}
		networkLayerSizes[networkLayerSizes.length - 1] = outputLayerSize;
		int totalAmountOfPredictionsThatGotRight = 0;
		for (int reTestLayerSetup = 0; reTestLayerSetup < amountOfTestingForEachLayerSetup; reTestLayerSetup++) {
			super.setNetworkLayerSizes(networkLayerSizes);
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
				for (int i = 0; i < predictions.length; i++) {
					if (predictions[i] > predictionChance) {
						predictionChance = predictions[i];
						predictionWithHighestChance = i;
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
				printWriter.print(hiddenLayerSizes[i] + " ");
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

	public void setAmountOfTrainingData(int amountOfTrainingData) {
		this.amountOfTrainingData = amountOfTrainingData;
	}

	public void setAmountOfTestData(int amountOfTestData) {
		this.amountOfTestData = amountOfTestData;
	}
	
	/**
	 * Sets how many times each layer setup will be trained and tested. Bigger amount will produce more accurate results.
	 * @param amountOfTestingForEachLayerSetup
	 */
	public void setAmountOfTestingForEachLayerSetup(int amountOfTestingForEachLayerSetup) {
		this.amountOfTestingForEachLayerSetup = amountOfTestingForEachLayerSetup;
	}
	
	/**
	 * Sets how many times the network is trained with the training data.
	 * @param amountOfRetrainingForEachNetwork
	 */
	public void setAmountOfRetrainingForEachNetwork(int amountOfRetrainingForEachNetwork) {
		this.amountOfRetrainingForEachNetwork = amountOfRetrainingForEachNetwork;
	}

	public void setInputLayerSize(int inputLayerSize) {
		this.inputLayerSize = inputLayerSize;
	}

	public void setOutputLayerSize(int outputLayerSize) {
		this.outputLayerSize = outputLayerSize;
	}
	
	/**
	 * Sets the starting point for hidden layer sizes. The array must be of the same length as the ending size array.
	 * @param hiddenLayerStartingSizes
	 */
	public void setHiddenLayerStartingSizes(int[] hiddenLayerStartingSizes) {
		this.hiddenLayerStartingSizes = hiddenLayerStartingSizes;
	}
	
	/**
	 * Sets the ending point for hidden layer sizes. The array must be of the same length as the ending size array.
	 * @param hiddenLayerEndingSizes
	 */
	public void setHiddenLayerEndingSizes(int[] hiddenLayerEndingSizes) {
		this.hiddenLayerEndingSizes = hiddenLayerEndingSizes;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Sets the operator for the increasing of layer sizes.
	 * @param layerSizeIncreaseOperator
	 */
	public void setLayerSizeIncreaseOperator(LayerSizeIncreaseOperators layerSizeIncreaseOperator) {
		this.layerSizeIncreaseOperator = layerSizeIncreaseOperator;
	}
	
	/**
	 * Sets the amount of how much the layer size will be increased in each iteration by the increase operator.
	 * @param layerSizeIncreaseAmount
	 */
	public void setLayerSizeIncreaseAmount(int layerSizeIncreaseAmount) {
		this.layerSizeIncreaseAmount = layerSizeIncreaseAmount;
	}

}
