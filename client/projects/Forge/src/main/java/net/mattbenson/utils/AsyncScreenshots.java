package net.mattbenson.utils;

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
import net.mattbenson.modules.types.other.ScreenshotUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class AsyncScreenshots implements Runnable {
	private final int width;
	private final int height;
	private final int[] pixelValues;
	private final Framebuffer framebuffer;
	private final File screenshotDirectory;
	private static BufferedImage image;
	private static File screenshot;

	public AsyncScreenshots(final int width, final int height, final int[] pixelValues,
			final Framebuffer framebuffer, final File screenshotDirectory) {
		this.width = width;
		this.height = height;
		this.pixelValues = pixelValues;
		this.framebuffer = framebuffer;
		this.screenshotDirectory = screenshotDirectory;
	}

	@Override
	public void run() {
		processPixelValues(this.pixelValues, this.width, this.height);
		AsyncScreenshots.screenshot = getTimestampedPNGFileForDirectory(this.screenshotDirectory);

		try {
			if (OpenGlHelper.isFramebufferEnabled()) {
				AsyncScreenshots.image = new BufferedImage(this.framebuffer.framebufferWidth,
						this.framebuffer.framebufferHeight, 1);
				int heightSize;
				for (int tHeight = heightSize = this.framebuffer.framebufferTextureHeight
						- this.framebuffer.framebufferHeight; tHeight < this.framebuffer.framebufferTextureHeight; ++tHeight) {
					for (int widthSize = 0; widthSize < this.framebuffer.framebufferWidth; ++widthSize) {
						AsyncScreenshots.image.setRGB(widthSize, tHeight - heightSize,
								this.pixelValues[tHeight * this.framebuffer.framebufferTextureWidth + widthSize]);
					}
				}
			} else {
				(AsyncScreenshots.image = new BufferedImage(this.width, this.height, 1)).setRGB(0, 0, this.width,
						this.height, this.pixelValues, 0, this.width);
			}
			ImageIO.write(AsyncScreenshots.image, "png", AsyncScreenshots.screenshot);

			String clientID = "70d7a50b633ae56";

			URL url = new URL("https://api.imgur.com/3/image");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(AsyncScreenshots.image, "png", baos);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String data = "";
			data = URLEncoder.encode("image", "UTF-8") + "="
					+ URLEncoder.encode(Base64.getEncoder().encodeToString(baos.toByteArray()), "UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Client-ID " + clientID);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			conn.connect();
			StringBuilder stb = new StringBuilder();
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				stb.append(line).append("\n");
			}
			wr.close();
			rd.close();

			sendChatMessages(stb.toString(), AsyncScreenshots.screenshot);

		} catch (Exception e) {
			e.printStackTrace();
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
					new ChatComponentTranslation("screenshot.failure", new Object[] { e.getMessage() }));
		}
	}

	private void sendChatMessages(final String stb, File file2) throws IOException {
		ScreenshotUploader uploader = Falcun.getInstance().moduleManager.getModule(ScreenshotUploader.class);
		
		if (uploader.isEnabled()) {
			JSONObject json = new JSONObject(stb);
			boolean success = json.getBoolean("success");
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(""));
			IChatComponent toSend = new ChatComponentText("");
			IChatComponent iChatCopy = new ChatComponentText("COPY ");
			IChatComponent iChatLink = new ChatComponentText("LINK ");
			if (success) {
				String link = json.getJSONObject("data").getString("link");
				iChatLink.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.LINK, link));
				iChatLink.getChatStyle().setBold(true);
				iChatLink.getChatStyle().setColor(EnumChatFormatting.GREEN);

				iChatCopy.getChatStyle().setBold(true);
				iChatCopy.getChatStyle().setColor(EnumChatFormatting.YELLOW);
				iChatCopy.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.COPY_URL, link));
			}

			IChatComponent iChatDelete = new ChatComponentText("DELETE ");
			iChatDelete.getChatStyle().setBold(true);
			iChatDelete.getChatStyle().setColor(EnumChatFormatting.RED);
			iChatDelete.getChatStyle()
					.setChatClickEvent(new ClickEvent(ClickEvent.Action.DELETE_FILE, file2.getAbsolutePath()));

			IChatComponent iChatOpen = new ChatComponentText("OPEN ");
			iChatOpen.getChatStyle().setBold(true);
			iChatOpen.getChatStyle().setColor(EnumChatFormatting.BLUE);
			iChatOpen.getChatStyle().setChatClickEvent(
					new ClickEvent(ClickEvent.Action.OPEN_FILE, AsyncScreenshots.screenshot.getCanonicalPath()));
			if (uploader.open) {
				toSend.appendSibling(iChatOpen);
			}
			if (uploader.delete) {
				toSend.appendSibling(iChatDelete);
			}
			if (uploader.copy) {
				toSend.appendSibling(iChatCopy);
			}
			if (uploader.link) {
				toSend.appendSibling(iChatLink);
			}

			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(toSend);
		} else {
			IChatComponent ichatcomponent = new ChatComponentText(file2.getName());
			ichatcomponent.getChatStyle()
					.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
			ichatcomponent.getChatStyle().setUnderlined(Boolean.valueOf(true));
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
					new ChatComponentTranslation("screenshot.success", new Object[] { ichatcomponent }));
		}
	}

	private static void processPixelValues(final int[] pixels, final int displayWidth, final int displayHeight) {
		final int[] xValues = new int[displayWidth];
		for (int yValues = displayHeight / 2, val = 0; val < yValues; ++val) {
			System.arraycopy(pixels, val * displayWidth, xValues, 0, displayWidth);
			System.arraycopy(pixels, (displayHeight - 1 - val) * displayWidth, pixels, val * displayWidth,
					displayWidth);
			System.arraycopy(xValues, 0, pixels, (displayHeight - 1 - val) * displayWidth, displayWidth);
		}
	}

	private static File getTimestampedPNGFileForDirectory(final File gameDirectory) {
		final String dateFormatting = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
		int screenshotCount = 1;
		File screenshot;
		while (true) {
			screenshot = new File(gameDirectory,
					dateFormatting + ((screenshotCount == 1) ? "" : ("_" + screenshotCount)) + ".png");
			if (!screenshot.exists()) {
				break;
			}
			++screenshotCount;
		}
		return screenshot;
	}
}