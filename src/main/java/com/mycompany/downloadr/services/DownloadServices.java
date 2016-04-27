package com.mycompany.downloadr.services;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class DownloadServices {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadServices.class);
	
	private static final String USER_AGENT = "HTTP Downloadr";
	
	private HttpClient client;
	
	public DownloadServices() throws Exception {
		this.client = HttpClientBuilder.create()
				.setUserAgent(USER_AGENT)
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
	 * @throws Exception 
	 */
	public Path download(URL fileUrl, Path destinationFolder) throws Exception {
		return download(fileUrl, destinationFolder, null, null);
	}
	
	/**
	 * Download file with given athentication
	 * @param fileUrl
	 * @param destinationFolder
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public Path download(URL fileUrl, Path destinationFolder, String user, String password) throws Exception {
		LOGGER.debug("Downloading file {} with username {} in folder {}", fileUrl, user, destinationFolder);

		// Clean url to obtain file name
		String fileStr = fileUrl.getPath();
		if (fileStr.endsWith("/")) {
			fileStr = fileStr.substring(0, fileStr.length() - 1);
		}
		Path destinationFile = Paths.get(destinationFolder.toString(), fileStr.substring(fileStr.lastIndexOf('/')));
		
		HttpContext ctx = !Strings.isNullOrEmpty(user) ? getUserContext(user, password) : HttpClientContext.create();
		HttpGet req = new HttpGet(fileUrl.toURI());
		HttpResponse res = client.execute(req, ctx);

		// try-with-ressource and Files.copy ensure that is and destinationFile are closed
		try (InputStream is = res.getEntity().getContent()) {
			long bytes = Files.copy(is, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			LOGGER.debug("Copied {} bytes to {}", bytes, destinationFile);
		}
		
		// simulate long files download
		Thread.sleep(new Random().nextInt(10) * 1000);
		
		return destinationFile;
	}
	
	private HttpContext getUserContext(String user, String password) {
		HttpClientContext ctx = HttpClientContext.create();
		
		CredentialsProvider prov = new BasicCredentialsProvider();
		prov.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
		ctx.setCredentialsProvider(prov);
		
		return ctx;
	}
}
