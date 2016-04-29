package com.mycompany.downloadr.services;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * {@link FileListingServices} implementation based on plain-text file
 * containing an URL at each line
 * 
 * @author xavier.castel@gmail.com
 *
 */
public class SimpleFileListing implements FileListingServices {
	
	private Path source;

	@Override
	public void init(Path sourceFile) {
		this.source = sourceFile;
	}

	@Override
	public long getFilesCount() throws IOException {
		return Files.lines(source).count();
	}

	@Override
	public URL getFileAtIndex(long index) throws IOException {
		String str = Files.lines(source).skip(index - 1).findFirst().get();
		return new URL(str);
	}

}
