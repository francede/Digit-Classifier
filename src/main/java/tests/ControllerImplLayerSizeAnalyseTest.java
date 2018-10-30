package tests;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

import controller.ControllerImplLayerSizeAnalyze;
import controller.LayerSizeIncreaseOperators;

public class ControllerImplLayerSizeAnalyseTest {

	/**
	 * This doesn't actually test the code, but provides an easy way to run the
	 * analyzing part of the program.
	 */
	@Ignore("This is just meant to run the layer analyzing part of the program")
	@Test
	public void test() {
		ControllerImplLayerSizeAnalyze analyzer = new ControllerImplLayerSizeAnalyze();
		analyzer.setHiddenLayerStartingSizes(new int[] { 30, 30, 30 });
		analyzer.setHiddenLayerEndingSizes(new int[] { 90, 90, 90 });
		analyzer.setLayerSizeIncreaseOperator(LayerSizeIncreaseOperators.SUM);
		analyzer.setLayerSizeIncreaseAmount(30);
		analyzer.setAmountOfRetrainingForEachNetwork(1);
		analyzer.setAmountOfTestingForEachLayerSetup(5);
		analyzer.setAmountOfTrainingData(60000);
		analyzer.setAmountOfTestData(10000);
		analyzer.setFileName("NNAnalyzeThreeLayers.txt");
		analyzer.startAnalyze();
	}
}
