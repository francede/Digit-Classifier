package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import neumeroverkko.Controller;
import neumeroverkko.ControllerImpl;
import neumeroverkko.Main;

public class ControllerTest {
	
	public static Controller controller;
	
	
	@BeforeClass public static void initTest(){
		controller = new ControllerImpl(new Main());
	}

	@Test
	public void testTrainNetwork() {
		controller.trainNetwork(24);
	}

}
