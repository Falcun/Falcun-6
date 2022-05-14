package net.mattbenson.gui.menu.pages;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.NoSuchElementException;

import javax.net.ssl.HttpsURLConnection;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.mattbenson.Falcun;
import net.mattbenson.config.Config;
import net.mattbenson.file.FileHandler;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.Page;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticRainbowButton;
import net.mattbenson.gui.menu.components.macros.MacroButton;
import net.mattbenson.gui.menu.components.macros.MacroTextfield;
import net.mattbenson.gui.menu.components.macros.SimpleTextButton;
import net.mattbenson.gui.menu.components.mods.ModCategoryButton;
import net.mattbenson.gui.menu.components.mods.ModScrollPane;
import net.mattbenson.gui.menu.components.profiles.ProfilesBase;
import net.mattbenson.gui.menu.components.profiles.ProfilesBlueButton;
import net.mattbenson.gui.profile.ProfileViewer;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ProfilesPage extends Page {
  private MacroTextfield nameNew;
  private MacroTextfield nameAdd;
  private MacroButton add;
  private ProfilesBlueButton download;
  private MacroButton delete;
  private ModScrollPane scrollPane;
  private CosmeticRainbowButton profileViewer;
  private ProfileViewer profilePage;

  public ProfilesPage(Minecraft mc, Menu menu, IngameMenu parent) {
    super(mc, menu, parent);
    
    profilePage = new ProfileViewer(parent.getFeature());	
  }

  @Override
  public void onInit() {
    int width = 300;
    int x = menu.getWidth() - width + 20;
    int y = 59;

    int compWidth = width - 6 - 20 * 2;

    nameNew = new MacroTextfield(TextPattern.TEXT_AND_NUMBERS, x, y + 85, compWidth, 22, "...");
    nameAdd = new MacroTextfield(TextPattern.TEXT_AND_NUMBERS, x, y + 255, compWidth, 22, "...");

    int acceptWidth = compWidth - 40;
    
    profileViewer = new CosmeticRainbowButton("DOWNLOAD A PROFILE", 150, y + 11, width - 80, 30) {
		@Override
		public void onAction() {
			setActive(false);
			mc.displayGuiScreen(profilePage);
		}
	};

    add = new MacroButton("ADD", x - 21 + width / 2 - acceptWidth / 2, y + 125, acceptWidth, 22, true) {
      @Override
      public void onAction() {
        setActive(false);

        if (nameNew.getText().isEmpty()) {
          return;
        }

        Falcun.getInstance().configManager.getConfig(nameNew.getText()).save();
        nameNew.setText("");

        populateScrollPane();
      }
    };

    download = new ProfilesBlueButton("DOWNLOAD", x - 21 + width / 2 - acceptWidth / 2, y + 295, acceptWidth, 22) {
      @Override
      public void onAction() {
        setActive(false);

        if (nameAdd.getText().isEmpty()) {
          return;
        }

        try {
          WebRequest request = new WebRequest(Falcun.PROFILES_URL + "/uploads/" + URLEncoder.encode(nameAdd.getText(), "UTF-8") + "/config.json", "GET", ContentType.FORM, false);
          WebRequestResult result = request.connect();
          if (result.getResponse() == 200) {

            JSONObject obj = new JSONObject(result.getData());
            JSONArray macroList = obj.getJSONArray("newhud");

            for (int i = 0; i < macroList.length(); i++) {
              JSONObject macro = macroList.getJSONObject(i);

              if (macro.getString("username").toString().equals("file")) {
                DownloadImage("https://falcun.net/v5/share/uploads/" + nameAdd.getText() + "/" + macro.getString("file"), Minecraft.getMinecraft().mcDataDir + "/hud/" + macro.getString("file"));
              }
            }
            Falcun.getInstance().configManager.getConfig(nameAdd.getText()).load(result.getData());
          } else {
            Falcun.getInstance().notificationManager.showNotification("Config failed to download, make sure the profile name is accurate.", Color.RED);
          }
        } catch (JSONException | NoSuchElementException | IOException e) {
          Falcun.getInstance().log.error("Failed to download config.", e);
        }

        nameAdd.setText("");
        populateScrollPane();
      }
    };

    delete = new MacroButton("CLEAR ALL PROFILES", x - 21 + width / 2 - compWidth / 2, menu.getHeight() - 22 - 20, compWidth, 22, false) {
      @Override
      public void onAction() {
        setActive(false);

        Falcun.getInstance().configManager.deleteAllConfigs();
        populateScrollPane();
      }
    };

    scrollPane = new ModScrollPane(31, 140, menu.getWidth() - width - 31 * 2, menu.getHeight() - 141, false);

    populateScrollPane();
  }

  private void populateScrollPane() {
    scrollPane.getComponents().clear();

    int spacing = 15;
    int height = 110;

    int defaultX = spacing;

    int y = spacing;
    int x = spacing;

    int width = 190;

    int maxWidth = scrollPane.getWidth() - spacing * 2;

    int innerSpacing = 5;
    int innerWidth = width - innerSpacing * 2;
    int buttonHeight = 20;

    int exitButtonSize = 18;
    
    for (Config config: Falcun.getInstance().configManager.getConfigs()) {
      scrollPane.addComponent(new ProfilesBase(config.getName(), x, y, width, height));

      scrollPane.addComponent(new SimpleTextButton("X", x + innerWidth - exitButtonSize + innerSpacing, y + innerSpacing, exitButtonSize, exitButtonSize, false) {
        @Override
        public void onAction() {
          setActive(false);

          config.delete();
          populateScrollPane();
        }
      });

      scrollPane.addComponent(new ProfilesBlueButton("UPLOAD PROFILE", x + innerSpacing, y + height - buttonHeight - innerSpacing * 3 - spacing, innerWidth, buttonHeight) {
        @Override
        public void onAction() {
          setActive(false);

          try {
            String code = URLEncoder.encode(RandomStringUtils.randomAlphabetic(12).toLowerCase(), "UTF-8");

            try {

              FileHandler test1 = new FileHandler(config.getFileHandler().getFile());

              String content = test1.getContent(false);

              JSONObject obj = new JSONObject(content);

              JSONArray macroList = obj.getJSONArray("newhud");

              for (int i = 0; i < macroList.length(); i++) {
                JSONObject macro = macroList.getJSONObject(i);

                if (macro.getString("username").toString().equals("file")) {
                  if (uploadImageToServer(Minecraft.getMinecraft().mcDataDir + "/hud/" + macro.getString("file"), code) == 1) {
                    Falcun.getInstance().notificationManager.showNotification("Config uploaded, config code: " + code + ", copied to clipboard.", Color.RED);
                  } else {
                    Falcun.getInstance().notificationManager.showNotification("Config failed to upload.", Color.RED);
                  }
                }
              }
            } catch (JSONException e) {

            }

            if (uploadProfileToServer(config.getFileHandler().getFile().getPath(), code) == 1) {
              Falcun.getInstance().notificationManager.showNotification("Config uploaded, config code: " + code + ", copied to clipboard.", Color.RED);
            } else {
              Falcun.getInstance().notificationManager.showNotification("Config failed to upload.", Color.RED);
            }

            GuiScreen.setClipboardString(code);
          } catch (JSONException | NoSuchElementException | IOException e) {
            Falcun.getInstance().log.error("Failed to upload config.", e);
          }

        }
      });

      scrollPane.addComponent(new MacroButton(config.isEnabled() ? "ENABLED" : "DISABLED", x + innerSpacing, y + height - innerSpacing * 2 - spacing, innerWidth, buttonHeight, config.isEnabled()) {
        @Override
        public void onAction() {
          setActive(false);

          Config cfg = Falcun.getInstance().configManager.getLoadedConfig();

          if (cfg != null) {
            cfg.save();
          }

          config.load();
          populateScrollPane();
        }
      });

      x += spacing + width;

      if (x + spacing + width > maxWidth) {
        x = defaultX;
        y += height + spacing;
      }
    }
  }

  @Override
  public void onRender() {
    int width = 300;
    int x = menu.getX() + menu.getWidth() - width + 20;
    int y = menu.getY() + 59;
    int height = 32;

    Fonts.RobotoTitle.drawString("PROFILES", menu.getX() + 31, menu.getY() + 80, IngameMenu.MENU_HEADER_TEXT_COLOR);

    drawHorizontalLine(menu.getX() + 31, menu.getY() + 110, menu.getWidth() - width - 31 * 2, 3, IngameMenu.MENU_LINE_COLOR);

    drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, menu.getHeight() - 58, MacrosPage.MENU_SIDE_BG_COLOR);

    drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, height + 1, ModCategoryButton.MAIN_COLOR);
    drawShadowDown(menu.getX() + menu.getWidth() - width, y + height, width);
    Fonts.RobotoMiniHeader.drawString("CREATE NEW PROFILE", menu.getX() + menu.getWidth() - width / 2 - Fonts.RobotoMiniHeader.getStringWidth("CREATE NEW PROFILE") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("CREATE NEW PROFILE") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);

    drawShadowDown(menu.getX() + menu.getWidth() - width, y - 1, width);

    y += 60;

    Fonts.Roboto.drawString("ENTER NAME", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);

    y += 120;

    drawRectFalcun(menu.getX() + menu.getWidth() - width, y, width, height + 1, ModCategoryButton.MAIN_COLOR);
    drawShadowDown(menu.getX() + menu.getWidth() - width, y + height, width);
    drawShadowUp(menu.getX() + menu.getWidth() - width, y, width);
    Fonts.RobotoMiniHeader.drawString("DOWNLOAD PROFILE", menu.getX() + menu.getWidth() - width / 2 - Fonts.RobotoMiniHeader.getStringWidth("DOWNLOAD PROFILE") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("DOWNLOAD PROFILE") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);

    y += 50;

    Fonts.Roboto.drawString("PROFILE CODE", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
  }

  @Override
  public void onLoad() {
    menu.addComponent(nameNew);
    menu.addComponent(nameAdd);
    menu.addComponent(add);
    menu.addComponent(download);
    menu.addComponent(delete);
    menu.addComponent(scrollPane);
    menu.addComponent(profileViewer);
  }

  @Override
  public void onUnload() {

  }

  @Override
  public void onOpen() {

  }

  @Override
  public void onClose() {

  }

  public int uploadProfileToServer(String fileLocation, String unique) throws IOException {
    // String postReceiverUrl = "http://falcun.net/v5/share/upload.php?id=" + unique;
    // HttpClient httpClient = new DefaultHttpClient();
    // HttpPost httpPost = new HttpPost(postReceiverUrl);
    // File file = new File(fileLocation);
    // FileBody fileBody = new FileBody(file);
    // MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    // reqEntity.addPart("fileToUpload", fileBody);
    // httpPost.setEntity(reqEntity);
    // HttpResponse response = httpClient.execute(httpPost);
    // HttpEntity resEntity = response.getEntity();

      String result ="";
      try {
        String url = "https://falcun.net/v5/share/upload.php?id=" + unique;
        HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
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
      } catch (Throwable err) {
        err.printStackTrace();
      }
      
    if (!result.isEmpty()) {
      // String responseStr = EntityUtils.toString(resEntity).trim();
      String responseStr = result.trim();
      if (responseStr.toString().contains("has been uploaded")) {
        return 1;
      }
    }
    return 0;

  }

  public int uploadImageToServer(String fileLocation, String unique) throws IOException {
    // String postReceiverUrl = "http://falcun.net/v5/share/uploadimage.php?id=" + unique;
    // HttpClient httpClient = new DefaultHttpClient();
    // HttpPost httpPost = new HttpPost(postReceiverUrl);
    // File file = new File(fileLocation);
    // FileBody fileBody = new FileBody(file);
    // MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    // reqEntity.addPart("fileToUpload", fileBody);
    // httpPost.setEntity(reqEntity);
    // HttpResponse response = httpClient.execute(httpPost);
    // HttpEntity resEntity = response.getEntity();

    // if (resEntity != null) {
    //   String responseStr = EntityUtils.toString(resEntity).trim();
    //   if (responseStr.toString().contains("has been uploaded")) {
    //     return 1;
    //   }
    // }
    // return 0;
	  
	  
//	     String postReceiverUrl = "http://falcun.net/v5/share/uploadimage.php?id=" + unique;
//	     HttpClient httpClient = new DefaultHttpClient();
//	     HttpPost httpPost = new HttpPost(postReceiverUrl);
//	     File file = new File(fileLocation);
//	     FileBody fileBody = new FileBody(file);
//	     MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//	     reqEntity.addPart("fileToUpload", fileBody);
//	     httpPost.setEntity(reqEntity);
//	     HttpResponse response = httpClient.execute(httpPost);
//	     HttpEntity resEntity = response.getEntity();
//
//	     if (resEntity != null) {
//	       String responseStr = EntityUtils.toString(resEntity).trim();
//	       if (responseStr.toString().contains("has been uploaded")) {
//	         return 1;
//	       }
//	     }
//	     return 0;
	  

		    String result ="";
		    try {
		      String url = "https://falcun.net/v5/share/uploadimage.php?id=" + unique;
		      HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
		      conn.setRequestMethod("POST");
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
		    } catch (Throwable err) {
		      err.printStackTrace();
		    }
		    
		  if (!result.isEmpty()) {
		    // String responseStr = EntityUtils.toString(resEntity).trim();
		    String responseStr = result.trim();
		    if (responseStr.toString().contains("has been uploaded")) {
		      return 1;
		    }
		  }
		  return 0;

  }

  public static void DownloadImage(String search, String path) {
    InputStream inputStream = null;
    OutputStream outputStream = null;

    try {
      URL url = new URL(search);
      String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
      URLConnection con = url.openConnection();
//      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
//      con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
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
}