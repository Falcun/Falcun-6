package net.mattbenson.web;

import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;

import net.mattbenson.Falcun;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.minecraft.client.Minecraft;

public class FalcunAssetDownloader extends Thread {
	@Override
	public void run() {
		if(!Falcun.DOWNLOADER_ENABLE) {
			return;
		}
		
		String SHA1 = "";
		
		WebRequest request;
		try {
			request = new WebRequest("https://falcun.xyz/assets/assets.txt", "GET", ContentType.NONE, false);
			SHA1 = request.connect().getData();
		} catch (JSONException | NoSuchElementException | IOException e) {
			e.printStackTrace();
			WebRequest.showPopup("We failed to contact our servers, please try again later. Error code: NOCONN.");
		}
	
		
		String filePath = "https://falcun.xyz/assets//assets.zip";
		
		File PATH = new File(Minecraft.getMinecraft().mcDataDir + "/falcunassets/");
		File newFile = new File(Minecraft.getMinecraft().mcDataDir + "/sig.txt");
		File tempFile = new File(Minecraft.getMinecraft().mcDataDir + "/temp.zip");
		
		if(!PATH.exists()) {
			PATH.mkdirs();
		}
		
		if(!tempFile.exists()) {
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				WebRequest.showPopup("We failed to update the assets, please try again later. Error code: IO403TMP.");
			}
		}
		
		if(!newFile.exists()) {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				WebRequest.showPopup("We failed to update the assets, please try again later. Error code: IO403SIG.");
			}
		}
		
		if(newFile.exists()) {
			String newHash;
			
			try {
				newHash = getContent(newFile);
			} catch (IOException e) {
				e.printStackTrace();
				WebRequest.showPopup("We failed to update the assets, please try again later. Error code: HASH403FILE.");
				return;
			}
			
			if(newHash.equalsIgnoreCase(SHA1)) {
				return;
			}
		}
		
		try {
			request = new WebRequest(filePath, "GET", ContentType.NONE, false);
			try {
				WebRequest.showPopup("Downloading assets please wait.");
				request.connect(tempFile);
			} catch (JSONException | NoSuchElementException | IOException e) {
				e.printStackTrace();
				WebRequest.showPopup("We failed to update the assets, please try again later. Error code: JSONOSIOEXC.");
				return;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			WebRequest.showPopup("We failed to update the assets, please try again later. Error code: MALURLEXC.");
			return;
		}
		
		if(!tempFile.exists()) {
			WebRequest.showPopup("We failed to update the assets, please try again later. Error code: FILE404.");
			return;
		}
		
		try {
			try(ZipInputStream stream = new ZipInputStream(new FileInputStream(tempFile))) {
				ZipEntry entry = null;
				while((entry = stream.getNextEntry()) != null) {
					tempFile = Paths.get(PATH.getPath(), entry.getName()).toFile();
					
					if(entry.isDirectory()) {
						tempFile.mkdirs();
					} else {
						try(FileOutputStream fo = new FileOutputStream(tempFile)) {
							int length = 0;
							byte[] buffer = new byte[8192];
							
							while((length = stream.read(buffer)) > 0) {
								fo.write(buffer, 0, length);
							}
						}
					}
				}
			}
			
			write(SHA1, newFile);
			
			if(!tempFile.delete()) {
				tempFile.deleteOnExit();
			}
			WebRequest.frame.dispose();
		} catch (IOException e) {
			e.printStackTrace();
			WebRequest.showPopup("We failed to update the assets, please try again later. Error code: EXTR403?.");
		}
	}
	
	private void write(String string, File file) throws IOException {
		try (Writer writer = new BufferedWriter(new FileWriter(file, false))) {
			writer.write(string);
		}
	}
	
	private String getContent(File file) throws IOException {
		StringBuilder builder = new StringBuilder();
		
		try (Stream<String> stream = Files.lines(file.toPath())) {
	        stream.forEach(line -> {
				builder.append(line);
	        });
		} catch(UncheckedIOException e) {
			
		}
		
		return builder.toString().trim();
	}
}