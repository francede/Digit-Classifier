package _deprecated;

import java.util.ArrayList;
import java.util.Random;

import model.InputData;
import model.InputDataBoolean;
import model.Matrix;
import model.NeuralNetworkImpl;

public class DevTestClass {
	public static void main(String[] args){
		testNNLarger();
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

	public static double[] testNN(double min, double max, double lr, int iterations){
		NeuralNetworkImpl nn = new NeuralNetworkImpl(new int[]{2,2,1});
		nn.reset(min, max);
		nn.setLearningRate(lr);
		ArrayList<InputData> trainingData = new ArrayList<InputData>();
		trainingData.add(new InputDataBoolean(new double[]{1,1},false));
		trainingData.add(new InputDataBoolean(new double[]{1,0},true));
		trainingData.add(new InputDataBoolean(new double[]{0,1},true));
		trainingData.add(new InputDataBoolean(new double[]{0,0},false));

		for(int i = 0; i < iterations; i++){
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
		double[] predictions = new double[trainingData.size()];
		for(int i = 0; i < trainingData.size(); i++){
			predictions[i] = nn.makePrediction(trainingData.get(i)).getData()[0][0];
		}
		return predictions;
	}

	public static void testNNLarger(){
		System.out.println("#\tmin\tmax\tLR\titers\tTT\tTF\tFT\tFF");
		System.out.println("--------------------------------------------------------------------");
		String best = null;
		double bestError = -1;
		String worst = null;
		double worstError = -1;
		double error = 0;
		int testNum = 1;
		double[] mins = {-2,-1,0};
		double[] maxs = {0,1,2};
		double[] lrs = {0.2,0.5,0.75};
		int[] iterations = {30000};
		double[] targets = {0,1,1,0};
 		for(double min : mins){
 			for(double max : maxs){
 				if(min == max) continue;
 				for(double lr : lrs){
 					for(int itr : iterations){
 						double[] averages = new double[4];
 						int tests = 25;
 						for(int i = 0; i < tests; i++){
 							double[] outs = testNN(min,max,lr,itr);


 							for(int out = 0; out < outs.length; out++){
 								averages[out] += outs[out];
 							}
 						}
 						String output = "";
 						error = 0;
 						for(int avrg = 0; avrg < averages.length; avrg++){
 							averages[avrg] /= tests;
 							output = output + String.format("%.2f", averages[avrg]) + "\t";
 							error += Math.abs((targets[avrg] - averages[avrg]));
 						}
 						error /= 4;

 						System.out.println(testNum + "\t" + min+"\t"+max+"\t"+lr+"\t"+itr+"\t"+output);
 						if(bestError == -1){
 							bestError = error;
 							best = testNum + "\t" + min+"\t"+max+"\t"+lr+"\t"+itr+"\t"+output;
 						}else if(error < bestError){
 							bestError = error;
 							best = testNum + "\t" + min+"\t"+max+"\t"+lr+"\t"+itr+"\t"+output;
 						}
 						if(worstError == -1){
 							worstError = error;
 							worst = testNum + "\t" + min+"\t"+max+"\t"+lr+"\t"+itr+"\t"+output;
 						}else if(error > worstError){
 							worstError = error;
 							worst = testNum + "\t" + min+"\t"+max+"\t"+lr+"\t"+itr+"\t"+output;
 						}
 						testNum++;
 					}
 				}
 			}
 		}
 		System.out.println("--------------------------------------------------------------------");
 		System.out.println("Done");
 		System.out.println("Best result(Error = " +String.format("%.2f", bestError)+ "):");
 		System.out.println(best);
 		System.out.println("Worst result(Error = " + String.format("%.2f", worstError)+ "):");
 		System.out.println(worst);

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
