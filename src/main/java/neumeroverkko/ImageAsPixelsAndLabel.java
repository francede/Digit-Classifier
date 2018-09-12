package neumeroverkko;

public class ImageAsPixelsAndLabel {

	private int[] pixels;
	private int label;

	public ImageAsPixelsAndLabel(int[] pixels, int label) {
		this.pixels = pixels;
		this.label = label;
	}

	public int[] getPixels() {
		return pixels;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

}
