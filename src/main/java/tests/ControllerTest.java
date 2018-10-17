package tests;

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
	@Ignore
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

}
