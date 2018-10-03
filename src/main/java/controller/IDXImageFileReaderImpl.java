package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import model.InputData;
import model.InputDataNumberImages;

/**
* This piece of code is based on a IDX file reader presented in Stackoverflow
* by RayDeeA.
* https://stackoverflow.com/questions/17279049/reading-a-idx-file-type-in-java
*/

/**
 * @author Antti Nieminen
 */
public class IDXImageFileReaderImpl implements IDXImageFileReader {

	private FileInputStream inImage = null;
	private FileInputStream inLabel = null;

	private String inputImagePath = "data/train-images/train-images.idx3-ubyte";
	private String inputLabelPath = "data/train-images/train-labels.idx1-ubyte";
	private String outputPNGPath = "data/train-images/";

	private int numberOfImages;
	private int numberOfRows;
	private int numberOfColumns;

	private int numberOfImagesRead;

	public IDXImageFileReaderImpl() {
		openFileStreams();
		numberOfImagesRead = 0;
	}

	private void openFileStreams() {
		try {
			inImage = new FileInputStream(inputImagePath);
			inLabel = new FileInputStream(inputLabelPath);

			// Although magicNumberImages isn't used in this class, it must be read from the
			// bit stream.
			int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());
			numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());

			// Although magicNumberLabels or numberOfLabels aren't used in this class, they
			// must be read from the
			// bit stream.
			int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8)
					| (inLabel.read());
			int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8)
					| (inLabel.read());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public InputData getSingleImageAsPixels() {
		InputData singleImageAsPixels = null;
		singleImageAsPixels = readImageFiles();
		return singleImageAsPixels;
	}

	private InputData readImageFiles() {
		if (numberOfImagesRead >= numberOfImages) {
			resetStream();
		}
		numberOfImagesRead++;

		InputData imageAsPixelsAndLabel = null;
		int numberOfPixels = numberOfRows * numberOfColumns;
		double[] pixelsOfImage = new double[numberOfPixels];
		

		try {
			

				
			
			// Read the pixels of the image
			for (int p = 0; p < numberOfPixels; p++) {
				int pixelValue = inImage.read();
				pixelsOfImage[p] = pixelValue;
			}

			// Read the label of the number
			int labelValue = inLabel.read();
			
			// Assign the pixels and label to a new object
			imageAsPixelsAndLabel = new InputDataNumberImages(pixelsOfImage, labelValue);
			

		} catch (IOException e) {
			e.printStackTrace();
		}

		return imageAsPixelsAndLabel;

	}

	private void resetStream() {
		closeFileStreams();
		openFileStreams();
		this.numberOfImagesRead = 0;
	}

	private void closeFileStreams() {
		if (inImage != null) {
			try {
				inImage.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (inLabel != null) {
			try {
				inLabel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ArrayList<InputData> getMultipleImagesAsPixels(int amountOfImages) {
		ArrayList<InputData> multipleImagesAsPixels = new ArrayList<InputData>();
		InputData singleImageAsPixels = null;
		for (int i = 1; i <= amountOfImages; i++) {
			singleImageAsPixels = readImageFiles();
			multipleImagesAsPixels.add(singleImageAsPixels);
		}
//		System.out.println("IDXImageFileReaderImpl: Number of images processed: " + amountOfImages);
		return multipleImagesAsPixels;
	}

	@Override
	public ArrayList<InputData> getAllImagesAsPixels() {
		ArrayList<InputData> allImagesAsPixels = new ArrayList<InputData>();
		allImagesAsPixels = getMultipleImagesAsPixels(60000);
		return allImagesAsPixels;
	}

	@Override
	public void createPNGFiles(int amount) {

		String inputImagePath = "data/train-images/train-images.idx3-ubyte";
		String inputLabelPath = "data/train-images/train-labels.idx1-ubyte";
		String outputPath = "data/train-images/";

		int[] hashMap = new int[10];

		try {

			BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_ARGB);
			int numberOfPixels = numberOfRows * numberOfColumns;
			int[] imgPixels = new int[numberOfPixels];
			System.out.println("Number of pixels: " + numberOfPixels);
			System.out.println("NumberofRows: " + numberOfRows);
			System.out.println("numberOfColumns: " + numberOfColumns);

			for (int i = 0; i < amount; i++) {

				if (i % 100 == 0) {
					System.out.println("Number of images extracted: " + i);
				}

				for (int p = 0; p < numberOfPixels; p++) {
					int gray = 255 - inImage.read();
					imgPixels[p] = 0xFF000000 | (gray << 16) | (gray << 8) | gray;
				}

				image.setRGB(0, 0, numberOfColumns, numberOfRows, imgPixels, 0, numberOfColumns);

				int label = inLabel.read();

				hashMap[label]++;
				
				if (label == 5) {
					File outputfile = new File(outputPath + label + "_0" + hashMap[label] + ".png");

					System.out.println("Created file " + outputPath + label + "_0" + hashMap[label] + ".png");

					ImageIO.write(image, "png", outputfile);
				}
				
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public InputData getTheFirstNumberFromTrainingFile() {
		resetStream();
		return readImageFiles();
	}
	
	public ArrayList<InputData> getTheFirstXAmountOfNumbersFromTrainingFile(int amount) {
		resetStream();
		return getMultipleImagesAsPixels(amount);
	}

}
