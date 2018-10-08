package orm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

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
		nodeAndSynapseAccessObject.createAllSynapses(weightsAsArrayList);
	}


	@Override
	public Matrix[] getWeightsFromDatabase() {
		ArrayList<double[]> weightsArrayList= nodeAndSynapseAccessObject.getAllWeightsfromBD();
		Matrix[] weightsMatrixArray = new Matrix[weightsArrayList.size()];
		for (int i = 0; i < weightsArrayList.size(); i++) {
			Matrix weightsMatrix = Matrix.arrayToMatrix(weightsArrayList.get(i));
			weightsMatrixArray[i] = weightsMatrix;
		}
		return weightsMatrixArray;
	}

	@Override
	public Matrix[] getBiasesFromDatabase() {
		ArrayList<double[]> biasesArrayList= nodeAndSynapseAccessObject.getAllBiasesfromDB();
		Matrix[] biasesMatrixArray = new Matrix[biasesArrayList.size()];
		for (int i = 0; i < biasesArrayList.size(); i++) {
			Matrix biasesMatrix = Matrix.arrayToMatrix(biasesArrayList.get(i));
			biasesMatrixArray[i] = biasesMatrix;
		}
		return biasesMatrixArray;
	}

}
