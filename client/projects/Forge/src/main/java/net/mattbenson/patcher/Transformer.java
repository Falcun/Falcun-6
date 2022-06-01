package net.mattbenson.patcher;

import java.util.regex.Pattern;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public interface Transformer {
	String[] getClassName();

	void transform(ClassNode paramClassNode, String paramString);
	
	default String mapMethodName(ClassNode classNode, MethodNode methodNode) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc);
	}
	
	default String getName() {
		return getClass().getName().replaceAll(Pattern.quote("."), "/");
	}
}