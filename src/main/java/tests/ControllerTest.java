package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Controller;
import controller.ControllerImpl;
import model.*;
import view.Gui;

public class ControllerTest {
	
	public static ControllerImpl controller;
	
	
	@BeforeClass public static void initTest(){
		controller = new ControllerImpl(null);
	}
	

	@Test
	public void testTrainNetwork() {
		controller.trainNetwork(24);
	}
	
	@Test
	public void tryPredictionsWithFiveFirstTrainNumbers() {
		System.out.println("tryPredictionsWithFiveFirstTrainNumbers");
		controller.resetNetwork();
		controller.trainNetwork(20000);
		ArrayList<InputData> inputdataList = controller.getIDXImageFileReader().getTheFirstXAmountOfNumbersFromTrainingFile(3000);
		int amountOfPredictionsThatGotRight = 0;
		int[] predictionsThatGotRight = new int[10];
		int[] totalPredictions = new int[10];
		for (InputData inputdata : inputdataList) {
			inputdata = (InputDataNumberImages)inputdata;
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

}
