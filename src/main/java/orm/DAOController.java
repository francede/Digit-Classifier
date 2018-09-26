package orm;

import model.Matrix;

public interface DAOController {
	
	public void putWeightsAndBiasesToDatabase(Matrix[] weights, Matrix[] biases);
	public Matrix[] getWeightsFromDatabase();
	public Matrix[] getBiasesFromDatabase();

}
