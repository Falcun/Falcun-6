package net.mattbenson.modules.types.other;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ScreenshotUploader extends Module {
	@ConfigValue.Boolean(name = "Link")
	public boolean link = true;
	
	@ConfigValue.Boolean(name = "Open")
	public boolean open = true;
	
	@ConfigValue.Boolean(name = "Delete")
	public boolean delete = true;
	
	@ConfigValue.Boolean(name = "Copy")
	public boolean copy = true;
	
	public ScreenshotUploader() {
		super("SS Uploader", ModuleCategory.OTHER);
	}
}
