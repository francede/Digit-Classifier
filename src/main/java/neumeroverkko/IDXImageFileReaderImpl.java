package neumeroverkko;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Pixels in the file 'train-images.idx3-ubyte' are organized row-wise. 
 * Pixel values are 0 to 255. 0 means background (white), 255 means foreground (black).
 * 
 * Label is the number drawn in the image (values 0-9)
 * 
 * This piece of code is based on a IDX file reader presented in Stackoverflow
 * by RayDeeA.
 * https://stackoverflow.com/questions/17279049/reading-a-idx-file-type-in-java
 * 
 * @author Antti Nieminen
 *
 */
public class IDXImageFileReaderImpl implements IDXImageFileReader {

	private FileInputStream inImage = null;
	private FileInputStream inLabel = null;

	private String inputImagePath = "data/train-images/train-images.idx3-ubyte";
	private String inputLabelPath = "data/train-images/train-labels.idx1-ubyte";
	private String outputPath = "data/train-images/";

	private int numberOfImages;
	private int numberOfRows;
	private int numberOfColumns;
	private int numberOfLabels;

	public IDXImageFileReaderImpl() {
		openFileStreams();
		try {
			// Although magicNumberImages isn't used in this class, it must be read from the
			// bit stream.
			int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());
			numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());

			// Although magicNumberLabels isn't used in this class, it must be read from the
			// bit stream.
			int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8)
					| (inLabel.read());
			numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());

		} catch (IOException e) {
			e.printStackTrace();
		}
		closeFileStreams();
	}

	private void openFileStreams() {
		try {
			inImage = new FileInputStream(inputImagePath);
			inLabel = new FileInputStream(inputLabelPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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

	public int[] getSingleImageAsPixels() {
		openFileStreams();
		int[] singleImageAsPixels = null;
		singleImageAsPixels = readImageFiles();
		closeFileStreams();
		return singleImageAsPixels;
	}

	public ArrayList<int[]> getAllImagesAsPixels() {
		openFileStreams();
		ArrayList<int[]> allImagesAsPixels = new ArrayList<int[]>();
		int[] singleImageAsPixels = null;
		for (int i = 0; i < numberOfImages; i++) {
			if (i % 100 == 0) {
				System.out.println("Number of images processed: " + i);
			}
			singleImageAsPixels = readImageFiles();
			allImagesAsPixels.add(singleImageAsPixels);
		}	
		
		closeFileStreams();
		return allImagesAsPixels;
	}

	private void createImagesAsPNGFiles() {
		// TODO Auto-generated method stub
	}

	private int[] readImageFiles() {

		int numberOfPixels = numberOfRows * numberOfColumns;
		int[] imageAsPixels = new int[numberOfPixels + 1];

		try {

			for (int p = 0; p < numberOfPixels; p++) {
				int pixelValue = inImage.read();
				imageAsPixels[p] = pixelValue;
			}

			int labelValue = inLabel.read();
			imageAsPixels[imageAsPixels.length - 1] = labelValue;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return imageAsPixels;

	}

	/*
	 * The original code from Stackoverflow with the paths changed. Stored here for
	 * testing purposes.
	 */
	public static void IDXReader() {
		FileInputStream inImage = null;
		FileInputStream inLabel = null;

		String inputImagePath = "data/train-images/train-images.idx3-ubyte";
		String inputLabelPath = "data/train-images/train-labels.idx1-ubyte";
		String outputPath = "data/train-images/";

		int[] hashMap = new int[10];

		try {
			inImage = new FileInputStream(inputImagePath);
			inLabel = new FileInputStream(inputLabelPath);

			int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());
			int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());
			int numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());
			int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8)
					| (inImage.read());

			int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8)
					| (inLabel.read());
			int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8)
					| (inLabel.read());

			BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_ARGB);
			int numberOfPixels = numberOfRows * numberOfColumns;
			int[] imgPixels = new int[numberOfPixels];
			System.out.println("Number of pixels: " + numberOfPixels);
			System.out.println("NumberofRows: " + numberOfRows);
			System.out.println("numberOfColumns: " + numberOfColumns);

			for (int i = 0; i < 2; i++) {

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
				File outputfile = new File(outputPath + label + "_0" + hashMap[label] + ".png");

				ImageIO.write(image, "png", outputfile);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inImage != null) {
				try {
					inImage.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (inLabel != null) {
				try {
					inLabel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
