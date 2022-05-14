package net.mattbenson.modules.types.fpssettings.cruches;

import net.mattbenson.Falcun;
import net.mattbenson.modules.types.fpssettings.FPSSettings;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.culling.ICamera;

public class ParticleCulling {
	public static ICamera camera;
	
	public static boolean shouldRender(EntityFX entityFX) {
		if (!Falcun.getInstance().moduleManager.getModule(FPSSettings.class).PARTICLE_CULLING)
			return true;
		if (entityFX == null)
			return false;
		
		return (camera == null || camera.isBoundingBoxInFrustum(entityFX.getEntityBoundingBox()));
		//return (camera == null || entityFX.distanceWalkedModified > -1.0F);
	}
}
