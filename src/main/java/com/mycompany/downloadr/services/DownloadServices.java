package com.mycompany.downloadr.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadServices {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadServices.class);
	
	private HttpClient client;
	
	public DownloadServices() throws Exception {
		this.client = HttpClientBuilder.create()
				.setSSLContext(configureSSL()).build();
	}
	
	private SSLContext configureSSL() throws Exception {
		return SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
	}

	/**
	 * Download the given file with no authentication
	 * @param fileUrl
	 * @param destinationFolder
	 * @return
	 * @throws IOException 
	 */
	public File download(URL fileUrl, File destinationFolder) throws IOException {
		return download(fileUrl, destinationFolder, null, null);
	}
	
	/**
	 * Download file with given NTLM authentication
	 * @param fileUrl
	 * @param destinationFolder
	 * @param user
	 * @param password
	 * @return
	 */
	public File download(URL fileUrl, File destinationFolder, String user, String password) throws IOException {
		LOGGER.debug("Downloading file {} with username {} in folder {}", fileUrl, user, destinationFolder);
		return null;
	}
}
