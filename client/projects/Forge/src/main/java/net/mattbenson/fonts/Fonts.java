package net.mattbenson.fonts;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class Fonts {
    public static NahrFont RalewayExtraBold;
    public static NahrFont RalewayExtraBoldSmall;
    public static NahrFont RalewayExtraBoldExtraSmall;
    public static NahrFont RalewayExtraBoldLarge;
    public static NahrFont ubuntuFont;
	public static NahrFont ubuntuMedium;
	public static NahrFont largeBoldFontRenderer;
	public static NahrFont RobotoVeryLarge;
	public static NahrFont KardinalB;
	public static NahrFont KardinalBSmall;
	public static NahrFont KardinalBMed;
	public static NahrFont KardinalBLarge;
    
    public static FalcunFont RobotoHUD;
    public static FalcunFont Roboto;
    public static FalcunFont RobotoSmall;
	public static FalcunFont RobotoMiniHeader;
	public static FalcunFont RobotoHeader;
	public static FalcunFont RobotoTitle;
	public static FalcunFont RobotoItalic;
	public static FalcunFont MC;
	
	public static void setupFonts() {
		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
		
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/Raleway-ExtraBold.ttf")).getInputStream()) {
            RalewayExtraBoldExtraSmall = new NahrFont(is, 12.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/Raleway-ExtraBold.ttf")).getInputStream()) {
            RalewayExtraBoldSmall = new NahrFont(is, 13.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/Raleway-ExtraBold.ttf")).getInputStream()) {
            RalewayExtraBold = new NahrFont(is, 14.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/Raleway-ExtraBold.ttf")).getInputStream()) {
            RalewayExtraBoldLarge = new NahrFont(is, 18.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/ubuntu.ttf")).getInputStream()) {
        	ubuntuMedium = new NahrFont(is, 14.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/Roboto-Bold.ttf")).getInputStream()) {
        	largeBoldFontRenderer = new NahrFont(is, 32.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/Ubuntu-L.ttf")).getInputStream()) {
        	RobotoVeryLarge = new NahrFont(is, 26.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/KardinalB.ttf")).getInputStream()) {
        	KardinalB = new NahrFont(is, 18.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/KardinalB.ttf")).getInputStream()) {
        	KardinalBSmall = new NahrFont(is, 14.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/KardinalB.ttf")).getInputStream()) {
        	KardinalBMed = new NahrFont(is, 20.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/KardinalB.ttf")).getInputStream()) {
        	KardinalBLarge = new NahrFont(is, 26.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try(InputStream is = resourceManager.getResource(new ResourceLocation("textures/fonts/ubuntu.ttf")).getInputStream()) {
        	ubuntuFont = new NahrFont(is, 22.0F);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
		RobotoHUD					= setupFont("Roboto-Bold"			, 24, false);
        
		Roboto						= setupFont("Roboto-Bold"			, 11, true);
		RobotoSmall					= setupFont("Roboto-Bold"			, 8, true);
		
		RobotoMiniHeader			= setupFont("Roboto-Bold"			, 14, true);
		RobotoHeader				= setupFont("Roboto-Bold"			, 22, true);
		RobotoTitle					= setupFont("Roboto-Bold"			, 16, true);
		
		RobotoItalic				= setupFont("Roboto-MediumItalic"	, 12, true);
		
		MC 							= setupFont("Minecraft"				, 14, true);
	}
	
	private static FalcunFont setupFont(String name, int size, boolean normal) {
		try(InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("textures/fonts/" + name + ".ttf")).getInputStream()) {
			return new FalcunFont(is, size, normal);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return new FalcunFont("Arial", size, normal);
	}
}