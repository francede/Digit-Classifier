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

			// Although magicNumberLabels or numberOfLabels aren't used in this class, they must be read from the
			// bit stream.
			int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8)
					| (inLabel.read());
			int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());

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

	public ImageAsPixelsAndLabel getSingleImageAsPixels() {
		openFileStreams();
		ImageAsPixelsAndLabel singleImageAsPixels = null;
		singleImageAsPixels = readImageFiles();
		closeFileStreams();
		return singleImageAsPixels;
	}
	
	public ArrayList<ImageAsPixelsAndLabel> getMultipleImagesAsPixels(int amountOfImages) {
		openFileStreams();
		ArrayList<ImageAsPixelsAndLabel> multipleImagesAsPixels = new ArrayList<ImageAsPixelsAndLabel>();
		ImageAsPixelsAndLabel singleImageAsPixels = null;
		for (int i = 0; i < amountOfImages; i++) {
			if (i % 100 == 0) {
				System.out.println("IDXImageFileReaderImpl: Number of images processed: " + i);
			}
			singleImageAsPixels = readImageFiles();
			multipleImagesAsPixels.add(singleImageAsPixels);
		}	
		
		closeFileStreams();
		return multipleImagesAsPixels;
	}

	public ArrayList<ImageAsPixelsAndLabel> getAllImagesAsPixels() {
		openFileStreams();
		ArrayList<ImageAsPixelsAndLabel> allImagesAsPixels = new ArrayList<ImageAsPixelsAndLabel>();
		allImagesAsPixels = getMultipleImagesAsPixels(60000);
		closeFileStreams();
		return allImagesAsPixels;
	}

	private void createImagesAsPNGFiles() {
		// TODO Auto-generated method stub
	}

	private ImageAsPixelsAndLabel readImageFiles() {
		
		ImageAsPixelsAndLabel imageAsPixelsAndLabel = null;
		int numberOfPixels = numberOfRows * numberOfColumns;
		int[] pixelsOfImage = new int[numberOfPixels];

		try {
			
			// Read the pixels of the image
			for (int p = 0; p < numberOfPixels; p++) {
				int pixelValue = inImage.read();
				pixelsOfImage[p] = pixelValue;
			}
			
			// Read the label of the number
			int labelValue = inLabel.read();
			
			// Assign the pixels and label to a new object
			imageAsPixelsAndLabel = new ImageAsPixelsAndLabel(pixelsOfImage, labelValue);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return imageAsPixelsAndLabel;

	}

	/*
	 * The original code from Stackoverflow with the paths changed. Stored for
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
