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
		int amountOfLayers = 2; // nodeAndSynapseObject.getAmountOfLayers();
		Matrix[] weightsAsMatrix = new Matrix[amountOfLayers - 1];
		
		for (int i = 0; i < amountOfLayers - 1; i++) {
			double[][] weights = new double[4][2];// nodeAndSynapseObject.getWeightsFrom(i, i+1)
			weightsAsMatrix[i] = Matrix.arrayToMatrix(weights);
		}
//		int layer0Size = 2;
//		int layer1Size = 4;
//		double[][] weightsFrom0to1 = new double[4][2]; // [rivit], [sarakkeet]
		return weightsAsMatrix;
	}

	@Override
	public Matrix[] getBiasesFromDatabase() {
		int amountOfLayers = 2; 
		// int amountOfLayers = nodeAndSynapseObject.getAmountOfLayers();
		Matrix[] biasesAsMatrix = new Matrix[amountOfLayers - 1];
		
		for (int i = 0; i < amountOfLayers - 1; i++) {
			double[] biases = new double[2]; 
			// double[] biases = nodeAndSynapseObject.getBiasesOfLayer(i)
			biasesAsMatrix[i] = Matrix.arrayToMatrix(biases);
		}
		return biasesAsMatrix;
	}

}
