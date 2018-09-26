package orm;

import model.Matrix;

public class DAOControllerImpl implements DAOController {
	
	// TODO: intantiate NodeAndSynapseAccessObject
	
	public void DAOController() {
	}

	@Override
	public void putWeightsAndBiasesToDatabase(Matrix[] weights, Matrix[] biases) {
		
	}

	@Override
	public Matrix[] getWeightsFromDatabase() {
		int layer0Size = 2;
		int layer1Size = 4;
		int layer2Size;
		int layer3Size;
		double[][] weightsFrom0to1 = new double[4][2]; // [rivit], [sarakkeet]
		Matrix weights0Matrix = Matrix.arrayToMatrix(weightsFrom0to1);
		double[] weightsFrom1to2;
		double[] weightsFrom2to3;
		
		return null;
	}

	@Override
	public Matrix[] getBiasesFromDatabase() {
		// TODO Auto-generated method stub
		return null;
	}

}
