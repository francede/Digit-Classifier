package neumeroverkko;

import neumeroverkko.Matrix.MapOperation;

public class NeuralNetworkImpl {
	private Matrix[] weights;
	private Matrix[] biases;

	private double 	learningRate = 0.1;
	private int 	learningIterationSize = 10;
	private int 	learningIterations = 40;
	private Matrix[] layerOutputs;

	private MapOperation activationFunction = (x) ->
		1 / (1+Math.pow(Math.E,-x));
	private MapOperation activationFunctionDerivative = (x) ->
		1 / (1+Math.pow(Math.E,-x)) * (1- (1/(1+Math.pow(Math.E,-x))) );

	/**
	 * Creates a neural network with all weights and biases set to 0
	 * @param layerSizes: The sizes of all nodes in the network including the input "layer"
	 */
	public NeuralNetworkImpl(int[] layerSizes){
		this.layerOutputs = new Matrix[layerSizes.length-1];
		this.weights = new Matrix[layerSizes.length-1];
		this.biases = new Matrix[layerSizes.length-1];
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
			output.map(activationFunction);
			layerOutputs[i] = Matrix.clone(output);
		}
		System.out.println(output);
		return output;
	}

	/**
	 * Returns a change Matrix for one training set
	 * @param inputs
	 * @param label
	 * @return
	 */
	public Matrix[] createGradients(Matrix inputs, int label){
		this.feedForward(inputs);

		//Error matrix calculated from outputs and target
		Matrix error = new Matrix(biases[biases.length-1].getRows(), 1);
		error.setValue(label, 0, 1);
		error.sub(layerOutputs[layerOutputs.length-1]);

		//All changes to be made according to this single training iteration
		Matrix[] gradients = new Matrix[weights.length];

		//Calculate gradient and deltas for the weights
		for(int out = 0; out < weights.length; out++){
			Matrix gradient = Matrix.clone(layerOutputs[layerOutputs.length-out-1]);
			gradient.map(activationFunctionDerivative);
			gradient.multiply(error);
			gradient.multiply(learningRate);
			gradients[layerOutputs.length-out-1] = gradient;
		}
		return gradients;
	}

	public void trainIteration(Matrix[] inputs, int[] labels){
		Matrix[] gradientAverage = new Matrix[weights.length];
		Matrix[] outputAverage = new Matrix[weights.length];

		for(int input = 0; input < inputs.length; input++){
			Matrix gradients[] = this.createGradients(inputs[input], labels[input]);

			for(int layer = 0; layer < gradients.length; layer++){
				if(outputAverage[layer] == null){
					outputAverage[layer] = Matrix.clone(layerOutputs[layer]);
				}else{
					outputAverage[layer].add(layerOutputs[layer]);
				}
				if(gradientAverage[layer] == null){
					gradientAverage[layer] = Matrix.clone(gradients[layer]);
				}else{
					gradientAverage[layer].add(gradients[layer]);
				}
			}
		}

		//Average of gradient across one training iteration
		for(Matrix layer : gradientAverage) layer.divide(inputs.length);

		//Apply gradient to weights and biases
		for(int layer = 0; layer < weights.length; layer++){
			Matrix layerOutputAverage_T = Matrix.transpose(outputAverage[layer]);
			Matrix deltaWeights = Matrix.dotProduct(gradientAverage[layer], layerOutputAverage_T);

			weights[layer].add(deltaWeights);
			biases[layer].add(gradientAverage[layer]);
		}
	}
}
