package falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/tweaker/BasicHook.java")
public class BasicHook implements IClassTransformer {

	public static Map<String, List<BasicHookModule>> targets = new HashMap<>();
	public static Map<String, Integer> stringIntegerMap = new HashMap<>();
	public static int id = 0;
	public static Remapper remapper = new Remapper();
	private static final boolean RECALC_FRAMES = Boolean.parseBoolean(System.getProperty("FORGE_FORCE_FRAME_RECALC", "false"));
	public static int WRITER_FLAGS = ClassWriter.COMPUTE_MAXS | (RECALC_FRAMES ? ClassWriter.COMPUTE_FRAMES : 0);
	private static final int READER_FLAGS = RECALC_FRAMES ? ClassReader.SKIP_FRAMES : ClassReader.EXPAND_FRAMES;

	public static void register(BasicHookModule basicHook) {
		List<BasicHookModule> patches = BasicHook.targets.computeIfAbsent(basicHook.targetClass, name -> new ArrayList<>());
		patches.add(basicHook);
		int id = BasicHook.id++;
		LogManager.getLogger().info("Registering: " + basicHook.getTargetClass() + " id: " + id);
		BasicHook.stringIntegerMap.put(basicHook.identifier, id);
		basicHook.postRegistration();
	}

	public static Function<Object[], ReturnStrategy>[] functions = new Function[512];

	public static void addFunction(Function<Object[], ReturnStrategy> toPerform, String id) {
		LogManager.getLogger().info("Adding function: " + toPerform.getClass().getName() + " String id: " + id + " n# id: " + BasicHook.stringIntegerMap.get(id));
		Integer index = BasicHook.stringIntegerMap.get(id);
		if (index != null && index >= 0 && index < BasicHook.functions.length) {
			BasicHook.functions[index] = toPerform;
		}
	}

	public static Function<Object[], ReturnStrategy> functionSupplier(int i) {
		Function func = BasicHook.functions[i];
//		if (func == null) {
//			LogManager.getLogger().info("Returning null function " + i);
//		}
		return func;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (BasicHook.targets.containsKey(transformedName)) {
			try {
				System.out.println("BasicHook Transforming: " + name);
				ClassNode cn = new ClassNode();
				ClassReader cr = new ClassReader(basicClass);
				cr.accept(cn, READER_FLAGS);
				List<BasicHookModule> hooks = BasicHook.targets.get(transformedName);
				for (BasicHookModule hook : hooks) {
					this.transform(cn, hook);
				}
				ClassWriter cw = new ClassWriter(WRITER_FLAGS);
				cn.accept(cw);

				CheckClassAdapter.verify(cr, false, new PrintWriter(System.out));
				return cw.toByteArray();
			} catch (Throwable err) {
				err.printStackTrace();
			}
		}
		return basicClass;
	}

	public void transform(ClassNode cn, BasicHookModule hookData) {
		String methodDesc = BasicHook.getMethodDesc(hookData.desc);
		String methodName = BasicHook.remapper.getNotchMethodName(hookData.targetClass.replace(".", "/"), hookData.methodName, hookData.desc);
		Hook hook = hookData.hookType;
		for (MethodNode methodNode : cn.methods) {
			if ((!methodNode.desc.equals(methodDesc) || !methodName.equals(methodNode.name)) && (!methodNode.desc.equals(hookData.desc) || !methodNode.name.equals(hookData.methodName))) {
				continue;
			}

			InsnList list = this.createInstructionList(methodNode, hookData);

			if (hookData instanceof BasicHookCustomMatcher) {
				((BasicHookCustomMatcher) hookData).methodAndInjectionCode(methodNode, list);
			} else {
				switch (hook) {
					case HEAD:
						methodNode.instructions.insert(list);
						break;
					case TAIL:
						methodNode.instructions.insertBefore(this.getLastReturn(methodNode), list);
						break;
					case BEFORERETURNS:
						List<AbstractInsnNode> allRets = this.getAllReturns(methodNode);
						for (AbstractInsnNode returnNode : allRets) {
							methodNode.instructions.insertBefore(returnNode, list);
						}
						break;
					default:
						break;
				}
			}
		}
	}

	public static String getMethodDesc(String desc) {
		StringBuilder sb = new StringBuilder();
		Type[] argumentTypes = ArrayUtils.add(Type.getArgumentTypes(desc), Type.getReturnType(desc));;
		for (int i = 0; i < argumentTypes.length; i++) {
			if (i == 0) {
				sb.append("(");
			} else if (i == argumentTypes.length - 1) {
				sb.append(")");
			}
			Type type = argumentTypes[i];
			if (type.getSort() == Type.OBJECT) {
				String className = type.getClassName();
				String qualified = className.replace(".", "/");
				sb.append("L");
				if (qualified.startsWith("net/minecraft")) {
					sb.append(BasicHook.remapper.getNotchClass(qualified));
				} else {
					sb.append(qualified);
				}
				sb.append(";");
			} else {
				sb.append(type.getDescriptor());
			}
		}
		return sb.toString();
	}

