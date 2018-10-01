package _deprecated;

import java.util.ArrayList;
import java.util.Random;

import model.InputData;
import model.InputDataBoolean;
import model.Matrix;
import model.NeuralNetworkImpl;

public class DevTestClass {
	public static void main(String[] args){
		testMatrixToArray();
	}

	public static void testMatrixToArray(){
		double[][] a = {{1,2,3}, {4,5,6}};
		Matrix m = Matrix.arrayToMatrix(a);
		System.out.println(m);
		double[] out = Matrix.matrixToArray(m);
		for(double o : out){
			System.out.println(o);
		}
	}

	public static void testNN(){
		NeuralNetworkImpl nn = new NeuralNetworkImpl(new int[]{2,2,1});
		nn.reset();
		nn.setLearningRate(0.1);
		ArrayList<InputData> trainingData = new ArrayList<InputData>();
		trainingData.add(new InputDataBoolean(new double[]{1,1},false));
		trainingData.add(new InputDataBoolean(new double[]{1,0},true));
		trainingData.add(new InputDataBoolean(new double[]{0,1},true));
		trainingData.add(new InputDataBoolean(new double[]{0,0},false));

		for(int i = 0; i < 100000; i++){
			int r = new Random().nextInt(4);
			switch(r){
			case 0:
				nn.train(trainingData.get(r));
				break;
			case 1:
				nn.train(trainingData.get(r));
				break;
			case 2:
				nn.train(trainingData.get(r));
				break;
			case 3:
				nn.train(trainingData.get(r));
				break;
			}
		}
		System.out.println("TT");
		nn.train(trainingData.get(0));
		System.out.println("TF");
		nn.train(trainingData.get(1));
		System.out.println("FT");
		nn.train(trainingData.get(2));
		System.out.println("FF");
		nn.train(trainingData.get(3));
	}

	public static void testNNLarger(){
		NeuralNetworkImpl nn = new NeuralNetworkImpl(new int[]{2,2,1});
		nn.reset();
		nn.setLearningRate(0.15);
		ArrayList<InputData> trainingData = new ArrayList<InputData>();
		trainingData.add(new InputDataBoolean(new double[]{1,1},false));
		trainingData.add(new InputDataBoolean(new double[]{1,0},true));
		trainingData.add(new InputDataBoolean(new double[]{0,1},true));
		trainingData.add(new InputDataBoolean(new double[]{0,0},false));

		for(int i = 0; i < 100000; i++){
			ArrayList<InputData> dataSet = new ArrayList<InputData>();
			for(int j = 0; j < 20; j++){
				int r = new Random().nextInt(4);
				dataSet.add(trainingData.get(r));
			}
			nn.trainWithaTrainingSet(dataSet);
		}
		System.out.println("TT");
		System.out.println(nn.makePrediction(trainingData.get(0)));
		System.out.println("TF");
		System.out.println(nn.makePrediction(trainingData.get(1)));
		System.out.println("FT");
		System.out.println(nn.makePrediction(trainingData.get(2)));
		System.out.println("FF");
		System.out.println(nn.makePrediction(trainingData.get(3)));
	}

	public static void testMatrixDimensions(){
		Matrix m1 = new Matrix(5,1);
		double[] d1 = new double[m1.getRows()];
		for(int i = 0; i < d1.length; i++){
			d1[i] = m1.getData()[i][0];
		}
		System.out.println(m1);
		System.out.println(d1[2]);
	}

	public static void testDimensions(){
		NeuralNetworkImpl nn = new NeuralNetworkImpl(new int[]{2,6,1});
		Matrix[] ws = nn.getWeights();
		for(Matrix w : ws){
			System.out.println(w);
		}
	}
}
