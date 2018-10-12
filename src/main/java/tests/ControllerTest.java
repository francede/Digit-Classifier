package tests;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import controller.ControllerImpl;
import model.*;

public class ControllerTest {

	public static ControllerImpl controller;

	@BeforeClass
	public static void initTest() {
		controller = new ControllerImpl(null);
	}

	/**
	 * Trains a network with default layer sizes and tests it with a number training
	 * set. Prints the predictions of each test number to console.
	 */
	@Test
	public void tryPredictionsWithTestSetNumbers() {
		System.out.println("tryPredictionsWithTestSetNumbers");
		int amountOfTrainingData = 10000;
		int amountOfTestData = 10;
		controller.resetNetwork();
		controller.trainNetwork(amountOfTrainingData);
		ArrayList<InputData> inputdataList = controller.getIDXImageFileReader()
				.getTheFirstXAmountOfNumbersFromTrainingFile(amountOfTestData);
		int amountOfPredictionsThatGotRight = 0;
		int[] predictionsThatGotRight = new int[10];
		int[] totalPredictions = new int[10];
		for (InputData inputdata : inputdataList) {
			inputdata = (InputDataNumberImages) inputdata;
			double[] predictions = controller.makePrediction(inputdata);
			System.out.println("Predictions: ");
			int prediction = 0;
			double predictionChance = 0;
			for (int i = 0; i < predictions.length; i++) {
				if (predictions[i] > predictionChance) {
					predictionChance = predictions[i];
					prediction = i;
				}
				System.out.printf(("[" + i + "]: %.5f \n"), predictions[i]);
			}
			if (Integer.parseInt(inputdata.getLabel()) == prediction) {
				amountOfPredictionsThatGotRight++;
				predictionsThatGotRight[prediction]++;
			}
			totalPredictions[prediction]++;

			System.out.println("Label: " + inputdata.getLabel() + ", prediction: " + prediction);
			System.out.println();
		}
		System.out.println("---------------------------------");
		System.out.println("amountOfPredictionsThatGotRight: " + amountOfPredictionsThatGotRight);
		for (int i = 0; i < predictionsThatGotRight.length; i++) {
			System.out.println("[" + i + "]: " + predictionsThatGotRight[i] + "/" + totalPredictions[i]);
		}
		System.out.println("---------------------------------");
	}

	/**
	 * Creates multiple neural networks with variable hidden layer sizes and
	 * produces a file with performance results. The hidden layer sizes are
	 * multiplied by 2 in each iteration.
	 */
//	@Ignore("This should be implemented as a separate class")
	@Test
	public void tryPredictionsWithTestSetNumbersAndDifferentHiddenLayerCombinations() {
		System.out.println("tryPredictionsWithTestSetNumbersAndDifferentHiddenLayerCombinations");
		String fileName = "analyze.txt";
		int amountOfTrainingData = 100;
		int amountOfTestData = 100;
		int amountOfRetrainingForEachNetwork = 10;
		int inputLayerSize = 784;
		int outputLayerSize = 10;
		int hiddenLayer1StartingSize = 2;
		int hiddenLayer1EndingSize = 8;
		int hiddenLayer2StartingSize = 2;
		int hiddenLayer2EndingSize = 8;

		ArrayList<Integer> network_layer_sizes = new ArrayList<Integer>();
		long startTime = System.nanoTime();
		System.out.println("Fetching images from files (this might take some time)");
		ArrayList<InputData> trainingSet = controller.getIDXImageFileReader()
				.getMultipleImagesAsPixels(amountOfTrainingData);
		ArrayList<InputData> testingSet = controller.getIDXImageFileReader()
				.getTheFirstXAmountOfNumbersFromTrainingFile(amountOfTestData);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.printf("Fetching images done. Took %.2f seconds.\n", duration * Math.pow(10, -9));
		System.out.println("Training and analyzing networks (this might take some time)");
//		network_layer_sizes.add(2);
//		network_layer_sizes.add(2);
		int n = network_layer_sizes.size() - 1;

		for (int i = 1; i < network_layer_sizes.size(); i++) {
			for (int j = hiddenLayer1StartingSize; j <= hiddenLayer1EndingSize; j *= 2) {
				network_layer_sizes.set(n - i, j);
				for (int k = hiddenLayer2StartingSize; k <= hiddenLayer2EndingSize; k *= 2) {
					long startTimeOfTraining = System.nanoTime();
					network_layer_sizes.set(n, k);
					int[] network_layer_sizes_array = new int[4];
					network_layer_sizes_array[0] = inputLayerSize;
					network_layer_sizes_array[1] = network_layer_sizes.get(0);
					network_layer_sizes_array[2] = network_layer_sizes.get(1);
					network_layer_sizes_array[3] = outputLayerSize;

					int totalAmountOfPredictionsThatGotRight = 0;

					for (int reTrain = 0; reTrain < amountOfRetrainingForEachNetwork; reTrain++) {
						controller.setNetwork_layer_sizes(network_layer_sizes_array);
						controller.resetNetwork();
						controller.trainNetworkTimeEfficiently(trainingSet);

						int amountOfPredictionsThatGotRight = 0;
						int[] predictionsThatGotRight = new int[outputLayerSize];
						int[] totalPredictionsPerOutput = new int[outputLayerSize];

						for (InputData inputdata : testingSet) {
							inputdata = (InputDataNumberImages) inputdata;
							double[] predictions = controller.makePrediction(inputdata);
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
					long endTimeOfTraining = System.nanoTime();
					try {
						FileWriter fileWriter = new FileWriter(fileName, true);
						PrintWriter printWriter = new PrintWriter(fileWriter);
						duration = endTimeOfTraining - startTimeOfTraining;

						printWriter.println(
								"Layersizes: " + network_layer_sizes_array[1] + " " + network_layer_sizes_array[2]);
						printWriter.printf(
								"amountOfPredictionsThatGotRight: " + totalAmountOfPredictionsThatGotRight + "/"
										+ amountOfTestData * 10 + " (%.2f %%)\n",
								(totalAmountOfPredictionsThatGotRight
										/ (double) (amountOfTestData * amountOfRetrainingForEachNetwork) * 100));
						printWriter.println();
						printWriter.printf("Took %.2f seconds", duration * Math.pow(10, -9));
						printWriter.println();
						printWriter.println();
						printWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}

				}

			}
		}
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.printf("Analyze complete. Took %.2f seconds.\n", duration * Math.pow(10, -9));
	}

}
