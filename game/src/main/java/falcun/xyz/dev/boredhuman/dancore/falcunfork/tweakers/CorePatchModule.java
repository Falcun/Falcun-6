package falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import org.objectweb.asm.tree.ClassNode;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/api/tweakers/CorePatchModule.java")
public abstract class CorePatchModule {
	public abstract String getTargetClass();

	public abstract void transformClass(ClassNode node);

	public void postRegistration() {

	}
}