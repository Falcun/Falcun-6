package net.mattbenson.assets;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.minecraft.client.main.Main;

public class FalcunUpdater {
	private final static String JSON = "https://falcun.xyz/launcher/version.json";
	private final static String CDN_URL = "https://falcun.xyz/launcher/";
	private final static String FORGE_VERSION = "1.8.9-11.15.1.2318-1.8.9";
	
	private String[] arguments;
	
	private File forgeJar;
	private File folder;

	public FalcunUpdater(String[] arguments) {
		this.arguments = arguments;
		
		String origPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String curPath = origPath;
		
		String search = ".jar!";
		int index = curPath.toLowerCase().indexOf(search.toLowerCase());
		if(index != -1) {
			curPath = curPath.substring(0, index + search.length() - 1);
			
			search = "file:";
			index = curPath.toLowerCase().indexOf(search.toLowerCase());
			if(index != -1) {
				this.folder = Paths.get(curPath.substring(0, index), "libraries", "net", "minecraftforge", "forge", FORGE_VERSION).toFile();
				this.forgeJar = Paths.get(folder.getAbsolutePath(), "forge-" + FORGE_VERSION + ".jar").toFile();
		
				System.out.println("RAW PATH " + origPath);
				System.out.println("FOLDER: " + folder.getAbsolutePath());
				System.out.println("FORGE JAR: " + forgeJar.getAbsolutePath());
				
				if(!forgeJar.exists()) {
					this.folder = null;
					this.forgeJar = null;
				}	
				}
			}
		}

