package neumeroverkko;

public class ImageAsPixelsAndLabel {

	private final int[] PIXELS;
	private final int LABEL;

	public ImageAsPixelsAndLabel(int[] pixels, int label) {
		this.PIXELS = pixels;
		this.LABEL = label;
	}

	public int[] getPixels() {
		return PIXELS;
	}

	public int getLabel() {
		return LABEL;
	}

}
