package net.mattbenson.assets;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebRequest {
	private URL url;
	private String requestMethod;
	private int responseCode;
	private String data;
	private Map<String, String> arguments;
	private Map<String, String> headers;
	
	public WebRequest(String url, String requestMethod) throws MalformedURLException {
		this.url = new URL(url);
		this.requestMethod = requestMethod;
		this.arguments = new HashMap<>();
		this.headers = new HashMap<>();
	}
	
	public void setURL(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	public URL getURL() {
		return url;
	}
	
	public void clearHeaders() {
		headers.clear();
	}
	
	public void clearArguments() {
		arguments.clear();
	}
		
	public void setHeader(String key, String value){
		headers.put(key, value);
	}
	
	public void setArguement(String key, String value){
		arguments.put(key, value);
	}
	
	public void connect() throws IOException, JSONException, NoSuchElementException {
		connect(null);
	}
	
	public void connect(File file) throws IOException, JSONException, NoSuchElementException {
		JSONObject jsonObject = new JSONObject();
		
		for(String key : arguments.keySet()) {
			String arg = arguments.get(key);
			
			if(arg.startsWith("[") && arg.endsWith("]")) {
				jsonObject.put(key, new JSONArray(arg));
			} else {
				jsonObject.put(key, arg);
			}
		}
		
		String data = jsonObject.toString();
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setDoOutput(true);
		
		conn.setReadTimeout(5000);
		conn.setConnectTimeout(5000);
		
		conn.setRequestMethod(requestMethod);
		
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
		
		for(String header : headers.keySet()) {
			conn.setRequestProperty(header, headers.get(header));
		}
		
		conn.setRequestProperty("Content-Type", "application/json");
		
		if(!requestMethod.equalsIgnoreCase("GET")) {
			conn.setRequestProperty("Content-Length", String.valueOf(data.length()));
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
		}
		
		conn.connect();
		
		if(file != null && conn.getInputStream() != null) {
			Files.copy(conn.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} catch(IOException e) {
			if(conn.getErrorStream() != null) {
				in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
		}
		
		if(in != null) {
			StringBuilder output = new StringBuilder();;
			String input = "";
			
			while((input = in.readLine()) != null) {
				if(!output.toString().isEmpty()) {
					output.append(System.getProperty("line.separator"));
				}
				
				output.append(input);
			}
			
			this.data = output.toString();
		} else {
			throw new IOException("Failed to get input stream.");
		}
		
		responseCode = conn.getResponseCode();
	}
	
	public String getData() {
		return data;
	}
	
	public int getResponseCode() {
		return responseCode;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
}
