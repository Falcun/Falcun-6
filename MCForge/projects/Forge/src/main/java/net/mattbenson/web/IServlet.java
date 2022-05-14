package net.mattbenson.web;

import java.util.Map;

public interface IServlet {
	String onRequest(String requestMethod, Map<String, String> data, String pageContent);
}
