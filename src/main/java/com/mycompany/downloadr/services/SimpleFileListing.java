package com.mycompany.downloadr.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;


/**
 * {@link FileListingServices} implementation based on plain-text file
 * containing an URL at each line
 * 
 * @author xavier.castel@gmail.com
 *
 */
public class SimpleFileListing implements FileListingServices {
	
	private File source;

	@Override
	public void init(File sourceFile) {
		this.source = sourceFile;
	}

	@Override
	public long getFilesCount() throws IOException {
		return Files.lines(source.toPath()).count();
	}

	@Override
	public URL getFileAtIndex(long index) throws IOException {
		String str = Files.lines(source.toPath()).skip(index - 1).findFirst().get();
		return new URL(str);
	}

}
