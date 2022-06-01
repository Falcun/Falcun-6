package net.mattbenson.cosmetics.cape;

import net.minecraft.client.entity.AbstractClientPlayer;

public class CapeData {
	public AbstractClientPlayer player;
	public long time;
	
	public CapeData(AbstractClientPlayer player) {
		this.player = player;
		this.time = System.currentTimeMillis();
	}

	public float getCapeWavePhase()
	{
		boolean moving = Math.abs(player.motionX) > 0 || Math.abs(player.motionZ) > 0;
		long speed = 1000; //Dont touch.
		
		if(moving) {
			if(player.isSprinting()) {
				speed *= 5; //10 times slower animations.
			} else if(player.isSneaking()) {
				speed *= 35; //35 times slower animations
			} else { //Normal walking
				speed *= 7; //7 times slower animations
			}
		} else {
			speed *= 25; //10 times slower animation
		}
		
		long curtime = System.currentTimeMillis();
		
		if(time < curtime) {
			this.time = curtime + speed;	
		}
		
		float delta = time - curtime;
		return (delta / speed) * 380F;
	}

	public AbstractClientPlayer getClientPlayer() {
		return player;
	}
}
