package falcun.xyz.dev.boredhuman.dancore.falcunfork.shader;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.InputStream;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/shaders/ShaderCreater.java")
public class ShaderCreator {

	public static int compileShader(int type, String source) {
		int id = GL20.glCreateShader(type);
		GL20.glShaderSource(id, source);
		GL20.glCompileShader(id);
		int result = GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS);
		if (result == GL11.GL_FALSE) {
			String info = GL20.glGetShaderInfoLog(id, 500);
			System.out.println(info);
			GL20.glDeleteShader(id);
			throw new RuntimeException("Could not create shader");
		}
		return id;
	}

	public static int createShader(String vs, String fs) {
		boolean doVs = vs != null;
		boolean doFs = fs != null;
		int program = GL20.glCreateProgram();
		int vsID = 0;
		if (doVs) {
			vsID = ShaderCreator.compileShader(GL20.GL_VERTEX_SHADER, vs);
			GL20.glAttachShader(program, vsID);
		}
		int fsID = 0;
		if (doFs) {
			fsID = ShaderCreator.compileShader(GL20.GL_FRAGMENT_SHADER, fs);
			GL20.glAttachShader(program, fsID);
		}
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
		if (doVs) {
			GL20.glDeleteShader(vsID);
		}
		if (doFs) {
			GL20.glDeleteShader(fsID);
		}
		return program;
	}

	public static int createShader(ResourceLocation vs, ResourceLocation fs) {
		String vsStr = null;
		String fsStr = null;
		IResourceManager irm = Minecraft.getMinecraft().getResourceManager();
		try {
			if (vs != null) {
				vsStr = ShaderCreator.readResource(irm.getResource(vs).getInputStream());
			}
			if (fs != null) {
				fsStr = ShaderCreator.readResource(irm.getResource(fs).getInputStream());
			}
		} catch (Throwable err) {
			err.printStackTrace();
		}
		return ShaderCreator.createShader(vsStr, fsStr);
	}

	public static String readResource(InputStream stream) {
		try {
			String resource = IOUtils.toString(stream);
			IOUtils.closeQuietly(stream);
			return resource;
		} catch (Throwable err) {
			err.printStackTrace();
			return "";
		}
	}
}