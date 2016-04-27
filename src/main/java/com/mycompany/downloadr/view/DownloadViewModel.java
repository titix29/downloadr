package com.mycompany.downloadr.view;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import com.mycompany.downloadr.services.DownloadServices;
import com.mycompany.downloadr.services.FileListingServices;
import com.mycompany.downloadr.services.SimpleFileListing;
import com.mycompany.downloadr.utils.BackgroundViewModel;

public class DownloadViewModel extends BackgroundViewModel<Void> {

	private final StringProperty inputFileProp;
	private final StringProperty outputFolderProp;
	private final StringProperty outputConsoleProp;

	public DownloadViewModel() {
		this.inputFileProp = new SimpleStringProperty();
		this.outputFolderProp = new SimpleStringProperty();
		this.outputConsoleProp = new SimpleStringProperty("");
	}

	@Override
	protected Task<Void> backgroundTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				FileListingServices fileSrv = new SimpleFileListing();
				DownloadServices downloadSrv = new DownloadServices();
				fileSrv.init(Paths.get(inputFileProp.get()));

				URL url;
				// Path downloaded;
				long nbFiles = fileSrv.getFilesCount();
				AtomicLong nbDownloaded = new AtomicLong();
				// TODO : investigate cast issue
				List<CompletableFuture<?>> futures = new ArrayList<>();
				for (long i = 1; i <= nbFiles; i++) {
					if (isCancelled()) {
						// user clicked on stop
						break;
					}
					url = fileSrv.getFileAtIndex(i);

					// sync mode
					// updateMessage("Downloading file " + url);
					// downloaded = downloadSrv.download(url,
					// Paths.get(outputFolderProp.get()));
					// appendToConsole(String.format("File [%d/%d] downloaded at %s",
					// i, nbFiles, downloaded));
					// updateProgress(i, nbFiles);
					
					updateMessage("Downloading...");

					// async mode
					final long currentIndex = i;
					futures.add(
						downloadSrv.downloadAsync(url, Paths.get(outputFolderProp.get()))
									.whenComplete((res, ex) -> {
										if (res != null) {
											appendToConsole(String.format(
													"File [%d/%d] downloaded at %s",
													currentIndex, nbFiles, res));
											updateProgress(nbDownloaded.incrementAndGet(), nbFiles);
										} else {
											failInternal(ex);
										}
									})
					);
				}

				// Wait for all to end otherwise Service is at finished state and GUI updates accordingly
				// TODO : investigate potential error here if size() > Integer.MAX_VALUE
				CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[futures.size()]))
									.get();
				// clear status
				updateMessage("");
				return null;
			}

			private void failInternal(Throwable e) {
				appendToConsole("Download failed : " + e.getMessage());
				updateProgress(1, 1);
			}

			@Override
			protected void failed() {
				failInternal(getException());
			}

			@Override
			protected void cancelled() {
				// release any ressource
				appendToConsole("User cancelled task");
				updateMessage("");
			}

		};
	}

	public String getInputFile() {
		return inputFileProp.get();
	}

	public void setInputFile(String inputFile) {
		inputFileProp.set(inputFile);
	}

	public StringProperty getInputFileProp() {
		return inputFileProp;
	}

	public String getOutputFolder() {
		return outputFolderProp.get();
	}

	public void setOutputFolder(String outputFolder) {
		outputFolderProp.set(outputFolder);
	}

	public StringProperty getOutputFolderProp() {
		return outputFolderProp;
	}

	public String getOutputConsole() {
		return outputConsoleProp.get();
	}

	public void setOutputConsole(String outputConsole) {
		outputConsoleProp.set(outputConsole);
	}

	private void appendToConsole(String text) {
		outputConsoleProp.set(outputConsoleProp.get() + text
				+ System.lineSeparator());
	}

	public StringProperty getOutputConsoleProp() {
		return outputConsoleProp;
	}

}
