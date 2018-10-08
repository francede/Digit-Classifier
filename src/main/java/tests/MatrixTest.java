package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.Matrix;
import model.MatrixException;

public class MatrixTest {
	private static Matrix m1;
	private static Matrix m2;
	private static double[] m1arr = {1, 2, 3};
	private static double[][] m2arr = {{1, 2, 3}, {4,5,6}};


	@BeforeClass public static void initTest() throws MatrixException{
		m1 = Matrix.arrayToMatrix(m1arr);
		m2 = Matrix.arrayToMatrix(m2arr);
		System.out.println("Matrix 1:\n" + m1);
		System.out.println("Matrix 2:\n" + m2);
	}

	@Before public void resetMatrix() throws MatrixException{
		m1 = Matrix.arrayToMatrix(m1arr);
		m2 = Matrix.arrayToMatrix(m2arr);
	}

	@Test public void testDotProduct() throws MatrixException {
		System.out.println("---Test dotProduct---");
		Matrix dotResult = Matrix.dotProduct(m2, m1);
		System.out.println("Dot result:\n" + dotResult);
		assertEquals(dotResult.getData()[0][0],14,0.005);
		assertEquals(dotResult.getData()[1][0],9,0.005);

		assertNull("Should return null if a.cols and b.rows don't match",Matrix.dotProduct(m1, m2));
	}

	@Test public void testTranspose(){
		Matrix m2t = Matrix.transpose(m2);
		for(int i = 0; i < m2t.getRows(); i++){
			for(int j = 0; j < m2t.getCols(); j++){
				assertEquals("j x i of original should match i x j of trasposed",
						m2.getData()[j][i],m2t.getData()[i][j], 0.005);
			}
		}
	}

	@Test public void testClone(){
		Matrix m1clone = Matrix.clone(m1);
		for(int col = 0; col < m1.getCols(); col++){
			for(int row = 0; row < m1.getRows(); row++){
				assertEquals(m1.getData()[row][col], m1clone.getData()[row][col], 0.005);
			}
		}
	}
}
