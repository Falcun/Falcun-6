package net.mattbenson.macros;

public class Macro {
	private String name;
	private String command;
	private int key;
	private boolean enabled;
	
	public Macro(String name, String command, int key, boolean enabled) {
		this.name = name;
		this.command = command;
		this.key = key;
		this.enabled = enabled;
	}
	
	public Macro(String name, String command, int key) {
		this(name, command, key, true);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
