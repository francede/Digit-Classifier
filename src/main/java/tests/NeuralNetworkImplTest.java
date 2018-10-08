package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.InputData;
import model.InputDataBoolean;
import model.Matrix;
import model.Matrix.MapOperation;
import model.NeuralNetwork;
import model.NeuralNetworkImpl;

public class NeuralNetworkImplTest {
	private static NeuralNetworkImpl nn;
	private static int[] layers = {2,3,2};
	private static MapOperation af = (x) -> x;
	private static MapOperation afd = (x) -> 1;
	private static InputData i = new InputDataBoolean(new double[]{1,0}, true);

	@Before
	public void resetNetwork(){
		nn = new NeuralNetworkImpl(layers);
		nn.setActivationFunction(af, afd);
		nn.setLearningRate(1);
		Matrix[] weights = nn.getWeights();
		for(Matrix layerWeights : weights){
			layerWeights.add(1);
		}
		nn.setWeights(weights);
	}

	@Test
	public void testMakePrediction() {
		assertEquals(nn.makePrediction(i).getData()[0][0],3.0,0.005);
	}

}
