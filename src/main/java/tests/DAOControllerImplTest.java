package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
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

	@BeforeClass
	public static void initTest() {
		DAOController = new DAOControllerImpl();
	}

	@Ignore("Replaced by testBiasValidity")
	@Test
	public void getBiasesFromNeuralNetworkAndPutThemToDataBase() {
		System.out.println("TEST BEGIN: getBiasesFromNeuralNetworkAndPutThemToDataBase()");
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(new int[] { 784, 8, 10 });
		neuralNetwork.reset();
		Matrix[] biases = neuralNetwork.getBiases();
		for (int i = 0; i < biases.length; i++) {
			double[] layerAsDoubles = Matrix.matrixToArray(biases[i]);
			System.out.println("Layer " + i + " biases: ");
			for (int j = 0; j < layerAsDoubles.length; j++) {
				System.out.println("[" + j + "]: " + layerAsDoubles[j]);
			}
		}
		System.out.println("TEST END: getBiasesFromNeuralNetworkAndPutThemToDataBase()");
//		DAOController.putBiasesToDatabase(biases);
//		DAOController.getBiasesFromDatabase();
	}

	@Ignore("Replaced by testWeightValidity")
	@Test
	public void getWeightsFromNeuralNetworkAndPutThemToDataBase() {
		System.out.println("TEST BEGIN: getWeightsFromNeuralNetworkAndPutThemToDataBase()");
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(new int[] { 784, 8, 10 });
		neuralNetwork.reset();
		Matrix[] biases = neuralNetwork.getWeights();
		for (int i = 0; i < biases.length; i++) {
			double[] layerAsDoubles = Matrix.matrixToArray(biases[i]);
			System.out.println("Weights between layers: " + i + " and " + (1 + i));
			for (int j = 0; j < layerAsDoubles.length; j++) {
				System.out.println("[" + j + "]: " + layerAsDoubles[j]);
			}
		}
		System.out.println("TEST END: getWeightsFromNeuralNetworkAndPutThemToDataBase()");
//		DAOController.putWeightsToDatabase(biases);
	}

	@Test
	public void testBiasValidity() {
		int[] network_layer_sizes = new int[] { 10, 8, 10 };
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(network_layer_sizes);
		neuralNetwork.reset();
		Matrix[] biasesFromNeuralNetwork = neuralNetwork.getBiases();
		DAOController.putBiasesToDatabase(biasesFromNeuralNetwork);
		Matrix[] biasesFromDatabase = DAOController.getBiasesFromDatabase();
		if (biasesFromDatabase == null) {
			fail("Bias matrix from database is null");
		}
		for (int i = 0; i < biasesFromNeuralNetwork.length; i++) {
			assertEquals(biasesFromNeuralNetwork[i].getCols(), biasesFromDatabase[i].getCols());
			assertEquals(biasesFromNeuralNetwork[i].getRows(), biasesFromDatabase[i].getRows());
			for (int j = 0; j < biasesFromNeuralNetwork[i].getCols(); j++) {
				for (int k = 0; k < biasesFromNeuralNetwork[i].getRows(); k++) {
					double[][] biases1 = biasesFromNeuralNetwork[i].getData();
					double[][] biases2 = biasesFromDatabase[i].getData();
					assertEquals(biases1[k][j], biases2[k][j], 0.001);
				}
			}
		}
	}

	@Test
	public void testWeightValidity() {
		int[] network_layer_sizes = new int[] { 10, 8, 10 };
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(network_layer_sizes);
		neuralNetwork.reset();
		Matrix[] weightsFromNeuralNetwork = neuralNetwork.getWeights();
		DAOController.putWeightsToDatabase(weightsFromNeuralNetwork);
		Matrix[] weightsFromDatabase = DAOController.getWeightsFromDatabase(network_layer_sizes);
		if (weightsFromDatabase == null) {
			fail("Weights matrix from database is null");
		}
		for (int i = 0; i < weightsFromNeuralNetwork.length; i++) {
			assertEquals(weightsFromNeuralNetwork[i].getCols(), weightsFromDatabase[i].getCols());
			assertEquals(weightsFromNeuralNetwork[i].getRows(), weightsFromDatabase[i].getRows());
			for (int j = 0; j < weightsFromNeuralNetwork[i].getCols(); j++) {
				for (int k = 0; k < weightsFromNeuralNetwork[i].getRows(); k++) {
					double[][] weights1 = weightsFromNeuralNetwork[i].getData();
					double[][] weights2 = weightsFromDatabase[i].getData();
					assertEquals(weights1[k][j], weights2[k][j], 0.001);
				}
			}
		}
	}

	@Test
	public void testDeleteAllDataFromDatabase() {
		int[] network_layer_sizes = { 10, 8, 10 };
		NeuralNetwork neuralNetwork = new NeuralNetworkImpl(network_layer_sizes);
		neuralNetwork.reset();
		Matrix[] weightsFromNeuralNetwork = neuralNetwork.getWeights();
		DAOController.putWeightsToDatabase(weightsFromNeuralNetwork);
		Matrix[] biasesFromNeuralNetwork = neuralNetwork.getBiases();
		DAOController.putBiasesToDatabase(biasesFromNeuralNetwork);
		DAOController.deleteAllDataInDatabase();
		Matrix[] weightsFromDatabase = DAOController.getWeightsFromDatabase(network_layer_sizes);
		assertEquals(0, weightsFromDatabase.length);
		Matrix[] biasesFromDatabase = DAOController.getBiasesFromDatabase();
		assertEquals(0, biasesFromDatabase.length);
	}
}
