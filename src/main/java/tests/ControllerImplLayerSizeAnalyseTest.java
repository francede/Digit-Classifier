package tests;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

import controller.ControllerImplLayerSizeAnalyze;
import controller.LayerSizeIncreaseOperators;

public class ControllerImplLayerSizeAnalyseTest {
	
	@Test
	public void test() {
		ControllerImplLayerSizeAnalyze analyzer = new ControllerImplLayerSizeAnalyze();
		analyzer.setHiddenLayerStartingSizes(new int[] {200});
		analyzer.setHiddenLayerEndingSizes(new int[] {400});
		analyzer.setLayerSizeIncreaseOperator(LayerSizeIncreaseOperators.SUM);
		analyzer.setLayerSizeIncreaseAmount(50);
		analyzer.setAmountOfRetrainingForEachNetwork(1);
		analyzer.setAmountOfTestingForEachLayerSetup(1);
		analyzer.setAmountOfTrainingData(100);
		analyzer.setAmountOfTestData(100);
		analyzer.startAnalyze();
	}
}
