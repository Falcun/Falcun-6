package falcun.xyz.dev.boredhuman.dancore.falcunfork.shader;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import net.minecraft.util.ResourceLocation;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/shaders/Shaders.java")
public class Shaders {

	private static Shader smoothCircleShader = null;
	private static Shader roundedQuadShader = null;
	private static Shader blurShader = null;
	private static Shader roundedQuadOutlineShader = null;
	private static Shader circleOutlineShader = null;

	private static Shader neonStreamShader = null;

	public static void createAll() {
		Shaders.getRoundedQuadShader();
		Shaders.getSmoothCircleShader();
		Shaders.getBlurShader();
	}

	public static Shader getSmoothCircleShader() {
		if (Shaders.smoothCircleShader == null) {
			Shaders.smoothCircleShader = new Shader(
				ShaderCreator.createShader(null, new ResourceLocation("falcun:shaders/circle.glsl")),
				"center", "radius", "feather", "color");
		}
		return Shaders.smoothCircleShader;
	}

	public static Shader getRoundedQuadShader() {
		if (Shaders.roundedQuadShader == null) {
			Shaders.roundedQuadShader = new Shader(
				ShaderCreator.createShader(null, new ResourceLocation("falcun:shaders/roundedQuad.glsl")),
				"curve", "area", "color", "feather");
		}
		return Shaders.roundedQuadShader;
	}

	public static Shader getBlurShader() {
		if (Shaders.blurShader == null) {
			Shaders.blurShader = new Shader(
				ShaderCreator.createShader(null, new ResourceLocation("falcun:shaders/gaussian-blur.glsl")),
				"tex", "resolution", "direction"
			);
		}
		return Shaders.blurShader;
	}

	public static Shader getRoundedQuadOutlineShader() {
		if (Shaders.roundedQuadOutlineShader == null) {
			Shaders.roundedQuadOutlineShader = new Shader(
				ShaderCreator.createShader(null, new ResourceLocation("falcun:shaders/roundedQuadOutline.glsl")),
				"curve", "area", "color", "feather", "lineWidth"
			);
		}
		return Shaders.roundedQuadOutlineShader;
	}

	public static Shader getCircleOutlineShader() {
		if (Shaders.circleOutlineShader == null) {
			Shaders.circleOutlineShader = new Shader(
				ShaderCreator.createShader(null, new ResourceLocation("falcun:shaders/circleoutlinefs.glsl")),
				"center", "radius", "feather", "color", "lineWidth"
			);
		}
		return Shaders.circleOutlineShader;
	}

	public static Shader getNeonStreamShader() {
		if (Shaders.neonStreamShader == null) {
			Shaders.neonStreamShader = new Shader(
				ShaderCreator.createShader(null, new ResourceLocation("falcun:shaders/neonstream.glsl")),
				"resolution"
			);
		}
		return Shaders.circleOutlineShader;
	}

}