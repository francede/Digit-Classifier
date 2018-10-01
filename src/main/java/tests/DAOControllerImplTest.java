package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import orm.DAOController;
import orm.DAOControllerImpl;

public class DAOControllerImplTest {
	
	public static DAOController DAOController;
	
	@BeforeClass public static void initTest(){
		DAOController = new DAOControllerImpl();
	}

	@Test
	public void test() {
		DAOController.getWeightsFromDatabase();
	}

}
