package net.mattbenson.patcher.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.mattbenson.patcher.Transformer;

public class ModelRendererTransformer implements Transformer {
	public static boolean batchModelRendering;
	
	public String[] getClassName() {
		return new String[] { "net.minecraft.client.model.ModelRenderer" };
	}

	public void transform(ClassNode classNode, String name) {
		classNode.fields.add(new FieldNode(1, "patcherCompiledState", "Z", null, null));
		
		for (MethodNode methodNode : classNode.methods) {
			String methodName = mapMethodName(classNode, methodNode);
			
			if (methodName.equals("render") || methodName.equals("func_78785_a")) {
				methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), renderStart());
				continue;
			}
			
			if (methodName.equals("compileDisplayList") || methodName.equals("func_78788_d")) {
				ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
				
				while (iterator.hasNext()) {
					AbstractInsnNode next = iterator.next();
					
					if (next instanceof VarInsnNode) {
						if (next.getOpcode() == 58 && ((VarInsnNode) next).var == 2) {
							methodNode.instructions.insert(next, getWorldRendererBegin());
						}
						continue;
					}
					
					if (next instanceof MethodInsnNode && ((MethodInsnNode) next).name.equals("glEndList")) {
						methodNode.instructions.insertBefore(next, getWorldRendererEnd());
					}
				}
			}
		}
	}

	public InsnList getWorldRendererEnd() {
		InsnList list = new InsnList();
		list.add((AbstractInsnNode) new FieldInsnNode(178, getName(), "batchModelRendering", "Z"));
		LabelNode ifeq = new LabelNode();
		list.add((AbstractInsnNode) new JumpInsnNode(153, ifeq));
		list.add((AbstractInsnNode) new MethodInsnNode(184, "net/minecraft/client/renderer/Tessellator", "func_178181_a", "()Lnet/minecraft/client/renderer/Tessellator;", false));
		list.add((AbstractInsnNode) new MethodInsnNode(182, "net/minecraft/client/renderer/Tessellator", "func_78381_a", "()V", false));
		list.add((AbstractInsnNode) ifeq);
		return list;
	}

	private InsnList getWorldRendererBegin() {
		InsnList list = new InsnList();
		list.add((AbstractInsnNode) new VarInsnNode(25, 0));
		list.add((AbstractInsnNode) new FieldInsnNode(178, getName(), "batchModelRendering", "Z"));
		list.add((AbstractInsnNode) new FieldInsnNode(181, "net/minecraft/client/model/ModelRenderer", "patcherCompiledState", "Z"));
		list.add((AbstractInsnNode) new FieldInsnNode(178, getName(), "batchModelRendering", "Z"));
		LabelNode ifeq = new LabelNode();
		list.add((AbstractInsnNode) new JumpInsnNode(153, ifeq));
		list.add((AbstractInsnNode) new VarInsnNode(25, 2));
		list.add((AbstractInsnNode) new IntInsnNode(16, 7));
		list.add((AbstractInsnNode) new FieldInsnNode(178, "net/minecraft/client/renderer/vertex/DefaultVertexFormats", "field_181703_c", "Lnet/minecraft/client/renderer/vertex/VertexFormat;"));
		list.add((AbstractInsnNode) new MethodInsnNode(182, "net/minecraft/client/renderer/WorldRenderer", "func_181668_a", "(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V", false));
		list.add((AbstractInsnNode) ifeq);
		return list;
	}

	private InsnList renderStart() {
		InsnList list = new InsnList();
		list.add((AbstractInsnNode) new VarInsnNode(25, 0));
		list.add((AbstractInsnNode) new FieldInsnNode(180, "net/minecraft/client/model/ModelRenderer", "patcherCompiledState", "Z"));
		list.add((AbstractInsnNode) new FieldInsnNode(178, getName(), "batchModelRendering", "Z"));
		LabelNode ificmpeq = new LabelNode();
		list.add((AbstractInsnNode) new JumpInsnNode(159, ificmpeq));
		list.add((AbstractInsnNode) new VarInsnNode(25, 0));
		list.add((AbstractInsnNode) new InsnNode(3));
		list.add((AbstractInsnNode) new FieldInsnNode(181, "net/minecraft/client/model/ModelRenderer", "field_78812_q", "Z"));
		list.add((AbstractInsnNode) ificmpeq);
		return list;
	}
}