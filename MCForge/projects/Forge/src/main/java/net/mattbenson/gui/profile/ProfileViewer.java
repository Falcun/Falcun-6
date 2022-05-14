package net.mattbenson.gui.profile;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.mattbenson.Falcun;
import net.mattbenson.config.Config;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.MinecraftMenuImpl;
import net.mattbenson.gui.framework.draw.DrawImpl;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.types.hud.Image1;
import net.mattbenson.modules.types.hud.Image2;
import net.mattbenson.modules.types.hud.Image3;
import net.mattbenson.modules.types.hud.Image4;
import net.mattbenson.modules.types.hud.Image5;
import net.mattbenson.modules.types.hud.Text1;
import net.mattbenson.modules.types.hud.Text2;
import net.mattbenson.modules.types.hud.Text3;
import net.mattbenson.modules.types.hud.Text4;
import net.mattbenson.modules.types.hud.Text5;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ProfileViewer extends MinecraftMenuImpl implements DrawImpl {
	private final static Menu menu = new Menu("", Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	private final static ResourceLocation SETTINGS = AssetUtils.getResource("/gui/settings.png");
	
	private final static int HIGLIGHT = new Color(255, 255, 255, 200).getRGB();
	private final static int HELPER = new Color(200, 200, 200, 150).getRGB();
	
	private final static int BACKGROUND = new Color(200, 200, 200, 100).getRGB();
	private final static int BORDER = new Color(225, 225, 225, 200).getRGB();
	private final static int RESIZE = new Color(50, 50, 50, 200).getRGB();
	private final static int TEXT_COLOR = new Color(255, 255, 255, 255).getRGB();
	
	private final static int BACKGROUND_HIDDEN = new Color(200, 0, 0, 50).getRGB();
	private final static int BORDER_HIDDEN = new Color(225, 0, 0, 100).getRGB();
	
	private final static int HOVER_TEXT = new Color(200, 200, 200, 255).getRGB();
	private final static int HOVER_BACKGROUND = new Color(75, 75, 75, 255).getRGB();
	private final static int HOVER_BORDER = new Color(0, 0, 0, 255).getRGB();
	private final static int HOVER_MOUSE = new Color(143, 143, 143, 255).getRGB();
	
	private final static int SCALE = 2;
	private final static int HELPER_THICKNESS = 2;
	private final static int RESIZE_SIZE = 10;
	private final static int SETTINGS_SIZE = 16;
	private final static int SNAP_SENS = 3;
	
	private HUDElement selected;
	private boolean resizing;
	private boolean moving;
	private boolean mouseDown;
	private boolean mouseDownCache;
	private boolean rightMouseDown;
	private boolean mouseDownRightCache;
	private int cachedX;
	private int cachedY;
	private double originalScale;
	private ArrayList<String> profileList  = new ArrayList<String>();
	private Config originalConfig;
	private int size;
	private int arrow;
	
	public ProfileViewer(Module feature) {
		super(feature, menu);
	}
	
	@Override
	public void initGui() {
		menu.setLocation(0, 0);
		menu.setWidth(mc.displayWidth);
		menu.setHeight(mc.displayHeight);
		
		super.initGui();
		
		originalConfig = Falcun.getInstance().configManager.getLoadedConfig();

		String result;
		try {
			String url = "https://falcun.net/v5/share/profiles.txt";
			HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			int resp = conn.getResponseCode();
			System.out.println(resp);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String strCurrentLine;
			while ((strCurrentLine = br.readLine()) != null) {
				System.out.println(strCurrentLine);
			}
			result = strCurrentLine;
			profileList = new ArrayList<String>(Arrays.asList(result.split(",")));
		size = profileList.size();
		} catch (Throwable err) {
			err.printStackTrace();
			return;
		}
		
		// WebRequest request = null;
		// try {
		// 	request = new WebRequest("https://falcun.net/v5/share/profiles.txt", "GET", ContentType.NONE, false);
		// } catch (MalformedURLException e) {
		// 	e.printStackTrace();
		// }
		// try {
		// 	WebRequestResult result = request.connect();
		// 	profileList = new ArrayList<String>(Arrays.asList(result.getData().split(",")));
		// 	size = profileList.size();
		// } catch (JSONException | NoSuchElementException | IOException e) {
		// 	e.printStackTrace();
		// }
	}
	
	@Override
	protected void mouseClicked(int mX, int mY, int mouseButton) throws IOException {
		if(mouseButton == 0) {
			mouseDown = true;
		} else if(mouseButton == 1) {
			rightMouseDown = true;
		}
		
		super.mouseClicked(mX, mY, mouseButton);
	}
	
	@Override
	public void drawScreen(int mX, int mY, float partialTicks) {
		
		GlStateManager.pushMatrix();
		float value = guiScale / new ScaledResolution(mc).getScaleFactor();
		GlStateManager.scale(value, value, value);
			
		String line1 = "Use the arrows to navigate through different profiles!".toUpperCase();
		int line1Width = getStringWidth(line1);
		int line1Height = getStringHeight(line1);
		
		int boxWidth = line1Width + 20;
		int boxHeight = line1Height + 4 + 20;
		
		int heightOffset = 10;
		
		int boxX = menu.getX() + menu.getWidth() / 2 - boxWidth / 2;
		int boxY = heightOffset;
		
		drawRectFalcun(boxX + 1, boxY + 1, boxWidth - 1, boxHeight - 1, HOVER_BACKGROUND);
		


		int mouseX = Math.round((float)mX / value);
		int mouseY = Math.round((float)mY / value);

		if (mouseX > boxX + 1 && mouseX < boxX + 100 && mouseY > boxY + boxHeight && mouseY < boxHeight + boxHeight + 2) {
			GuiScreen.drawRectangle(boxX + 1, boxY + boxHeight, boxX + 100, boxHeight + boxHeight + 2, HOVER_MOUSE);
		} else {
			GuiScreen.drawRectangle(boxX + 1, boxY + boxHeight, boxX + 100, boxHeight + boxHeight + 2, HOVER_TEXT);
		}
		

		
		if (mouseX > boxX + boxWidth - 100 && mouseX < boxX + boxWidth && mouseY > boxY + boxHeight && mouseY < boxHeight + boxHeight + 2) {
			GuiScreen.drawRectangle(boxX + boxWidth - 100, boxY + boxHeight, boxX + boxWidth, boxHeight + boxHeight + 2, HOVER_MOUSE);
		} else {
			GuiScreen.drawRectangle(boxX + boxWidth - 100, boxY + boxHeight, boxX + boxWidth, boxHeight + boxHeight + 2, HOVER_TEXT);
		}

		drawHorizontalLine(boxX, boxY, boxWidth + 1, 1, HOVER_BORDER);
		drawHorizontalLine(boxX, boxY + boxHeight - 1, boxWidth + 1, 1, HOVER_BORDER);
		drawHorizontalLine(boxX, boxY + 33, boxWidth + 1, 1, HOVER_BORDER);

		drawArrows(">", boxX + boxWidth - 55, boxY + 35, HOVER_BORDER);
		
		drawText(line1, boxX + boxWidth / 2 - line1Width / 2, boxY + 12, HOVER_TEXT);

		drawText("DOWNLOAD", boxX + 15, boxY + 42, HOVER_BORDER);

		GlStateManager.popMatrix();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
	
		mouseDownCache = mouseDown;
		mouseDownRightCache = rightMouseDown;
		
		if(mouseDown) {
			mouseDown = Mouse.isButtonDown(0);
		}
		
		if(rightMouseDown) {
			rightMouseDown = Mouse.isButtonDown(1);
		}
		
	
			if (mouseDown) {
				
				if (mouseX > boxX + boxWidth - 100 && mouseX < boxX + boxWidth && mouseY > boxY + boxHeight && mouseY < boxHeight + boxHeight + 2) {
				
					Falcun.getInstance().configManager.getConfig(originalConfig.getName()).load();
					
					if (arrow >= size) {
						arrow = 0;
					}
					
					String configName = profileList.get(arrow);			
					  WebRequest request = null;
					try {
						request = new WebRequest(Falcun.PROFILES_URL + "/uploads/" + URLEncoder.encode(configName, "UTF-8") + "/config.json", "GET", ContentType.FORM, false);
					} catch (MalformedURLException | UnsupportedEncodingException e) {
						e.printStackTrace();
					}
			          WebRequestResult result = null;
					try {
						result = request.connect();
					} catch (JSONException | NoSuchElementException | IOException e) {
						e.printStackTrace();
					}
			          if (result.getResponse() == 200) {

			            JSONObject obj = new JSONObject(result.getData());
			            JSONArray macroList = obj.getJSONArray("newhud");

			            for (int i = 0; i < macroList.length(); i++) {
			              JSONObject macro = macroList.getJSONObject(i);

			              if (macro.getString("username").toString().equals("file")) {
			                DownloadImage("https://falcun.net/v5/share/uploads/" + configName + "/" + macro.getString("file"), Minecraft.getMinecraft().mcDataDir + "/hud/" + macro.getString("file"));
			              }
			            }
		                Falcun.getInstance().configManager.load(result.getData());
			          } else {
			            Falcun.getInstance().notificationManager.showNotification("An error occurred", Color.RED);
			          }
					
			          arrow++;
			          Minecraft.getMinecraft().renderGlobal.loadRenderers();
				} 
				
				if (mouseX > boxX + 1 && mouseX < boxX + 100 && mouseY > boxY + boxHeight && mouseY < boxHeight + boxHeight + 2) {
					int get = 0;
					if (arrow > 0) {
						get = arrow - 1;
					}
					Falcun.getInstance().configManager.getConfig(profileList.get(get)).load();
					mc.displayGuiScreen(null);
				}
				
				
				mouseDown = false;
			}
		

		
		
	}
	
	@Override
	public void drawText(String text, int x, int y, int color) {
		Fonts.RobotoSmall.drawString(text, x, y, color);
	}
	
	public void drawArrows(String text, int x, int y, int color) {
		Fonts.largeBoldFontRenderer.drawString(text, x, y, color);
	}
	
	@Override
	public int getStringWidth(String string) {
		return (int) Fonts.RobotoSmall.getStringWidth(string);
	}
	
	@Override
	public int getStringHeight(String string) {
		return (int) Fonts.RobotoSmall.getStringHeight(string);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		Image1.imageRun();
		Image2.imageRun();
		Image3.imageRun();
		Image4.imageRun();
		Image5.imageRun();
		Text1.imageRun();
		Text2.imageRun();
		Text3.imageRun();
		Text4.imageRun();
		Text5.imageRun();
		
		new Thread(() -> {
			Config config = Falcun.getInstance().configManager.getLoadedConfig();
			
			if(config != null) {
				config.save();
			}
		}).start();
	}
	

	public static void DownloadImage(String search, String path) {
	    InputStream inputStream = null;
	    OutputStream outputStream = null;

	    try {
	      URL url = new URL(search);
	      String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
	      URLConnection con = url.openConnection();
	      con.setRequestProperty("User-Agent", USER_AGENT);
	      inputStream = con.getInputStream();
	      outputStream = new FileOutputStream(path);
	      byte[] buffer = new byte[2048];
	      int length;
	      while ((length = inputStream.read(buffer)) != -1) {
	        outputStream.write(buffer, 0, length);
	      }
	    } catch (Exception ex) {}
	    try {
	      outputStream.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    try {
	      inputStream.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			if(menu.onMenuExit(keyCode)) {
				return;
			}
			
			mc.displayGuiScreen(null);
			Falcun.getInstance().configManager.getConfig(originalConfig.getName()).load();
		} else {
			menu.onKeyDown(typedChar, keyCode);
		}
	}
}
