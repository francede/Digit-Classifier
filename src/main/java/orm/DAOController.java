package orm;

import model.Matrix;

public interface DAOController {
	public Matrix[] getWeightsFromDatabase(int[] layer_sizes);
	public Matrix[] getBiasesFromDatabase();
	public void putWeightsToDatabase(Matrix[] weights);
	public void putBiasesToDatabase(Matrix[] biases);
	public void deleteAllDataInDatabase();

}
