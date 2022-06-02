package falcun.net.api.textures;

import net.minecraft.util.ResourceLocation;

public final class FalcunTexture {
	public static final ResourceLocation glowLogo = new ResourceLocation("falcun:logos/f_outline_glow.png");
	public static final ResourceLocation[] loadingAnimation = new ResourceLocation[112];
	public static final ResourceLocation[] logoAnimation = new ResourceLocation[60];

	public static final ResourceLocation[] moduleBoxToggle = new ResourceLocation[4];

	public static final ResourceLocation discordIcon = new ResourceLocation("falcun:gui/discord.png");
	public static void setupArrays(){
		for (int i = 0; i < 112; i++) {
			loadingAnimation[i] = new ResourceLocation("falcun:animated/loading/falcun_layer_" + (i) + ".png");
		}
		for (int i = 0; i < 60; i++) {
			logoAnimation[i] = new ResourceLocation("falcun:animated/logo/falcun-logo-" + (i + 1) + ".png");
		}
		moduleBoxToggle[0]= new ResourceLocation("falcun:gui/modules/disabled 1.png");
		moduleBoxToggle[1]= new ResourceLocation("falcun:gui/modules/disabled hovered 1.png");
		moduleBoxToggle[2]= new ResourceLocation("falcun:gui/modules/enabled 1.png");
		moduleBoxToggle[3]= new ResourceLocation("falcun:gui/modules/enabled hovered 1.png");
	}
}
