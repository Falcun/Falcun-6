package mchorse.emoticons.utils.resources;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Stack;

public class MultiskinThread implements Runnable
{
	private static MultiskinThread instance;
	private static Thread thread;

	public Stack<MultiResourceLocation> locations = new Stack<MultiResourceLocation>();

	public static synchronized void add(MultiResourceLocation location)
	{
		if (instance != null && !thread.isAlive())
		{
			instance = null;
		}

		if (instance == null)
		{
			instance = new MultiskinThread();
			instance.locations.add(location);
			thread = new Thread(instance);
			thread.start();
		}
		else
		{
			instance.locations.add(location);
		}
	}

	public static void clear()
	{
		instance = null;
	}

	/**
	 * Create a byte buffer from buffered image
	 */
	public static ByteBuffer bytesFromBuffer(BufferedImage image)
	{
		int w = image.getWidth();
		int h = image.getHeight();

		ByteBuffer buffer = GLAllocation.createDirectByteBuffer(w * h * 4);
		int[] pixels = new int[w * h];

		image.getRGB(0, 0, w, h, pixels, 0, w);

		for (int y = 0; y < h; y++)
		{
			for (int x = 0; x < w; x++)
			{
				int pixel = pixels[y * w + x];

				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}

		buffer.flip();

		return buffer;
	}

	@Override
	public void run()
	{
		while (!this.locations.isEmpty() && instance != null)
		{
			MultiResourceLocation location = this.locations.pop();
			final ITextureObject texture = Minecraft.getMinecraft().renderEngine.getTexture(location);

			if (texture != null)
			{
				try
				{
					BufferedImage image = TextureProcessor.process(location);
					final int w = image.getWidth();
					final int h = image.getHeight();
					final ByteBuffer buffer = bytesFromBuffer(image);

					Minecraft.getMinecraft().addScheduledTask(new Runnable() {

						@Override
						public void run() {
							TextureUtil.allocateTexture(texture.getGlTextureId(), w, h);

							GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlTextureId());
							GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
							GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
							GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
							
						}
					}
					);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				this.locations.add(location);
			}
		}

		instance = null;
		thread = null;
	}
}