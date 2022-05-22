package falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;

import java.io.InputStream;
import java.util.Scanner;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/tweaker/Remapper.java")
public class Remapper {

	BiMap<String, String> classMappings = HashBiMap.create();
	BiMap<String, String> fieldMappings = HashBiMap.create();
	BiMap<String, String> methodMappings = HashBiMap.create();

	public Remapper() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("assets/falcun/tweakers/notch-mcp.srg");
		if (inputStream != null) {
			Scanner scanner = new Scanner(inputStream);
			long now = System.currentTimeMillis();
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("CL")) {
					String[] parts = line.split(" ");
					this.classMappings.put(parts[1], parts[2]);
				} else if (line.startsWith("FD")) {
					String[] parts = line.split(" ");
					this.fieldMappings.put(parts[1], parts[2]);
				} else if (line.startsWith("MD")) {
					String[] parts = line.split(" ");
					this.methodMappings.put(parts[1] + parts[2], parts[3] + parts[4]);
				}
			}
			System.out.println("Took " + (System.currentTimeMillis() - now) + " to read mapping file");
		}
	}

	public String getNotchClass(String mcpClassName) {
		return this.classMappings.inverse().get(mcpClassName);
	}

	public String getNotchMethodName(String mcpClassName, String methodName, String mcpMethodDesc) {
		String name = this.methodMappings.inverse().get(mcpClassName + "/" + methodName + mcpMethodDesc);
		// this is hit when method is not originally from minecraft
		if (name == null) {
			return methodName;
		}
		return name.substring(name.substring(0, name.indexOf("(")).lastIndexOf("/") + 1, name.indexOf("("));
	}
}
