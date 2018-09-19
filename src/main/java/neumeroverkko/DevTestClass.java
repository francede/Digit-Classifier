package neumeroverkko;

import java.util.Random;

public class DevTestClass {
	public static void main(String[] args){
		NeuralNetworkImpl nn = new NeuralNetworkImpl(new int[]{2,2,1});
		nn.randomizeAll();
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
				System.out.println("TT");
				nn.trainOnce(inTT, labelFalse);
				break;
			case 1:
				System.out.println("TF");
				nn.trainOnce(inTF, labelTrue);
				break;
			case 2:
				System.out.println("FT");
				nn.trainOnce(inFT, labelTrue);
				break;
			case 3:
				System.out.println("FF");
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
}
