package net.mattbenson.events.types.world;

import java.util.ArrayList;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.events.Event;
import net.mattbenson.modules.types.factions.Patchcrumbs;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.BlockPos;

import net.mattbenson.modules.types.fpssettings.FPSSettings;

//Hooked at net.minecraft.client.Minecraft.java
public class OnTickEvent extends Event {

  public OnTickEvent() {
      
    if (FPSSettings.noLagStackTNT) {
 
      if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft() != null) {
        if (Falcun.getInstance().moduleManager.getModule(Patchcrumbs.class).isEnabled() && !Falcun.getInstance().moduleManager.getModule(Patchcrumbs.class).mergeTNT) return;

         List<BlockPos> allTNT = new ArrayList<>();
         List<BlockPos> allSand = new ArrayList<>();
         for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList){
             if (o instanceof EntityTNTPrimed){
                 EntityTNTPrimed etp = (EntityTNTPrimed) o;
                 if (allTNT.contains(etp.getPosition())){
                     Minecraft.getMinecraft().theWorld.removeEntity(etp);
                 } else {
                     allTNT.add(etp.getPosition());
                 }
             }
        	
             if (o instanceof EntityFallingBlock){
            	 EntityFallingBlock etp = (EntityFallingBlock) o;
                 if (allSand.contains(etp.getPosition())){
                     Minecraft.getMinecraft().theWorld.removeEntity(etp);
                 } else {
                	 allSand.add(etp.getPosition());
                 }
             } 	
         }
        }
    }
   
        
    Minecraft.getMinecraft().enableGLErrorChecking = !FPSSettings.DISABLE_GL_ERROR_CHECKING;
  }
}
        
        
