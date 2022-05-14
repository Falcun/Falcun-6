package falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import org.objectweb.asm.tree.ClassNode;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/api/tweakers/BasicHookModule.java")
public class BasicHookModule extends CorePatchModule {

	public final String methodName;
	public final String desc;
	public final BasicHook.Hook hookType;
	public final String identifier;
	public final String targetClass;

	public BasicHookModule(String targetClass, String methodName, String desc, String identifier, BasicHook.Hook hookType) {
		this.targetClass = targetClass;
		this.methodName = methodName;
		this.desc = desc;
		this.hookType = hookType;
		this.identifier = identifier;
	}

	@Override
	public String getTargetClass() {
		return this.targetClass;
	}

	@Override
	public void transformClass(ClassNode node) {

	}
}
