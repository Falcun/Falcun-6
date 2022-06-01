package net.mattbenson.patcher;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.mattbenson.patcher.transformers.ModelRendererTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
	private final Multimap<String, Transformer> transformerMap = ArrayListMultimap.<String, Transformer>create();
	private final Logger logger = LogManager.getLogger("Falcun");

	public ClassTransformer() {
		registerTransformer(new ModelRendererTransformer());
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		return createTransformer(transformedName, bytes, this.transformerMap, this.logger);
	}

	private void registerTransformer(Transformer transformer) {
		for(String cls : transformer.getClassName()) {
			this.transformerMap.put(cls, transformer);
		}
	}

	public static byte[] createTransformer(String transformedName, byte[] bytes, Multimap<String, Transformer> transformerMap, Logger logger) {
		if(bytes == null) {
			return null;
		}
				
		Collection<Transformer> transformers = transformerMap.get(transformedName);
		
		if(transformers.isEmpty()) {
			return bytes;
		}
		
		ClassReader classReader = new ClassReader(bytes);
		ClassNode classNode = new ClassNode();
		classReader.accept((ClassVisitor) classNode, 8);
		
		for(Transformer transformer : transformers) {
			transformer.transform(classNode, transformedName);
		}
		
		ClassWriter classWriter = new ClassWriter(2);
		
		try {
			classNode.accept(classWriter);
		} catch (Throwable e) {
			logger.error("Exception when transforming {} : {}", new Object[] { transformedName, e.getClass().getSimpleName(), e });
		}
		
		return classWriter.toByteArray();
	}
}