package net.mattbenson.modules.types.general;

import java.awt.Color;
import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.movement.MovementInputEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;

public class ToggleSneak extends Module {
	@ConfigValue.Boolean(name = "Background")
	private boolean backGround = false;
	
	@ConfigValue.Color(name = "Background Color")
	private Color background = new Color(0, 0, 0, 150);
	
	@ConfigValue.Boolean(name = "Toggle Sneak")
	private static boolean optionToggleSneak = true;
	
	@ConfigValue.Boolean(name = "Toggle Sprint")
	public
	static boolean optionToggleSprint = true;
	
	@ConfigValue.Boolean(name = "Double Tap Sprint")
	public
	static boolean optionDoubleTap = false;
	
	@ConfigValue.Boolean(name = "Fly Boost")
	public
	static boolean optionEnableFlyBoost = true;
	
	@ConfigValue.Double(name = "Fly Boost Speed Vertical" , min = 0.1D, max = 20D)
	public
	static double flyboostspeedVertical = 2.0;
	
	@ConfigValue.Double(name = "Fly Boost Speed Horizontal" , min = 0.1D, max = 20D)
	public
	static double flyboostspeedHorizontal = 2.0;
	
	@ConfigValue.Boolean(name = "Custom Font")
	private static boolean customFont = false;
	
	@ConfigValue.Color(name = "Color")
	private Color color = Color.WHITE;
	
	public static boolean isDisabled;
	public static boolean canDoubleTap;

	public static boolean sprint = false;
	public static boolean sprintHeldAndReleased = false;
	public static boolean sprintDoubleTapped = false;

	public static long lastPressed;
	public static long lastSprintPressed;
	public static boolean handledSneakPress;
	public static boolean handledSprintPress;
	public static boolean wasRiding;
	
	public static boolean wasSprintDisabled = false;	
	public static String textForHUD = "";
	
	private HUDElement hud;
	private int width = 20;
	private int height = 10;
	
	public ToggleSneak() {
		super("ToggleSneak", ModuleCategory.MODS);
		hud = new HUDElement("status", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		addHUD(hud);
	}
	
	public static void update(Minecraft mc, MovementInputFromOptions options, EntityPlayerSP thisPlayer)
	{
		options.moveStrafe = 0.0F;
		options.moveForward = 0.0F;

		if(Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown())
		{
			++options.moveForward;
		}

		if(Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown())
		{
			--options.moveForward;
		}

		if(Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown())
		{
			++options.moveStrafe;
		}

		if(Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown())
		{
			--options.moveStrafe;
		}

		options.jump = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();

		//
		// Sneak Toggle - Essentially the same as old ToggleSneak
		//

		// Check to see if Enabled - Added 6/17/14 to provide option to disable Sneak Toggle
		if (optionToggleSneak)
		{
			// Key Pressed
			if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown() && !handledSneakPress)
			{
				// Descend if we are flying, note if we were riding (so we can unsneak once dismounted)
				if(thisPlayer.isRiding() || thisPlayer.capabilities.isFlying)
				{
					options.sneak = true;
					wasRiding = thisPlayer.isRiding();
				}
				else
				{
					options.sneak = !options.sneak;
				}

				lastPressed = System.currentTimeMillis();
				handledSneakPress = true;
			}

			// Key Released
			if (!Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown() && handledSneakPress)
			{
				// If we are flying or riding, stop sneaking after descent/dismount.
				if(thisPlayer.capabilities.isFlying || wasRiding)
				{
					options.sneak = false;
					wasRiding = false;
				}
				// If the key was held down for more than 300ms, stop sneaking upon release.
				else if(System.currentTimeMillis() - lastPressed > 300L)
				{
					options.sneak = false;
				}

				handledSneakPress = false;
			}
		}
		else
		{
			options.sneak = Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown();
		}

		if(options.sneak)
		{
			options.moveStrafe = (float)((double)options.moveStrafe * 0.3D);
			options.moveForward = (float)((double)options.moveForward * 0.3D);
		}
		// Recreated sprinting requirements from minecraft's source code directly, instead of skipping several important checks that can trigger anti-cheats.
		//
		//  Sprint Toggle - Updated 6/18/2014
		//

		// Recreated sprinting requirements from minecraft's source code directly, instead of skipping several important checks that can trigger anti-cheats.
        boolean enoughHunger = (float)thisPlayer.getFoodStats().getFoodLevel() > 6.0F;
        boolean canSprint = !options.sneak && enoughHunger;
        
        
		isDisabled = !optionToggleSprint;
		canDoubleTap = optionDoubleTap;

		// Key Pressed
		if((canSprint || isDisabled) && Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown() && !handledSprintPress)
		{
			if(!isDisabled)
			{
				sprint = !sprint;
				lastSprintPressed = System.currentTimeMillis();
				handledSprintPress = true;
				sprintHeldAndReleased = false;
			}
		}

		// Key Released
		if((canSprint || isDisabled) && !Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown() && handledSprintPress)
		{
			// Was key held for longer than 300ms?  If so, mark it so we can resume vanilla behavior
			if(System.currentTimeMillis() - lastSprintPressed > 300L)
			{
				sprintHeldAndReleased = true;
			}
			handledSprintPress = false;
		}

		UpdateStatus(options, thisPlayer, Minecraft.getMinecraft().gameSettings);
	}


