package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import controller.Controller;
import controller.ControllerImpl;
import model.Matrix;
import model.NeuralNetwork;
import model.NeuralNetworkImpl;
import orm.DAOController;
import orm.DAOControllerImpl;

public class DAOControllerImplTest {
	
	public static DAOControllerImpl DAOController;
	
	@BeforeClass public static void initTest(){
		DAOController = new DAOControllerImpl();
	}

	@Test
	public void testPutBiasesToDataBase() {
//		DAOController.putBiasesToDatabaseTest();
	}
	
	@Test
	public void getBiasesFromNeuralNetworkAndPutThemToDataBase() {
		System.out.println("TEST BEGIN: getBiasesFromNeuralNetworkAndPutThemToDataBase()");
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(new int[] {784, 8, 10});
		neuralNetwork.reset();
		Matrix[] biases = neuralNetwork.getBiases();
		for (int i = 0; i < biases.length; i++) {
			double[] layerAsDoubles = Matrix.matrixToArray(biases[i]);
			System.out.println("Layer "+ i +" biases: ");
			for (int j = 0; j < layerAsDoubles.length; j++) {
				System.out.println("[" + j + "]: " + layerAsDoubles[j] );
			}
		}
		System.out.println("TEST END: getBiasesFromNeuralNetworkAndPutThemToDataBase()");
		DAOController.putBiasesToDatabase(biases);
		DAOController.getBiasesFromDatabase();
	}
	
	@Test
	public void getWeightsFromNeuralNetworkAndPutThemToDataBase() {
		System.out.println("TEST BEGIN: getWeightsFromNeuralNetworkAndPutThemToDataBase()");
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(new int[] {784, 8, 10});
		neuralNetwork.reset();
		Matrix[] biases = neuralNetwork.getWeights();
		for (int i = 0; i < biases.length; i++) {
			double[] layerAsDoubles = Matrix.matrixToArray(biases[i]);
			System.out.println("Weights between layers: " + i + " and " + (1+i));
			for (int j = 0; j < layerAsDoubles.length; j++) {
				System.out.println("[" + j + "]: " + layerAsDoubles[j] );
			}
		}
		System.out.println("TEST END: getWeightsFromNeuralNetworkAndPutThemToDataBase()");
//		DAOController.putWeightsToDatabase(biases);
	}
	
	@Test
	public void testBiasValidity() {
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(new int[] {784, 8, 10});
		neuralNetwork.reset();
		Matrix[] biasesFromNeuralNetwork = neuralNetwork.getBiases();
		DAOController.putBiasesToDatabase(biasesFromNeuralNetwork);
		Matrix[] biasesFromDatabase = DAOController.getBiasesFromDatabase();
		if (biasesFromDatabase == null) {
			fail("Bias matrix from database is null");
		}
		for(int i = 0; i < biasesFromNeuralNetwork.length; i++) {
			double[] layerFromNeuralNetwork = Matrix.matrixToArray(biasesFromNeuralNetwork[i]);
			double[] layerFromDatabase = Matrix.matrixToArray(biasesFromDatabase[i]);
			for (int j = 0; j < layerFromNeuralNetwork.length; j++) {
				assertEquals(layerFromNeuralNetwork[j], layerFromDatabase[j], 0.001);
			}			
		}		
	}
	
	@Test
	public void testWeightValidity() {
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(new int[] {784, 8, 10});
		neuralNetwork.reset();
		Matrix[] weightsFromNeuralNetwork = neuralNetwork.getWeights();
		DAOController.putWeightsToDatabase(weightsFromNeuralNetwork);
		Matrix[] weightsFromDatabase = DAOController.getWeightsFromDatabase();
		if (weightsFromDatabase == null) {
			fail("Weights matrix from database is null");
		}
		for(int i = 0; i < weightsFromNeuralNetwork.length; i++) {
			double[] synapseFromNeuralNetwork = Matrix.matrixToArray(weightsFromNeuralNetwork[i]);
			double[] synapseFromDatabase = Matrix.matrixToArray(weightsFromDatabase[i]);
			for (int j = 0; j < synapseFromNeuralNetwork.length; j++) {
				assertEquals(synapseFromNeuralNetwork[j], synapseFromDatabase[j], 0.001);
			}			
		}		
	}
}
