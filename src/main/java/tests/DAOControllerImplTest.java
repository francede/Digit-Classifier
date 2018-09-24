package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import neumeroverkko.DAOController;
import neumeroverkko.DAOControllerImpl;

public class DAOControllerImplTest {
	
	public static DAOController DAOController;
	
	@BeforeClass public static void initTest(){
		DAOController = new DAOControllerImpl();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
