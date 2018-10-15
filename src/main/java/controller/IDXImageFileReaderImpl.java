package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

	private String trainsetImagePath = "data/train-images/train-images.idx3-ubyte";
	private String trainsetLabelPath = "data/train-images/train-labels.idx1-ubyte";
	private String testsetImagePath = "data/train-images/testset-images.idx3-ubyte";
	private String testsetLabelPath = "data/train-images/testset-labels.idx1-ubyte";
	private String outputPNGPath = "data/train-images/";

	private int numberOfImages;
	private int numberOfRows;
	private int numberOfColumns;

	private int numberOfImagesRead;

	public IDXImageFileReaderImpl() {
		openFileStreams();
		numberOfImagesRead = 0;
	}

	@SuppressWarnings("unused")
	private void openFileStreams() {
		try {
			inImage = new FileInputStream(trainsetImagePath);
			inLabel = new FileInputStream(trainsetLabelPath);

			/*
			 * Although magicNumberImages isn't used in this class, it must be read from the
			 * bit stream.
			 */
			int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());
			numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());

			/*
			 * Although magicNumberLabels or numberOfLabels aren't used in this class, they
			 * must be read from the bit stream.
			 */
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
		createPNGFiles(amount, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	@Override
	public void createPNGFiles(int amount, int... restrictOutputToTheseLabels) {
		int[] hashMap = new int[10];
		try {
			BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_ARGB);
			int numberOfPixels = numberOfRows * numberOfColumns;
			int[] imgPixels = new int[numberOfPixels];
			System.out.println("Extracting PNGs");
			System.out.println("Number of pixels: " + numberOfPixels);
			System.out.println("NumberofRows: " + numberOfRows);
			System.out.println("numberOfColumns: " + numberOfColumns);
			for (int i = 0; i < amount;) {
				for (int p = 0; p < numberOfPixels; p++) {
					int gray = 255 - inImage.read();
					imgPixels[p] = 0xFF000000 | (gray << 16) | (gray << 8) | gray;
				}
				image.setRGB(0, 0, numberOfColumns, numberOfRows, imgPixels, 0, numberOfColumns);
				int label = inLabel.read();
				hashMap[label]++;
				if (Arrays.stream(restrictOutputToTheseLabels).anyMatch(j -> j == label)) {
					File outputfile = new File(outputPNGPath + label + "_0" + hashMap[label] + ".png");
					System.out.println("Created file " + outputPNGPath + label + "_0" + hashMap[label] + ".png");
					ImageIO.write(image, "png", outputfile);
					i++;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InputData getTheFirstNumberFromTrainingFile() {
		resetStream();
		return readImageFiles();
	}

	public ArrayList<InputData> getTheFirstXAmountOfNumbersFromTrainingFile(int amount) {
		String s1 = this.trainsetImagePath;
		String s2 = this.trainsetLabelPath;
		this.trainsetImagePath = testsetImagePath;
		this.trainsetLabelPath = testsetLabelPath;
		resetStream();
		ArrayList<InputData> multipleImagesAsPixels = getMultipleImagesAsPixels(amount);
		this.trainsetImagePath = s1;
		this.trainsetLabelPath = s2;
		return multipleImagesAsPixels;
	}

}
