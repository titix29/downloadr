package com.mycompany.downloadr.services;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleFileListingTest {

	private static FileListingServices srv;
	
	@BeforeClass
	public static void init() {
		srv = new SimpleFileListing();
	}
	
	@Test
	public void sample1_size() throws Exception {
		srv.init(getSample1());
		assertEquals(3, srv.getFilesCount());
	}
	
	@Test
	public void sample1_first() throws Exception {
		srv.init(getSample1());
		assertEquals("http://docs.oracle.com/javase/8/javafx/properties-binding-tutorial/binding.htm", 
				srv.getFileAtIndex(1).toString());
	}
	
	@Test
	public void sample1_last() throws Exception {
		srv.init(getSample1());
		assertEquals("http://code.makery.ch/library/javafx-8-tutorial/fr/part1/", 
				srv.getFileAtIndex(3).toString());
	}
	
	private File getSample1() throws Exception {
		return new File(getClass().getClassLoader().getResource("sample1.txt").toURI());
	}
}
