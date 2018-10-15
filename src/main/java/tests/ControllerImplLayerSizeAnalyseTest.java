package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.ControllerImplLayerSizeAnalyze;

public class ControllerImplLayerSizeAnalyseTest {

	@Test
	public void test() {
		ControllerImplLayerSizeAnalyze analyzer = new ControllerImplLayerSizeAnalyze();
		analyzer.startAnalyze();
	}

}
