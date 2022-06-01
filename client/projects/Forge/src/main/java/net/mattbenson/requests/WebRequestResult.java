package net.mattbenson.requests;

import java.util.List;
import java.util.Map;

public class WebRequestResult {
	private WebRequest request;
	private Map<String, List<String>> headers;
	private String data;
	private int response;
	
	public WebRequestResult(WebRequest request, String data, Map<String, List<String>> map, int response) {
		this.request = request;
		this.data = data;
		this.headers = map;
		this.response = response;
	}
	
	public WebRequest getRequest() {
		return request;
	}
	
	public String getData() {
		return data;
	}
	
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	
	public int getResponse() {
		return response;
	}
}
