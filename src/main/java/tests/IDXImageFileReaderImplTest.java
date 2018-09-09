package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import neumeroverkko.IDXImageFileReader;
import neumeroverkko.IDXImageFileReaderImpl;

public class IDXImageFileReaderImplTest {
	
	private static IDXImageFileReader IDXReader;

	@Before public void initTest(){
		IDXReader = new IDXImageFileReaderImpl();
	}
	
	@Test public void getOneImageAsPixels() {
		System.out.println("---Test: get one image as pixels---");
		int[] imageAsPixels;
		imageAsPixels = IDXReader.getSingleImageAsPixels();
		assertEquals("The image int[] length is wrong. ", 785, imageAsPixels.length, 0.1);
		checkPixelValues(imageAsPixels);

	}
	
	public void checkPixelValues(int[] imageAsPixels) {
		for (int i : imageAsPixels) {
			assertTrue("The pixel value is below 0", i >= 0);
			assertTrue("The pixel value is greater than 255", i <= 255);
		}
	}
	
	@Test public void getMultipleImagesAsPixels() {
		System.out.println("---Test: get multiple images as pixels---");
		ArrayList<int[]> multipleImagesAsPixels;
		int amountOfImages = 5;
		multipleImagesAsPixels = IDXReader.getMultipleImagesAsPixels(amountOfImages);
		assertNotNull("The image Arraylist is null", multipleImagesAsPixels);
		for (int[] imageAsPixels : multipleImagesAsPixels) {
			checkPixelValues(imageAsPixels);
		}
	}
	
	@Ignore("Test takes a lot of time")
	@Test public void getAllImagesAsPixels() {
		System.out.println("---Test: get all images as pixels---");
		ArrayList<int[]> allImagesAsPixels;
		allImagesAsPixels = IDXReader.getAllImagesAsPixels();
		assertNotNull("The image Arraylist is null", allImagesAsPixels);
		for (int[] imageAsPixels : allImagesAsPixels) {
			checkPixelValues(imageAsPixels);
		}
	}

}
