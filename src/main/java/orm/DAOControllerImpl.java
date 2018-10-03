package orm;

import java.util.ArrayList;

import model.Matrix;

public class DAOControllerImpl implements DAOController {
	
	private NodeAndSynapseAccessObject nodeAndSynapseAccessObject;
	
	public DAOControllerImpl() {
		nodeAndSynapseAccessObject = new NodeAndSynapseAccessObject();
	}

	@Override
	public void putWeightsAndBiasesToDatabase(Matrix[] weights, Matrix[] biases) {
		for (Matrix layer : weights) {
			double[] weightsOfLayer = Matrix.matrixToArray(layer);
		}
	}
	
	public void putBiasesToDatabase(Matrix[] biasesAsMatrix) {
		ArrayList<double[]> biasesAsArrayList = new ArrayList<>();
		for (Matrix layerAsMatrix : biasesAsMatrix) {
			double[] layerAsDoubles = Matrix.matrixToArray(layerAsMatrix);
			biasesAsArrayList.add(layerAsDoubles);
		}
		nodeAndSynapseAccessObject.createAllNodes(biasesAsArrayList);		
	}
	
	public void putWeightsToDatabase(Matrix[] weightsAsMatrix) {
		ArrayList<double[]> weightsAsArrayList = new ArrayList<>();
		for (Matrix synapseAsMatrix : weightsAsMatrix) {
			double[] synapseAsDoubles = Matrix.matrixToArray(synapseAsMatrix);
			weightsAsArrayList.add(synapseAsDoubles);
		}
		nodeAndSynapseAccessObject.createAllNodes(weightsAsArrayList);
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
//		int amountOfLayers = 2; 
//		// int amountOfLayers = nodeAndSynapseObject.getAmountOfLayers();
//		Matrix[] biasesAsMatrix = new Matrix[amountOfLayers - 1];
//		
//		for (int i = 0; i < amountOfLayers - 1; i++) {
//			double[] biases = new double[2]; 
//			// double[] biases = nodeAndSynapseObject.getBiasesOfLayer(i)
//			biasesAsMatrix[i] = Matrix.arrayToMatrix(biases);
//		}
//		return biasesAsMatrix;
		nodeAndSynapseAccessObject.getAllBiasesfromDB();
		return null;
	}

}
