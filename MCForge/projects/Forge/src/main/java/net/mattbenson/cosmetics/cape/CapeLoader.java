package net.mattbenson.cosmetics.cape;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import net.mattbenson.Falcun;
import net.minecraft.client.renderer.texture.TextureUtil;

public class CapeLoader extends Thread {
	private Cape cape;
	
	public CapeLoader(Cape cape) {
		this.cape = cape;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < cape.getTotal(); i++) {
			try {
				BufferedImage image = TextureUtil.readBufferedImage(new FileInputStream(cape.getFramePath(i)));
				cape.setPreload(i, image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		cape.setReady(true);
	}
}
