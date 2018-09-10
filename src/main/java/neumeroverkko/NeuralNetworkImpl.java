package neumeroverkko;

import neumeroverkko.Matrix.MapOperation;

public class NeuralNetworkImpl{
	private Matrix[] weights;
	private Matrix[] biases;
	//private int[] layerSizes;
	private float learningRate = 0.1f;
	private MapOperation activationFunction = (x) -> {float y = (float)(1/(1+Math.pow(Math.E,-x)));return y;};

	/**
	 * Creates a neural network with all weights and biases set to 0
	 * @param layerSizes: The sizes of all nodes in the network including the input "layer"
	 */
	public NeuralNetworkImpl(int[] layerSizes){
		//this.layerSizes = layerSizes.clone();
		this.weights = new Matrix[layerSizes.length-1];
		this.biases = new Matrix[layerSizes.length-1];
		for(int i = 0; i < layerSizes.length-1; i++){
			weights[i] = new Matrix(layerSizes[i+1], layerSizes[i]);
			biases[i] = new Matrix(layerSizes[i+1], 1);
		}
	}

	/**
	 * Sets the activation function used by this neural network
	 * @param newFunction: A lambda expression to be used as the activation function
	 */
	public void setActivationFunction(MapOperation newFunction){
		this.activationFunction = newFunction;
	}

	/**
	 * Sets a new learning rate for the neural network
	 * @param newRate
	 */
	public void setLearningRate(float newRate){
		this.learningRate = newRate;
	}

	/**
	 * Randomizes this neural network's weights and biases
	 */
	public void randomizeAll(){
		for(Matrix w : weights)	w.randomize();
		for(Matrix b : biases )	b.randomize();
	}

	/**
	 * Creates an output using the feed-forward algorithm
	 * @param inputs as a vector matrix
	 * @return Returns the outputs as a vector matrix
	 */
	public Matrix feedForward(Matrix inputs){
		Matrix output = Matrix.clone(inputs);
		for(int i = 0; i < weights.length; i++){
			output = Matrix.dotProduct(weights[i], output);
			output.add(biases[i]);
			output.map(activationFunction);
		}
		return output;
	}
}
