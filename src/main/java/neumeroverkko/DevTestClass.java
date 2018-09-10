package neumeroverkko;

public class DevTestClass {
	public static void main(String[] args){
		NeuralNetworkImpl nn = new NeuralNetworkImpl(new int[]{2,2});
		//nn.randomizeAll();
		Matrix in = Matrix.arrayToMatrix(new float[]{1,2});
		Matrix out = nn.feedForward(in);
		System.out.println(in);
		System.out.println(out);
	}
}
