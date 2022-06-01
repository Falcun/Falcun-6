package mapwriter.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FactionRegex {
	static final Map<String, String> facRegex = new HashMap<String, String>();
	static final Map<String, Pattern> facPattern = new HashMap<String, Pattern>();
	
	String factionShowRegex = "_{2,30}\\.\\[ ([^\\(].*) \\]\\.";
	String factionMapRegex = "_{1,20}\\.\\[ \\((.+)\\) (.+) \\]\\.";
	
	Pattern pShow = Pattern.compile(this.factionShowRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Pattern pMap = Pattern.compile(this.factionMapRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	
	static {
		addRegex("show", "_{2,30}\\.\\[ ([^\\(].*) \\]\\.");
		addRegex("map", "_{1,20}\\.\\[ \\((.+)\\) (.+) \\]\\.");
		addRegex("desc", "Description: (.*)");
		addRegex("pOnline", "(Followers Online|Members online) \\((\\d+)\\):( (?:.+)|)");
		addRegex("pOffline", "(Followers Online|Members offline) \\(\\d+\\): ((?:.+))");
		addRegex("landPow", "Land \\/ Power \\/ Maxpower:  (\\d+)\\/(\\d+)\\/(\\d+)");
		addRegex("facClosed", "Joining: invitation is required");
		addRegex("allies", "Allied to: (.*)");
		addRegex("enemies", "Enemies: (.*)");
		addRegex("truces", "In Truce with: (.*)");
		addRegex("wealth", "Total wealth: (.*)");
	}
	
	protected static void addRegex(String name, String regex) {
		facRegex.put(name, regex);
		facPattern.put(name, Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
	}
	
	public static String getRegex(String name) {
		return facRegex.get(name);
	}
	
	protected Pattern getPattern(String name) {
		return this.facPattern.get(name);
	}
	
	public boolean didRegex(String line, String regexName) {
		Pattern regex = getPattern(regexName);
		if (regex == null) return false;
		Matcher m = regex.matcher(line);
		if (m.find()) return true;
		return false;
	}
	
	public boolean didFactionShow(String line) {
		Matcher m = this.pShow.matcher(line);
		if (m.find()) return true;
		return false;
	}
	
	public boolean didFactionMap(String line) {

		Matcher m = this.pMap.matcher(line);
		if (m.find()) return true;
		if(line.contains("<") &&line.contains(">") && line.contains(",") && line.contains("(") && line.contains(")") ) {
			return true;
		}
		return false;
	}
}