package falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers;


import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/api/tweakers/BasicHookCustomMatcher.java")
public abstract class BasicHookCustomMatcher extends BasicHookModule{
	public BasicHookCustomMatcher(String targetClass, String methodName, String desc, String identifier) {
		super(targetClass, methodName, desc, identifier, null);
	}

	/**
	 *
	 * @param methodNode the matched method
	 * @param insnList the generated code which needs to be inserted.
	 */
	public abstract void methodAndInjectionCode(MethodNode methodNode, InsnList insnList);
}
