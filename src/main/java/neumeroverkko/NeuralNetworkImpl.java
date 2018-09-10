package neumeroverkko;

import neumeroverkko.Matrix.MapOperation;

public class NeuralNetworkImpl{
	private Matrix[] weights;
	private Matrix[] biases;
	private int[] layerSizes;
	//private MapOperation activationFunction = (x) -> {};

	public NeuralNetworkImpl(int[] layerSizes){
		this.layerSizes = layerSizes.clone();
		for(int i = 0; i < layerSizes.length-1; i++){
			weights[i] = new Matrix(layerSizes[i], layerSizes[i+1]);
			biases[i] = new Matrix(layerSizes[i+1], 1);
		}


	}

}
