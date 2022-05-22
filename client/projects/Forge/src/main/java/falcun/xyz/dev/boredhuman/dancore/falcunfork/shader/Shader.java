package falcun.xyz.dev.boredhuman.dancore.falcunfork.shader;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;
import java.util.Map;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/shaders/Shader.java")
public class Shader {
	int programID;
	Map<String, Integer> uniforms = new HashMap<>();

	public Shader(int programID, String... uniformNames) {
		this.programID = programID;
		for (String name : uniformNames) {
			int location = GL20.glGetUniformLocation(programID, name);
			this.uniforms.put(name, location);
		}
	}

	public int getProgramID() {
		return this.programID;
	}

	public int getUniformID(String name) {
		Integer uniformID = this.uniforms.get(name);
		if (uniformID == null || uniformID == -1) {
			System.out.println("Tried to get an invalid uniform " + name);
			return -1;
		}
		return uniformID;
	}
}