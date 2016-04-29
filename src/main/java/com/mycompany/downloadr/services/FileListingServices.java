package com.mycompany.downloadr.services;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Service producing file URLs from an input file
 * 
 * @author xavier.castel@gmail.com
 *
 */
public interface FileListingServices {
	
	/**
	 * Initialize the file listing services
	 * @param sourceFile
	 */
	void init(Path sourceFile);

	/**
	 * Returns number of files in source file
	 * @throws IOException
	 * @return
	 */
	long getFilesCount() throws IOException;
	
	/**
	 * Returns the file URL at given index (1-based)
	 * @param index
	 * @throws IOException
	 * @return
	 */
	URL getFileAtIndex(long index) throws IOException;
	
}
