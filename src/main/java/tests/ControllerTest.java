package tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import controller.Controller;
import controller.ControllerImpl;
import model.*;
import view.Gui;

public class ControllerTest {

	public static ControllerImpl controller;

	@BeforeClass
	public static void initTest() {
		controller = new ControllerImpl(null);
	}

	@Test
	public void testTrainNetwork() {
		controller.trainNetwork(24);
	}

	@Ignore
	@Test
	public void tryPredictionsWithTestSetNumbers() {
		System.out.println("tryPredictionsWithTestSetNumbers");
		controller.resetNetwork();
		controller.trainNetwork(100);
		ArrayList<InputData> inputdataList = controller.getIDXImageFileReader()
				.getTheFirstXAmountOfNumbersFromTrainingFile(100);
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
				System.out.println("[" + i + "]: " + predictions[i]);
			}
			if (Integer.parseInt(inputdata.getLabel()) == prediction) {
				amountOfPredictionsThatGotRight++;
				predictionsThatGotRight[prediction]++;
			}
			totalPredictions[prediction]++;

//			System.out.println("Label: " + inputdata.getLabel() + ", prediction: " + prediction);
//			System.out.println();
		}
		System.out.println("---------------------------------");
		System.out.println("amountOfPredictionsThatGotRight: " + amountOfPredictionsThatGotRight);
		for (int i = 0; i < predictionsThatGotRight.length; i++) {
			System.out.println("[" + i + "]: " + predictionsThatGotRight[i] + "/" + totalPredictions[i]);
		}
		System.out.println("---------------------------------");
	}

	@Test
	public void tryPredictionsWithTestSetNumbersAndDifferentHiddenLayerCombinations() {
		System.out.println("tryPredictionsWithTestSetNumbersAndDifferentHiddenLayerCombinations");
		String fileName = "analyze.txt";
		BufferedWriter writer;
		int amountOfPredictions = 10000;
		// Iterate through different layer combinations: 1 and 2 layers, sizes varies
		ArrayList<Integer> network_layer_sizes = new ArrayList();
		long startTime = System.nanoTime();
		System.out.println("Fetching images from files (this might take some time)");
		ArrayList<InputData> trainingSet = controller.getIDXImageFileReader().getMultipleImagesAsPixels(60000);
		ArrayList<InputData> testingSet = controller.getIDXImageFileReader()
				.getTheFirstXAmountOfNumbersFromTrainingFile(amountOfPredictions);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.printf("Fetching images done. Took %.2f seconds.\n", duration * Math.pow(10, -9));
		System.out.println("Training and analyzing networks (this might take some time)");
		
		

		network_layer_sizes.add(2);
		network_layer_sizes.add(2);
		int n = network_layer_sizes.size() - 1;

		for (int i2 = 1; i2 < network_layer_sizes.size(); i2++) {
			for (int j = 288; j <= 384; j += 32) {
				network_layer_sizes.set(n - i2, j);
				for (int k = 16; k <= 64; k += 16) {
					long startTimeOfTraining = System.nanoTime();
					network_layer_sizes.set(n, k);
					int[] network_layer_sizes_array = new int[4];
					network_layer_sizes_array[0] = 784;
					network_layer_sizes_array[1] = network_layer_sizes.get(0);
					network_layer_sizes_array[2] = network_layer_sizes.get(1);
					network_layer_sizes_array[3] = 10;
					
					int totalAmountOfPredictionsThatGotRight = 0;
					
					for (int reTrain = 0; reTrain < 10; reTrain++) {

						controller.setNetwork_layer_sizes(network_layer_sizes_array);

						controller.resetNetwork();
						controller.trainNetworkTimeEfficiently(trainingSet);

						int amountOfPredictionsThatGotRight = 0;
						int[] predictionsThatGotRight = new int[10];
						int[] totalPredictionsPerOutputType = new int[10];
						
						for (InputData inputdata : testingSet) {
							inputdata = (InputDataNumberImages) inputdata;
							double[] predictions = controller.makePrediction(inputdata);
//							System.out.println("Predictions: ");
							int prediction = 0;
							double predictionChance = 0;
							for (int i = 0; i < predictions.length; i++) {
								if (predictions[i] > predictionChance) {
									predictionChance = predictions[i];
									prediction = i;
								}
//								System.out.println("[" + i + "]: " + predictions[i]);
							}
							if (Integer.parseInt(inputdata.getLabel()) == prediction) {
								amountOfPredictionsThatGotRight++;
								predictionsThatGotRight[prediction]++;
							}
							totalPredictionsPerOutputType[prediction]++;
						}
						totalAmountOfPredictionsThatGotRight += amountOfPredictionsThatGotRight;
					}
					long endTimeOfTraining = System.nanoTime();

					try {
						FileWriter fileWriter = new FileWriter(fileName, true);
						PrintWriter printWriter = new PrintWriter(fileWriter);
//						printWriter.println("---------------------------------");
						duration = endTimeOfTraining - startTimeOfTraining;
						
						printWriter.println(
								"Layersizes: " + network_layer_sizes_array[1] + " " + network_layer_sizes_array[2]);
						printWriter.printf(
								"amountOfPredictionsThatGotRight: " + totalAmountOfPredictionsThatGotRight + "/"
										+ amountOfPredictions * 10 + " (%.2f %%)\n",
								(totalAmountOfPredictionsThatGotRight / (double) (amountOfPredictions * 10) * 100));
						printWriter.println();
						printWriter.printf("Took %.2f seconds", duration * Math.pow(10, -9));
						printWriter.println();
						printWriter.println();
//						for (int i = 0; i < predictionsThatGotRight.length; i++) {
//							System.out.println("[" + i + "]: " + predictionsThatGotRight[i] + "/"
//									+ totalPredictionsPerOutputType[i]);
//						}
//						printWriter.println("---------------------------------");

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
