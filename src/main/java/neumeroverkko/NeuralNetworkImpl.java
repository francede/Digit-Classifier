package neumeroverkko;

import neumeroverkko.Matrix.MapOperation;

public class NeuralNetworkImpl{
	private Matrix[] weights;
	private Matrix[] biases;
	private Matrix[] layerOutputs;
	private Matrix[] layerWeightedSums;

	private int actualLayers;
	private double 	learningRate = 0.1;
//	private int 	learningIterationSize = 10;
//	private int 	learningIterations = 40;

	private MapOperation activationFunction = (x) ->
		1 / (1+Math.pow(Math.E,-x));
	private MapOperation activationFunctionDerivative = (x) ->
		1 / (1+Math.pow(Math.E,-x)) * (1- (1/(1+Math.pow(Math.E,-x))) );

	/**
	 * Creates a neural network with all weights and biases set to 0
	 * @param layerSizes: The sizes of all nodes in the network including the input "layer"
	 */
	public NeuralNetworkImpl(int[] layerSizes){
		this.actualLayers = layerSizes.length - 1;
		this.layerOutputs = new Matrix[actualLayers];
		this.layerWeightedSums = new Matrix[actualLayers];

		this.weights = new Matrix[actualLayers];
		this.biases = new Matrix[actualLayers];

		for(int i = 0; i < layerSizes.length-1; i++){
			weights[i] = new Matrix(layerSizes[i+1], layerSizes[i]);
			biases[i] = new Matrix(layerSizes[i+1], 1);
		}
	}

	/**
	 * Sets the activation function used by this neural network
	 * @param newFunction: The lambda expression to be used as the activation function
	 * @param newFunctionDerivative: The lambda expression of the derivative of <code>newFunction</code>
	 */
	public void setActivationFunction(MapOperation newFunction, MapOperation newFunctionDerivative){
		this.activationFunction = newFunction;
		this.activationFunctionDerivative = newFunctionDerivative;
	}

	/**
	 * Sets a new learning rate for the neural network
	 * @param newRate
	 */
	public void setLearningRate(double newRate){
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
			layerWeightedSums[i] = Matrix.clone(output);
			output.map(activationFunction);
			layerOutputs[i] = Matrix.clone(output);
		}
		System.out.println(output);
		return output;
	}

	public void trainOnce(Matrix inputs, Matrix target){
		Matrix targetClone = Matrix.clone(target);
		Matrix output = this.feedForward(inputs);
		Matrix[][] deltas = createChanges(inputs, output, targetClone);
		Matrix[] weightDeltas = deltas[0];
		Matrix[] biasDeltas = deltas[1];
		for(int layer = 0; layer < actualLayers; layer++){
//			System.out.println("layer " + layer);
//			System.out.println(weights[layer]);
//			System.out.println(weightDeltas[layer]);
			weights[layer].add(weightDeltas[layer]);
//			System.out.println(weights[layer]);
			biases[layer].add(biasDeltas[layer]);
		}
	}

	public Matrix[][] createChanges(Matrix inputs, Matrix outputs, Matrix target){
		Matrix[] errors = createErrors(target, outputs, this.weights);
		Matrix[] gradients = createGradients(errors);
		Matrix[] weightDeltas = createDeltas(gradients, inputs);
		return new Matrix[][]{weightDeltas, gradients};
	}

	public Matrix[] createErrors(Matrix target, Matrix output, Matrix[] weights){
		Matrix[] errors = new Matrix[actualLayers];

		//Error from output is T - O(last)
		//Error for layer n is Wt(n+1) .p E(n+1)
		//Where T is target, O is layer output, Wt is transposed weights
		errors[errors.length-1] = Matrix.operate(target, output, "SUB");
		for(int n = errors.length-2; n >= 0; n--){
			Matrix Wt = Matrix.transpose(weights[n+1]);
			errors[n] = Matrix.dotProduct(Wt, errors[n+1]);
		}
		return errors;
	}

	public Matrix[] createGradients(Matrix[] errors){
		Matrix[] gradients = new Matrix[actualLayers];

		//Gradient for layer n is da(S(n)) * E(n) * LR
		//Where da is derivateive of af and S is weighted sum before af
		for(int n = 0; n < gradients.length; n++){
			Matrix S = layerWeightedSums[n];
			Matrix E = errors[n];
			S.map(activationFunctionDerivative);
			gradients[n] = Matrix.operate(S, E, "MULTIPLY");
			gradients[n].multiply(learningRate);
		}
		return gradients;
	}

	public Matrix[] createDeltas(Matrix[] gradients, Matrix inputs){
		Matrix[] deltaWeights = new Matrix[actualLayers];

		//First weights' deltas(Wd) is G(1) .p It
		//Weights' deltas at layer n is G(n) .p Ot(n-1)
		//Where G is gradient, It is transposed inputs and Ot is layer's outputs(after af) transposed
		deltaWeights[0] = Matrix.dotProduct(gradients[0], Matrix.transpose(inputs));
		for(int n = 1; n < deltaWeights.length; n++){
			Matrix Ot = Matrix.transpose(layerOutputs[n-1]);
			deltaWeights[n] = Matrix.dotProduct(gradients[n], Ot);
		}

		return deltaWeights;
	}

}
