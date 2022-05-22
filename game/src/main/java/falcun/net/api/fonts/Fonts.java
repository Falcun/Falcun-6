package falcun.net.api.fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.InputStream;

public final class Fonts {

	public static FalcunFont RalewayExtraBold;
	public static FalcunFont RalewayExtraBoldSmall;
	public static FalcunFont RalewayExtraBoldExtraSmall;
	public static FalcunFont RalewayExtraBoldLarge;
	public static FalcunFont ubuntuFont;
	public static FalcunFont ubuntuMedium;
	public static FalcunFont largeBoldFontRenderer;
	public static FalcunFont RobotoVeryLarge;
	public static FalcunFont KardinalB;
	public static FalcunFont KardinalBSmall;
	public static FalcunFont KardinalBMed;
	public static FalcunFont KardinalBLarge;

	public static FalcunFont RobotoHUD;
	public static FalcunFont Roboto;
	public static FalcunFont RobotoSmall;
	public static FalcunFont Roboto12;
	public static FalcunFont RobotoMiniHeader;
	public static FalcunFont RobotoHeader;
	public static FalcunFont RobotoTitle;
	public static FalcunFont RobotoItalic;
	public static FalcunFont MC;

	public static FalcunFont panton;
	public static FalcunFont exo2;


	static {
		RalewayExtraBoldExtraSmall = setupFont("Raleway-ExtraBold", 12, false);
		RalewayExtraBoldSmall = setupFont("Raleway-ExtraBold", 13, false);
		RalewayExtraBold = setupFont("Raleway-ExtraBold", 14, false);
		RalewayExtraBoldLarge = setupFont("Raleway-ExtraBold", 18, false);

		largeBoldFontRenderer = setupFont("Roboto-Bold", 32, false);
		RobotoVeryLarge = setupFont("Ubuntu-L", 26, false);

		KardinalB = setupFont("KardinalB", 20, false);
		KardinalBSmall = setupFont("KardinalB", 14, false);
		KardinalBMed = setupFont("KardinalB", 20, false);

		KardinalBLarge = setupFont("KardinalB", 26, false);

		ubuntuMedium = setupFont("ubuntu", 14, false);
		ubuntuFont = setupFont("ubuntu", 22, false);

		RobotoHUD = setupFont("Roboto-Bold", 24, false);

		Roboto = setupFont("Roboto-Bold", 11, true);
		Roboto12 = setupFont("Roboto-Bold", 12, true);
		RobotoSmall = setupFont("Roboto-Bold", 8, true);

		RobotoMiniHeader = setupFont("Roboto-Bold", 14, true);
		RobotoHeader = setupFont("Roboto-Bold", 22, true);
		RobotoTitle = setupFont("Roboto-Bold", 16, true);

		RobotoItalic = setupFont("Roboto-MediumItalic", 12, true);

		MC = setupFont("Minecraft", 14, true);

		panton = setupFont("Panton-LightitalicCaps", 16, false);
		exo2 = setupFont("Exo2-Regular", 16, false);
	}

	private static FontV2 setupFont(String name, int size, boolean normal) {
		try (InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("falcun:fonts/" + name + ".ttf")).getInputStream()) {
			return new FontV2(is, size, normal);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new FontV2("Arial", size, normal);
	}
}