	public static void UpdateSprint(boolean newValue, boolean doubleTapped)
	{
		sprint = newValue;
		sprintDoubleTapped = doubleTapped;
	}

	//
	//  Detect any changes in movement state and update HUD - Added 4/14/2014
	//
	public static void UpdateStatus(MovementInputFromOptions options, EntityPlayerSP thisPlayer, GameSettings settings)
	{

		String output = "";

		boolean isFlying = thisPlayer.capabilities.isFlying;
		boolean isRiding = thisPlayer.isRiding();
		boolean isHoldingSneak = settings.keyBindSneak.isKeyDown();
		boolean isHoldingSprint = settings.keyBindSprint.isKeyDown();

		if(isFlying)
		{
			DecimalFormat numFormat = new DecimalFormat("#.00");
			
			String speedVert = numFormat.format(flyboostspeedVertical);
			String speedHori = numFormat.format(flyboostspeedHorizontal);
			String label = speedVert + "x, " + speedHori + "x";
			
			if(speedVert.equalsIgnoreCase(speedHori)) {
				 label = speedVert + "x";
			}
			
			if (optionEnableFlyBoost && sprint) output += "[Flying (" + label + " boost)]  ";
			else output += "[Flying]  ";
		}
		if(isRiding)	output += "[Riding]  ";

		if (options.sneak)
		{
			if(isFlying)			output += "[Descending]  ";
			else if(isRiding)		output += "[Dismounting]  ";
			else if(isHoldingSneak)	output += "[Sneaking (Key Held)]  ";
			else					output += "[Sneaking (Toggled)]  ";
		}
		else if (sprint)
		{
			if(!isFlying && !isRiding)
			{
				//  Detect Vanilla conditions - ToggleSprint disabled, DoubleTapped and Hold & Release
				boolean isVanilla = sprintHeldAndReleased || isDisabled || sprintDoubleTapped;

				if(isHoldingSprint)		output += "[Sprinting (Key Held)]";
				else if(isVanilla)		output += "[Sprinting (Vanilla)]";
				else					output += "[Sprinting (Toggled)]";
			}
		}
		textForHUD = output;

	}


	public void render() {
		if(!customFont) {
			hud.setWidth(mc.fontRendererObj.getStringWidth(textForHUD) + 1);
			hud.setHeight(mc.fontRendererObj.FONT_HEIGHT + 1);
		} else {
			hud.setWidth(Fonts.RobotoHUD.getStringWidth(textForHUD));
			hud.setHeight(Fonts.RobotoHUD.getStringHeight(textForHUD) + 3);
		}
		
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		
		GL11.glPushMatrix();
		
		if(backGround) {
			DrawUtils.drawGradientRect(hud.getX(), hud.getY(), hud.getX() + hud.getWidth(), hud.getY() + hud.getHeight(), background.getRGB(), background.getRGB());
		}
		if(!this.customFont) {
		

		if(color.getBlue() == 5 && color.getRed() == 5 && color.getGreen() == 5) {
			DrawUtils.drawChromaString(textForHUD, hud.getX() + 1, hud.getY() + 1, true ,true);
		} else if(color.getBlue() == 6 && color.getRed() == 6 && color.getGreen() == 6) {
			DrawUtils.drawChromaString(textForHUD, hud.getX() + 1, hud.getY() + 1, false ,true);
		} else {
			mc.fontRendererObj.drawStringWithShadow(textForHUD, (float) hud.getX() + 1, (float)hud.getY() + 1, color.getRGB());
		}
		} else {
			if(color.getBlue() == 5 && color.getRed() == 5 && color.getGreen() == 5) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD,textForHUD, hud.getX() + 1, hud.getY()+ 1, true ,true);
			} else if(color.getBlue() == 6 && color.getRed() == 6 && color.getGreen() == 6) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD,textForHUD, hud.getX() + 1, hud.getY() + 1, false ,true);
			} else {
				Fonts.RobotoHUD.drawString(textForHUD, hud.getX() + 1, hud.getY() + 1, color.getRGB());
			}
		}
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
	}

}
