package neumeroverkko;

import java.util.Random;

public class DevTestClass {
	public static void main(String[] args){
		testMatrixDimensions();
	}

	public static void testNN(){
		NeuralNetworkImpl nn = new NeuralNetworkImpl(new int[]{2,2,1});
		nn.reset();
		nn.setLearningRate(0.2);
		Matrix inTT = Matrix.arrayToMatrix(new double[]{1,1});
		Matrix inTF = Matrix.arrayToMatrix(new double[]{1,0});
		Matrix inFT = Matrix.arrayToMatrix(new double[]{0,1});
		Matrix inFF = Matrix.arrayToMatrix(new double[]{0,0});

		Matrix labelTrue = Matrix.arrayToMatrix(new double[]{1});
		Matrix labelFalse = Matrix.arrayToMatrix(new double[]{0});

		for(int i = 0; i < 100000; i++){
			int r = new Random().nextInt(4);
			switch(r){
			case 0:
				nn.trainOnce(inTT, labelFalse);
				break;
			case 1:
				nn.trainOnce(inTF, labelTrue);
				break;
			case 2:
				nn.trainOnce(inFT, labelTrue);
				break;
			case 3:
				nn.trainOnce(inFF, labelFalse);
				break;
			}
		}
		System.out.println("TT");
		nn.trainOnce(inTT, labelFalse);
		System.out.println("TF");
		nn.trainOnce(inTF, labelTrue);
		System.out.println("FT");
		nn.trainOnce(inFT, labelTrue);
		System.out.println("FF");
		nn.trainOnce(inFF, labelFalse);
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
}