	public InsnList createInstructionList(MethodNode methodNode, BasicHookModule hookData) {
		// Function function = BasicHook.functionSupplier(id);
		// if (function != null) {
		//     Object[] mehodargsSelf = new Object[]{this, methodargs}
		//     ReturnStrategy returnStrategy = function.apply(methodargsSelf);
		//     if (returnStrategy != DONOTHING) {
		//         return returnStrategy.returnObject;
		//     }
		// }
		// pop function or returnstrategy from stack
		Type returnType = Type.getReturnType(methodNode.desc);
		InsnList list = new InsnList();
		LabelNode startLabel = new LabelNode();
		list.add(startLabel);
		list.add(BasicHook.getInstructionForSize(this.stringIntegerMap.get(hookData.identifier)));
		list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(BasicHook.class), "functionSupplier",
			"(I)Ljava/util/function/Function;", false));
		list.add(new InsnNode(Opcodes.DUP));
		LabelNode endLabel = new LabelNode();
		list.add(new JumpInsnNode(Opcodes.IFNULL, endLabel));
		list.add(this.generateParamArray(methodNode));
		list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, Type.getInternalName(Function.class), "apply",
			"(Ljava/lang/Object;)Ljava/lang/Object;", true));
		list.add(new TypeInsnNode(Opcodes.CHECKCAST, "falcun/xyz/dev/boredhuman/dancore/falcunfork/tweakers/ReturnStrategy"));
		list.add(new InsnNode(Opcodes.DUP));
		list.add(new FieldInsnNode(Opcodes.GETFIELD, "falcun/xyz/dev/boredhuman/dancore/falcunfork/tweakers/ReturnStrategy", "action", "Ldev/boredhuman/dancore/falcunfork/tweakers/ReturnStrategy$Action;"));
		list.add(new FieldInsnNode(Opcodes.GETSTATIC, "falcun/xyz/dev/boredhuman/dancore/falcunfork/tweakers/ReturnStrategy$Action", "DONOTHING", "Ldev/boredhuman/dancore/falcunfork/tweakers/ReturnStrategy$Action;"));
		list.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, endLabel));
		list.add(new FieldInsnNode(Opcodes.GETFIELD, "falcun/xyz/dev/boredhuman/dancore/falcunfork/tweakers/ReturnStrategy", "returnObject", "Ljava/lang/Object;"));
		list.add(BasicHook.getCastInvoke(returnType));
		list.add(new InsnNode(this.getLastReturn(methodNode).getOpcode()));
		list.add(endLabel);
		InsnNode pop = new InsnNode(Opcodes.POP);
		list.add(pop);
		return list;
	}

	/**
	 * @param methodNode method to steal from
	 * @return creates a list of instructions to copy the args to a object array contain "this" if the method is not static
	 */
	public InsnList generateParamArray(MethodNode methodNode) {
		InsnList makeArrayList = new InsnList();
		boolean isStatic = Modifier.isStatic(methodNode.access);
		Type[] args = Type.getArgumentTypes(methodNode.desc);
		int start = isStatic ? 0 : 1;
		makeArrayList.add(BasicHook.getInstructionForSize(args.length + start));
		makeArrayList.add(new TypeInsnNode(Opcodes.ANEWARRAY, "java/lang/Object"));
		if (!isStatic) {
			makeArrayList.add(new InsnNode(Opcodes.DUP));
			makeArrayList.add(BasicHook.getInstructionForSize(0));
			makeArrayList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			makeArrayList.add(new InsnNode(Opcodes.AASTORE));
		}
		int index = start;
		for (Type type : args) {
			makeArrayList.add(new InsnNode(Opcodes.DUP));
			makeArrayList.add(BasicHook.getInstructionForSize(index));
			index++;
			int opcode = 0;
			int extra = 0;
			if (type == Type.DOUBLE_TYPE) {
				opcode = Opcodes.DLOAD;
				extra = 1;
			} else if (type == Type.LONG_TYPE) {
				opcode = Opcodes.LLOAD;
				extra = 1;
			} else if (type == Type.INT_TYPE) {
				opcode = Opcodes.ILOAD;
			} else if (type == Type.SHORT_TYPE) {
				opcode = Opcodes.ILOAD;
			} else if (type == Type.CHAR_TYPE) {
				opcode = Opcodes.ILOAD;
			} else if (type == Type.BYTE_TYPE) {
				opcode = Opcodes.ILOAD;
			} else if (type == Type.FLOAT_TYPE) {
				opcode = Opcodes.FLOAD;
			} else if (type == Type.BOOLEAN_TYPE) {
				opcode = Opcodes.ILOAD;
			} else if (type.getSort() == Type.OBJECT) {
				opcode = Opcodes.ALOAD;
			}
			makeArrayList.add(new VarInsnNode(opcode, start));
			if (opcode != Opcodes.ALOAD) {
				makeArrayList.add(BasicHook.convertPrimitiveToWrappedType(type));
			}
			start += 1 + extra;
			makeArrayList.add(new InsnNode(Opcodes.AASTORE));
		}
		return makeArrayList;
	}

	/**
	 * @param method for querying
	 * @return returns the last return opcode
	 */
	public AbstractInsnNode getLastReturn(MethodNode method) {
		AbstractInsnNode[] instructions = method.instructions.toArray();
		for (int i = instructions.length - 1; i > 0; i--) {
			AbstractInsnNode instruction = instructions[i];
			int opcode = instruction.getOpcode();
			if (opcode > 171 && opcode < 178) {
				return instruction;
			}
		}
		return null;
	}

	/**
	 * @param returnType the primitive type
	 * @return a list of instructions which converts a wrapped primitive to its primitive i.e {@link java.lang.Boolean} to primitive boolean, if not a wrapped primitive performs type check i.e instanceof
	 */
	public static InsnList getCastInvoke(Type returnType) {
		InsnList list = new InsnList();
		int sort = returnType.getSort();
		String cast = null;
		String methodName = null;
		String signature = null;
		switch (sort) {
			case Type.BOOLEAN:
				cast = "java/lang/Boolean";
				methodName = "booleanValue";
				signature = "()Z";
				break;
			case Type.INT:
				cast = "java/lang/Integer";
				methodName = "intValue";
				signature = "()I";
				break;
			case Type.SHORT:
				cast = "java/lang/Short";
				methodName = "shortValue";
				signature = "()S";
				break;
			case Type.DOUBLE:
				cast = "java/lang/Double";
				methodName = "doubleValue";
				signature = "()D";
				break;
			case Type.CHAR:
				cast = "java/lang/Character";
				methodName = "charValue";
				signature = "()C";
				break;
			case Type.BYTE:
				cast = "java/lang/Byte";
				methodName = "byteValue";
				signature = "()B";
				break;
			case Type.FLOAT:
				cast = "java/lang/Float";
				methodName = "floatValue";
				signature = "()F";
				break;
			case Type.OBJECT:
				cast = returnType.getClassName().replace(".", "/");
				break;
			case Type.LONG:
				cast = "java/lang/Long";
				methodName = "longValue";
				signature = "()J";
				break;
		}
		if (cast != null) {
			TypeInsnNode castCheck = new TypeInsnNode(Opcodes.CHECKCAST, cast);
			list.add(castCheck);
			if (methodName == null) {
				return list;
			} else {
				MethodInsnNode methodCall = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, cast, methodName, signature,
					false);
				list.add(methodCall);
			}
		} else {
			list.add(new InsnNode(Opcodes.POP));
		}
		return list;
	}

	public List<AbstractInsnNode> getAllReturns(MethodNode method) {
		List<AbstractInsnNode> instructions = new ArrayList<>();
		method.instructions.iterator().forEachRemaining(instruction -> {
			int opcode = instruction.getOpcode();
			if (opcode > 171 && opcode < 178) {
				instructions.add(instruction);
			}
		});
		return instructions;
	}

	/**
	 * @param size the integer to be pushed onto the stack
	 * @return the appropriate opcode for the integer
	 */
	public static int getOpcodeForSize(int size) {
		switch (size) {
			case 0:
				return Opcodes.ICONST_0;
			case 1:
				return Opcodes.ICONST_1;
			case 2:
				return Opcodes.ICONST_2;
			case 3:
				return Opcodes.ICONST_3;
			case 4:
				return Opcodes.ICONST_4;
			case 5:
				return Opcodes.ICONST_5;
		}
		if (size < 128) {
			return Opcodes.BIPUSH;
		} else {
			return Opcodes.SIPUSH;
		}
	}

	/**
	 * @param size the size of the integer to be pushed on to the stack
	 * @return the appropriate instruction node for the integer
	 */
	public static AbstractInsnNode getInstructionForSize(int size) {
		if (size < 6) {
			return new InsnNode(BasicHook.getOpcodeForSize(size));
		} else {
			return new IntInsnNode(BasicHook.getOpcodeForSize(size), size);
		}
	}

	public static AbstractInsnNode convertPrimitiveToWrappedType(Type storeType) {
		String klass;
		String signature;
		if (storeType == Type.FLOAT_TYPE) {
			klass = "java/lang/Float";
			signature = "(F)";
		} else if (storeType == Type.BOOLEAN_TYPE) {
			klass = "java/lang/Boolean";
			signature = "(Z)";
		} else if (storeType == Type.DOUBLE_TYPE) {
			klass = "java/lang/Double";
			signature = "(D)";
		} else if (storeType == Type.LONG_TYPE) {
			klass = "java/lang/Long";
			signature = "(J)";
		} else {
			klass = "java/lang/Integer";
			signature = "(I)";
		}
		signature += "L" + klass + ";";
		return new MethodInsnNode(Opcodes.INVOKESTATIC, klass, "valueOf", signature, false);
	}

	public enum Hook {
		HEAD, TAIL, BEFORERETURNS, CUSTOM;
	}

}
