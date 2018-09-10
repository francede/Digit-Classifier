package neumeroverkko;

public class ImageAsPixels {
	
	private int[] pixels;
	private int label;
	
	public ImageAsPixels(int[] pixels, int label) {
		this.pixels = pixels;
		this.label = label;
	}

	public int[] getPixels() {
		return pixels;
	}

	public int getLabel() {
		return label;
	}

}
