package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Controller;
import controller.ControllerImpl;
import view.Gui;

public class ControllerTest {
	
	public static Controller controller;
	
	
	@BeforeClass public static void initTest(){
		controller = new ControllerImpl(new Gui());
	}

	@Test
	public void testTrainNetwork() {
		controller.trainNetwork(24);
	}

}
