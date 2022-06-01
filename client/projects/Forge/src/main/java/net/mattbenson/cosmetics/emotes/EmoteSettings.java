package net.mattbenson.cosmetics.emotes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.mattbenson.Falcun;

public class EmoteSettings {
	private boolean isEnabled;
	private int keybind = Keyboard.KEY_V;
	private String emote1 = "";
	private String emote2 = "";
	private String emote3 = "";
	private String emote4 = "";

	public EmoteSettings(String emote1, String emote2, String emote3, String emote4, boolean isEnabled, int bind) {
		this.emote1 = emote1;
		this.emote2 = emote2;
		this.emote3 = emote3;
		this.emote4 = emote4;
		this.isEnabled = isEnabled;
		this.keybind = bind;
	}
	
	public EmoteSettings() {
		loadEmoteSettings();
	}
	
	public boolean isEnabled() {
		return true;
	}

	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	public int getKeyBind() {
		return this.keybind;
	}

	public void setKeyBind(int bind) {
		this.keybind = bind;
	}

	public void setEmote1(String emote1) {
		this.emote1 = emote1;
	}

	public void setEmote2(String emote2) {
		this.emote2 = emote2;
	}

	public void setEmote3(String emote3) {
		this.emote3 = emote3;
	}

	public void setEmote4(String emote4) {
		this.emote4 = emote4;
	}

	public String getEmote1() {
		return this.emote1;
	}

	public String getEmote2() {
		return this.emote2;
	}

	public String getEmote3() {
		return this.emote3;
	}

	public String getEmote4() {
		return this.emote4;
	}
	
	public static void loadEmoteSettings() {
		String emote1 = "";
		String emote2 = "";
		String emote3 = "";
		String emote4 = "";
		boolean isEnabled = true;
		int bind = Keyboard.KEY_V;

		if (!Falcun.MAIN_DIR.exists()) {
			Falcun.MAIN_DIR.mkdir();
		}
		File emoteFile = new File(Falcun.MAIN_DIR + File.separator + "emote_settings.txt");
		if (!emoteFile.exists()) {
			try {
				emoteFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedReader bf = new BufferedReader(new FileReader(emoteFile));
				String line;
				while ((line = bf.readLine()) != null) {
					String[] args = line.split("::");
					String name = args[0];
					if (name.equalsIgnoreCase("enabled")) {
						isEnabled = Boolean.parseBoolean(args[1]);
					}
					if (name.equalsIgnoreCase("bind")) {
						bind = Integer.valueOf(args[1]);
					}
					if (name.equalsIgnoreCase("emote1")) {
						emote1 = args[1];
					}
					if (name.equalsIgnoreCase("emote2")) {
						emote2 = args[1];
					}
					if (name.equalsIgnoreCase("emote3")) {
						emote3 = args[1];
					}
					if (name.equalsIgnoreCase("emote4")) {
						emote4 = args[1];
					}
				}
				bf.close();
			} catch (Exception ex) {

			}
		}
		
		Falcun.getInstance().emoteSettings = new EmoteSettings(emote1, emote2, emote3, emote4, isEnabled, bind);
		saveEmoteSettings();
	}

	public static void saveEmoteSettings() {
		if (!Falcun.MAIN_DIR.exists()) {
			Falcun.MAIN_DIR.mkdir();
		}
		File emoteFile = new File(Falcun.MAIN_DIR + File.separator + "emote_settings.txt");
		if (!emoteFile.exists()) {
			try {
				emoteFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			emoteFile.delete();
			try {
				emoteFile.createNewFile();
			} catch (Exception ex) {

			}
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(emoteFile));
			StringBuilder builder1 = new StringBuilder();
			builder1.append("Enabled");
			builder1.append("::");
			builder1.append(Falcun.getInstance().emoteSettings.isEnabled);
			builder1.append("\n");
			builder1.append("Bind");
			builder1.append("::");
			builder1.append(Falcun.getInstance().emoteSettings.getKeyBind());
			builder1.append("\n");
			builder1.append("Emote1");
			builder1.append("::");
			builder1.append(Falcun.getInstance().emoteSettings.getEmote1());
			builder1.append("\n");
			builder1.append("Emote2");
			builder1.append("::");
			builder1.append(Falcun.getInstance().emoteSettings.getEmote2());
			builder1.append("\n");
			builder1.append("Emote3");
			builder1.append("::");
			builder1.append(Falcun.getInstance().emoteSettings.getEmote3());
			builder1.append("\n");
			builder1.append("Emote4");
			builder1.append("::");
			builder1.append(Falcun.getInstance().emoteSettings.getEmote4());
			writer.write(builder1.toString());
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}