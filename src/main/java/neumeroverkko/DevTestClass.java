package neumeroverkko;

public class DevTestClass {
	public static void main(String[] args){
		NeuralNetworkImpl nn = new NeuralNetworkImpl(new int[]{5,10,10});
		nn.randomizeAll();
		Matrix[] in = new Matrix[]{Matrix.arrayToMatrix(new double[]{0,1,0,0,1})};
		int[] labels = {2};
		for(int i = 0; i < 5000; i++){
			nn.trainIteration(in, labels);
		}

		//System.out.println(in);
		//System.out.println(out);
	}
}
