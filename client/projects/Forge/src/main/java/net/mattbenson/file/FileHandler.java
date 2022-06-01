package net.mattbenson.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileHandler {
	private final String separator = System.getProperty("line.separator");
	
	private File file;
	private boolean fresh = false;
	
	public FileHandler(File file) {
		this.file = file;
	}
	
	public void init() throws IOException {
		if (file.getParent() != null) {
			File folder = new File(file.getParent());
			
			if(!folder.exists()) {
				folder.mkdirs();
			}
		}
		
		if(!file.exists()){
			file.createNewFile();
			fresh = true;
		}
	}
	
	public void writeToFile(String content, boolean append) throws IOException {
		try (Writer writer = new BufferedWriter(new FileWriter(file, append))) {
			writer.write(content);
		}
	}
	
	public void writeToFile(byte[] content) throws IOException {
		try (OutputStream writer = new FileOutputStream(file)) {
			writer.write(content);
		}
	}
	
	public byte[] getContentInBytes() throws IOException {
		byte[] bytes = new byte[(int)file.length()];
		
		try (FileInputStream reader = new FileInputStream(file)) {
			reader.read(bytes);
		}
		
		return bytes;
	}
	
	public String getContent(boolean ignoreComments) throws IOException {
		StringBuilder builder = new StringBuilder();
		
		try (Stream<String> stream = Files.lines(file.toPath())) {
	        stream.forEach(line -> {
				if(line.startsWith("//") && ignoreComments)
					return;
				
				builder.append((line.isEmpty() ? "" : separator) + line);
	        });
		}
		
		return builder.toString();
	}
	
	public String[] getContentInLines(boolean ignoreComments) throws IOException {
		List<String> lines = new ArrayList<>();
		
		try (Stream<String> stream = Files.lines(file.toPath())) {
	        stream.forEach(line -> {
				if(line.startsWith("//") && ignoreComments)
					return;
				
				lines.add(line);
	        });
		}
		
		return lines.toArray(new String[lines.size()]);
	}
	
	public String[] getLinesByPrefix(String prefix, boolean ignoreComments) throws IOException {
		List<String> lines = new ArrayList<>(Arrays.asList(getContentInLines(ignoreComments)));

		for(int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			if (!line.startsWith(prefix)) {
				lines.remove(i);
			}
		}

		return lines.toArray(new String[lines.size()]);
	}
	
	public String[] getLinesByNeedle(String needle, boolean ignoreComments) throws IOException {
		List<String> lines = new ArrayList<>(Arrays.asList(getContentInLines(ignoreComments)));

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			if (!line.contains(needle)) {
				lines.remove(i);
			}
		}

		return lines.toArray(new String[lines.size()]);
	}
	
	public void removeLinesByPrefix(String prefix, boolean ignoreComments) throws IOException {
		List<String> lines = new ArrayList<>(Arrays.asList(getContentInLines(ignoreComments)));

		StringBuilder output = new StringBuilder();

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			if (!line.startsWith(prefix)) {
				output.append(String.valueOf((output.length() == 0) ? "" : separator) + line);
			}
		}

		writeToFile(output.toString(), false);
	}

	public void removeLinesByNeedle(String needle, boolean ignoreComments) throws IOException {
		List<String> lines = new ArrayList<>(Arrays.asList(getContentInLines(ignoreComments)));

		StringBuilder output = new StringBuilder();

		for(int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			if (!line.contains(needle)) {
				output.append(String.valueOf((output.length() == 0) ? "" : separator) + line);
			}
		}

		writeToFile(output.toString(), false);
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isFresh() {
		return fresh;
	}

	public void setFresh(boolean fresh) {
		this.fresh = fresh;
	}
}
