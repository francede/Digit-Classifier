package model;

import java.util.ArrayList;

import model.Matrix.MapOperation;

public class NeuralNetworkImpl implements NeuralNetwork{
	private Matrix[] weights;
	private Matrix[] biases;
	private Matrix[] layerOutputs;
	private Matrix[] layerWeightedSums;
	private int actualLayers;
	private double 	learningRate = 0.1;

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

//SETTERS & GETTERS --------------------------------------------------------------------------------------

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
	 * Sets the weights of the network
	 */
	public void setWeights(Matrix[] weights){
		if(this.weights.length != weights.length) throw new MatrixException("weights[] length must match.");
		for(int i = 0; i < this.weights.length; i++){
			if(weights[i].getCols() == this.weights[i].getCols() && weights[i].getRows() == this.weights[i].getRows()){
				this.weights[i] = Matrix.clone(weights[i]);
			}else{
				throw new MatrixException("Weights' (index: " + i + ") matrices' dimensions must match.");
			}
		}
	}

	/**
	 * Sets the biases of the network
	 */
	public void setBiases(Matrix[] biases){
		if(this.biases.length != biases.length) throw new MatrixException("biases[] length must match.");
		for(int i = 0; i < this.biases.length; i++){
			if(biases[i].getCols() == this.biases[i].getCols() && biases[i].getRows() == this.biases[i].getRows()){
				this.biases[i] = Matrix.clone(biases[i]);
			}else{
				throw new MatrixException("Biases' (index: " + i + ") matrices' dimensions must match.");
			}
		}
	}

	/**
	 * Gets the weights of the network
	 */
	public Matrix[] getWeights() {
		Matrix[] weightsClone = new Matrix[weights.length];
		for(int i = 0; i < weightsClone.length; i++){
			weightsClone[i] = Matrix.clone(weights[i]);
		}
		return weightsClone;
	}

	/**
	 * Gets the biases of the network
	 */
	public Matrix[] getBiases() {
		Matrix[] biasesClone = new Matrix[biases.length];
		for(int i = 0; i < biasesClone.length; i++){
			biasesClone[i] = Matrix.clone(biases[i]);
		}
		return biasesClone;
	}

	public void reset(){
		this.reset(0,1);
	}

	/**
	 * Randomizes this neural network's weights and biases
	 */
	public void reset(double min, double max){
		for(Matrix w : weights)	w.randomize(min, max);
		for(Matrix b : biases )	b.randomize(min, max);
	}

	/**
	 * Creates an output using the feed-forward algorithm
	 * @param inputs as a vector matrix
	 * @return Returns the outputs as a vector matrix
	 * @throws MatrixException
	 */
	private Matrix feedForward(Matrix inputs){
		Matrix output = Matrix.clone(inputs);
		for(int i = 0; i < weights.length; i++){
			output = Matrix.dotProduct(weights[i], output);
			output.add(biases[i]);
			layerWeightedSums[i] = Matrix.clone(output);
			output.map(activationFunction);
			layerOutputs[i] = Matrix.clone(output);
		}
		return output;
	}

	/**
	 * Uses the backpropagation algorithm to calculate weights' and biases' changes
	 * @param inputs
	 * @param outputs
	 * @param target
	 * @return return[0] is weight deltas as a table of matrices, return[1] is gradients
	 * @throws MatrixException
	 */
	private Matrix[][] backpropagate(Matrix inputs, Matrix outputs, Matrix target){
		Matrix[] errors = createErrors(target, Matrix.clone(outputs), this.weights);
		Matrix[] gradients = createGradients(errors);
		Matrix[] weightDeltas = createDeltas(gradients, inputs);
		//bias deltas are the gradients
		return new Matrix[][]{weightDeltas, gradients};
	}

	private Matrix[] createErrors(Matrix target, Matrix output, Matrix[] weights){
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

	private Matrix[] createGradients(Matrix[] errors){
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

	private Matrix[] createDeltas(Matrix[] gradients, Matrix inputs){
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

	/**
	 * Uses the feedforward algorithm to generate a guess based on the current weights and biases and the input
	 */
	@Override
	public Matrix makePrediction(InputData input){

		Matrix image = Matrix.arrayToMatrix(input.getInput());
		Matrix output = feedForward(image);

		return output;
	}

	/**
	 * Trains the network once based on the training data
	 */
	public void train(InputData trainingData) {
		Matrix input = Matrix.arrayToMatrix(trainingData.getInput());
		Matrix target = Matrix.arrayToMatrix(trainingData.getTarget());
		Matrix output = this.feedForward(input);

		Matrix[][] changes = this.backpropagate(input, output, target);
		Matrix[] weightDeltas = changes[0];
		Matrix[] biasDeltas = changes[1];

		for(int layer = 0; layer < actualLayers; layer++){
			weights[layer].add(weightDeltas[layer]);
			biases[layer].add(biasDeltas[layer]);
		}
	}

	/**
	 * Trains the network data with the average of the gradients generated from the individual training datas in trainingSet
	 */
	public void trainWithaTrainingSet(ArrayList<InputData> trainingSet){
		Matrix[] averageWeightDeltas = new Matrix[actualLayers];
		Matrix[] averageBiasDeltas = new Matrix[actualLayers];

		for(InputData data : trainingSet){
			Matrix input = Matrix.arrayToMatrix(data.getInput());
			Matrix target = Matrix.arrayToMatrix(data.getTarget());
			Matrix output = this.feedForward(input);
			Matrix[][] deltas = this.backpropagate(input, output, target);
			Matrix[] weightDeltas = deltas[0];
			Matrix[] biasDeltas = deltas[1];
			for(int layer = 0; layer < actualLayers; layer++){
				if(averageWeightDeltas[layer] == null){
					averageWeightDeltas[layer] = Matrix.clone(weightDeltas[layer]);
					averageBiasDeltas[layer] = Matrix.clone(biasDeltas[layer]);
				}else{
					averageWeightDeltas[layer].add(weightDeltas[layer]);
					averageBiasDeltas[layer].add(biasDeltas[layer]);
				}
			}
		}

		//Take average
		for(int layer = 0; layer < actualLayers; layer++){
			averageWeightDeltas[layer].divide(trainingSet.size());
			averageBiasDeltas[layer].divide(trainingSet.size());
		}

		//Add average to weights and biases
		for(int layer = 0; layer < actualLayers; layer++){
			weights[layer].add(averageWeightDeltas[layer]);
			biases[layer].add(averageBiasDeltas[layer]);
		}
	}
}
