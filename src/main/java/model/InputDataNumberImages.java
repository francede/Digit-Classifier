package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class InputDataNumberImages implements InputData{

	private final double[] input;
	private double[] target;
	private String label;
	private static int pngCount = 0;

	public InputDataNumberImages(double[] inputAsPixels, int label) {
		this.label = Integer.toString(label);
		this.input = new double[inputAsPixels.length];
		for(int i = 0; i < inputAsPixels.length; i++){
			this.input[i] = inputAsPixels[i] / 255.0;
		}
		this.target = new double[10];
		this.target[label] = 1.0;
//		drawInputDataAsPng();
	}
	
	public InputDataNumberImages(double[] inputAsPixels) {
		this.target = null;
		this.label = null;
		this.input = new double[inputAsPixels.length];
		for(int i = 0; i < inputAsPixels.length; i++){
			this.input[i] = inputAsPixels[i] / 255.0;
		}
//		drawInputDataAsPng();
	}

	private void drawInputDataAsPng() {
		int numberOfColumns = 28;
		int numberOfRows = 28;
		String outputPath = "data/train-images/";

		try {

			BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_ARGB);
			int numberOfPixels = numberOfRows * numberOfColumns;
			int[] imgPixels = new int[numberOfPixels];
			System.out.println("Number of pixels: " + numberOfPixels);
			System.out.println("NumberofRows: " + numberOfRows);
			System.out.println("numberOfColumns: " + numberOfColumns);

			



				for (int p = 0; p < numberOfPixels; p++) {
					int gray = (int)(255 * this.input[p]);
					imgPixels[p] = 0xFF000000 | (gray << 16) | (gray << 8) | gray;
				}

				image.setRGB(0, 0, numberOfColumns, numberOfRows, imgPixels, 0, numberOfColumns);

				File outputfile = new File(outputPath + pngCount + ".png");
				pngCount++;

				ImageIO.write(image, "png", outputfile);
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	public double[] getInput() {return this.input;}

	public double[] getTarget() {return this.target;}

	public String getLabel() {return this.label;}

	public void setLabelAndTarget(String label){
		int index;
		try{
			index = Integer.parseInt(label);
			target = new double[10];
			target[index] = 1.0;
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
		}
	}

}