	public String[] run() {
		
		String operSys = System.getProperty("os.name").toLowerCase();
		if (operSys.contains("win")) {
			if(forgeJar == null) {
				showPopup("We failed to trace the minecraft installation path. You've not been updated for now.");
				return arguments;
			}
			if(forgeJar.getPath().toLowerCase().endsWith("bin")) {
				return arguments;
			}
			
			String sha1 = getSHA1(forgeJar);
			
			if(sha1 == null) {
				showPopup("We failed to validate the integrity of your version. You've not been updated for now.");
				return arguments;
			}
			
			try {
				WebRequest1 request = new WebRequest1(JSON, "GET");
				request.connect();
				
				JSONObject json = new JSONObject(request.getData());
				JSONArray libraries = json.getJSONArray("libraries");
				
				String sha1Found = null;
				String name = "";
				
				for(int i = 0; i < libraries.length(); i++) {
					JSONObject library = libraries.getJSONObject(i);
					
					if(!library.has("url") || library.isNull("url")) {
						continue;
					}
					
					if(library.getString("url").equalsIgnoreCase(CDN_URL)) {
						sha1Found = library.getString("sha1");
						name = library.getString("name");
						break;
					}
				}
				
				System.out.println("SHA1Found:" + sha1Found);
				System.out.println("name:" + name);
				
				if(sha1Found == null || name == null) {
					throw new IOException("Missing falcun entry.");
				}
				
				if(sha1.equalsIgnoreCase(sha1Found)) {
					return arguments;
				}
				
				new Thread(() -> {
					showPopup("New update found! Attempting update, stand by.");
				}).start();
				
				Path path = Paths.get(folder.getPath(), "UpdateWrapper.jar");
				Files.copy(FalcunUpdater.class.getResourceAsStream("/UpdateWrapper.jar"), path, StandardCopyOption.REPLACE_EXISTING);
				String updateWrapperPath = path.toFile().getAbsolutePath();
				
				File temp = Paths.get(folder.getPath(), "temp-jar.jar").toFile();
				
				if(temp.exists()) {
					temp.delete();
				}
				
				StringBuilder urlBuilder = new StringBuilder(CDN_URL);
				
				String lastFolder = "";
				String[] parts = name.split(":");
				
				for(int i = 0; i < parts.length; i++) {
					String part = parts[i];
					
					if(i == 0) {
						String folders = part.replaceAll(Pattern.quote("."), "/");
						urlBuilder.append(folders);
					} else if(i == 1) {
						lastFolder = part + "-";
						urlBuilder.append("/" + part + "/");
					} else if(i == 2) {
						lastFolder += part;
						urlBuilder.append(part + "/");
					}
				}
				
				urlBuilder.append(lastFolder + ".jar");
				
				request.setURL(urlBuilder.toString());
				request.connect(temp);
				
				StringBuilder args = new StringBuilder();
				
				args.append("javaw -jar \"" + updateWrapperPath + "\"");
				args.append(" ");
				args.append("-old " + forgeJar.getAbsolutePath());
				args.append(" ");
				args.append("-new " + temp.getAbsolutePath());
				
				/*
				RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
				
				args.append(" ");
				args.append("-launch ");
				
				args.append("java -cp \"" + versionJar.getAbsolutePath() + "\"");
				
				args.append(" ");
				
				args.append("net.minecraft.client.main.Main");
				
				args.append(" ");
				
				args.append(mx.getClassPath().replaceAll("-", "\\-"));
				args.append(" ");
				
				for(String arg : mx.getInputArguments()) {
					String str = arg;
					
					if(str.startsWith("-")) {
						str = "\\" + str;
					}
					
					args.append(str + " ");
				}
							
				for(String arg : arguments) {
					args.append(arg + " ");
				}
				*/			
				Runtime.getRuntime().exec(args.toString());
				WebRequest1.frame.dispose();
				System.exit(0);
			} catch (JSONException | NoSuchElementException | IOException e) {
				e.printStackTrace();
				showPopup("We failed to validate the integrity of your version. You've not been updated for now.");
				return arguments;
			}
		}
		
		

		
		
		
		if (operSys.contains("mac")) {
		if(forgeJar == null) {
			showPopup("We failed to trace the minecraft installation path. You've not been updated for now.");
			return arguments;
		}
		
		if(forgeJar.getPath().toLowerCase().endsWith("bin")) {
			return arguments;
		}
		
		String sha1 = getSHA1(forgeJar);
		
		if(sha1 == null) {
			showPopup("We failed to validate the integrity of your version. You've not been updated for now.");
			return arguments;
		}
		
		try {
			WebRequest request = new WebRequest(JSON, "GET");
			request.connect();
			
			JSONObject json = new JSONObject(request.getData());
			JSONArray libraries = json.getJSONArray("libraries");
			
			String sha1Found = null;
			String name = "";
			
			for(int i = 0; i < libraries.length(); i++) {
				JSONObject library = libraries.getJSONObject(i);
				
				if(!library.has("url") || library.isNull("url")) {
					continue;
				}
				
				if(library.getString("url").equalsIgnoreCase(CDN_URL)) {
					sha1Found = library.getString("sha1");
					name = library.getString("name");
					break;
				}
			}
			
			System.out.println("SHA1Found:" + sha1Found);
			System.out.println("name:" + name);
			
			if(sha1Found == null || name == null) {
				throw new IOException("Missing falcun entry.");
			}
			
			if(sha1.equalsIgnoreCase(sha1Found)) {
				return arguments;
			}
			
			new Thread(() -> {
				showPopup("New update found! Attempting update, stand by.");
			}).start();
			
			Path path = Paths.get(folder.getPath(), "UpdateWrapper.jar");
			Files.copy(FalcunUpdater.class.getResourceAsStream("/UpdateWrapper.jar"), path, StandardCopyOption.REPLACE_EXISTING);
			String updateWrapperPath = path.toFile().getAbsolutePath();
			
			File temp = Paths.get(folder.getPath(), "temp-jar.jar").toFile();
			
			if(temp.exists()) {
				temp.delete();
			}
			
			StringBuilder urlBuilder = new StringBuilder(CDN_URL);
			
			String lastFolder = "";
			String[] parts = name.split(":");
			
			for(int i = 0; i < parts.length; i++) {
				String part = parts[i];
				
				if(i == 0) {
					String folders = part.replaceAll(Pattern.quote("."), "/");
					urlBuilder.append(folders);
				} else if(i == 1) {
					lastFolder = part + "-";
					urlBuilder.append("/" + part + "/");
				} else if(i == 2) {
					lastFolder += part;
					urlBuilder.append(part + "/");
				}
			}
			
			urlBuilder.append(lastFolder + ".jar");
			
			request.setURL(urlBuilder.toString());
			request.connect(temp);
			
			StringBuilder args = new StringBuilder();
			
			args.append("java -jar " + updateWrapperPath);
			args.append(" ");
			args.append("-old " + forgeJar.getAbsolutePath());
			args.append(" ");
			args.append("-new " + temp.getAbsolutePath());
			
			/*
			RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
			
			args.append(" ");
			args.append("-launch ");
			
			args.append("java -cp \"" + versionJar.getAbsolutePath() + "\"");
			
			args.append(" ");
			
			args.append("net.minecraft.client.main.Main");
			
			args.append(" ");
			
			args.append(mx.getClassPath().replaceAll("-", "\\-"));
			args.append(" ");
			
			for(String arg : mx.getInputArguments()) {
				String str = arg;
				
				if(str.startsWith("-")) {
					str = "\\" + str;
				}
				
				args.append(str + " ");
			}
						
			for(String arg : arguments) {
				args.append(arg + " ");
			}
			*/			
			System.out.println("LAAAUNCH CMMMD: " + "java -jar " + updateWrapperPath + " -old " + forgeJar.getAbsolutePath() + " -new " + temp.getAbsolutePath());
			
			WebRequest1.frame.dispose();
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", updateWrapperPath, "-old", forgeJar.getAbsolutePath(), "-new", temp.getAbsolutePath());
			Process p = pb.start();
			


			try {
				int exitCode = p.waitFor();
				System.out.println(exitCode);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.exit(0);
		} catch (JSONException | NoSuchElementException | IOException e) {
			e.printStackTrace();
			showPopup("We failed to validate the integrity of your version. You've not been updated for now.");
			return arguments;
		}
		
		return null;
		}
		
		
		
		
		return null;
	}
	
	private void showPopup(String string) {
		JFrame panel = new JFrame();
		panel.setAlwaysOnTop(true);
		panel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panel.setLocationRelativeTo(null);
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(panel, string);
	}
	
	
	private String getSHA1(File file) {
		try(InputStream fileStream = new FileInputStream(file)) {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			
			try(DigestInputStream digestStream = new DigestInputStream(fileStream, digest)) {
				byte[] buffer = new byte[8192];
				while(digestStream.read(buffer) != -1);
				
				StringBuilder builder = new StringBuilder();
				byte[] message = digestStream.getMessageDigest().digest();
				
				for(byte b : message) {
					builder.append(String.format("%02x", b));
				}
				
				return builder.toString();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}