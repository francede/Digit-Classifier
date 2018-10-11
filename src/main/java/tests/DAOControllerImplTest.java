package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import model.Matrix;
import model.NeuralNetwork;
import model.NeuralNetworkImpl;
import orm.DAOControllerImpl;

public class DAOControllerImplTest {

	public static DAOControllerImpl DAOController;
	private int[] network_layer_sizes = new int[] { 10, 8, 10 };
	private NeuralNetwork neuralNetwork;

	@BeforeClass
	public static void initTest() {
		DAOController = new DAOControllerImpl();
	}
	
	@Before
	public void initNetwork() {
		System.out.println("--TEST BEGINS--");
		neuralNetwork = new NeuralNetworkImpl(network_layer_sizes);
		neuralNetwork.reset();
	}
	
	/**
	 * Puts biases to the database and recovers them, and compares the data.
	 */
	@Test
	public void testBiasValidity() {		
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
	
	/**
	 * Puts weights to the database and recovers them, and compares the data.
	 */
	@Test
	public void testWeightValidity() {
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
	
	/**
	 * Puts biases and weights to the database and deletes them, and validates that the database is empty.
	 */
	@Test
	public void testDeleteAllDataFromDatabase() {
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
