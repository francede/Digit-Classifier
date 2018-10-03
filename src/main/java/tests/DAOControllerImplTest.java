package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

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

}
