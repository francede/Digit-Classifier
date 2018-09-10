package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import neumeroverkko.IDXImageFileReader;
import neumeroverkko.IDXImageFileReaderImpl;
import neumeroverkko.ImageAsPixelsAndLabel;

public class IDXImageFileReaderImplTest {
	
	private static IDXImageFileReader IDXReader;

	@Before public void initTest(){
		IDXReader = new IDXImageFileReaderImpl();
	}
	
	@Test public void getOneImageAsPixels() {
		System.out.println("---Test: get one image as pixels---");
		ImageAsPixelsAndLabel imageAsPixels;
		imageAsPixels = IDXReader.getSingleImageAsPixels();
		assertEquals("The image int[] length is wrong. ", 784, imageAsPixels.getPixels().length, 0.1);
		checkPixelValues(imageAsPixels);

	}
	
	public void checkPixelValues(ImageAsPixelsAndLabel imageAsPixels) {
		for (int i : imageAsPixels.getPixels()) {
			assertTrue("The pixel value is below 0", i >= 0);
			assertTrue("The pixel value is greater than 255", i <= 255);
		}
	}
	
	@Test public void checkLabelValues() {
		System.out.println("---Test: check label values from 5 first images in the training set---");
		ArrayList<ImageAsPixelsAndLabel> multipleImagesAsPixels;
		int amountOfImages = 5;
		int[] rightLabels = {0,0,8,1,0};
		multipleImagesAsPixels = IDXReader.getMultipleImagesAsPixels(amountOfImages);
		for (int i = 0; i < amountOfImages; i++) {
			assertEquals("The label was wrong for a number", rightLabels[i], multipleImagesAsPixels.get(i).getLabel());
		}
	}
	
	@Test public void getMultipleImagesAsPixels() {
		System.out.println("---Test: get multiple images as pixels---");
		ArrayList<ImageAsPixelsAndLabel> multipleImagesAsPixels;
		int amountOfImages = 5;
		multipleImagesAsPixels = IDXReader.getMultipleImagesAsPixels(amountOfImages);
		assertNotNull("The image Arraylist is null", multipleImagesAsPixels);
		for (ImageAsPixelsAndLabel imageAsPixels : multipleImagesAsPixels) {
			checkPixelValues(imageAsPixels);
		}
	}
	
	@Ignore("Test takes a lot of time")
	@Test public void getAllImagesAsPixels() {
		System.out.println("---Test: get all images as pixels---");
		ArrayList<ImageAsPixelsAndLabel> allImagesAsPixels;
		allImagesAsPixels = IDXReader.getAllImagesAsPixels();
		assertNotNull("The image Arraylist is null", allImagesAsPixels);
		for (ImageAsPixelsAndLabel imageAsPixels : allImagesAsPixels) {
			checkPixelValues(imageAsPixels);
		}
	}

}
