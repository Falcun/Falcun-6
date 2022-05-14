package net.minecraftforge.fml.common.asm.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;

public class TerminalTransformer implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (basicClass == null) return null;
        ClassReader reader = new ClassReader(basicClass);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        ClassVisitor visitor = writer;
        visitor = new ExitVisitor(visitor);

        reader.accept(visitor, 0);
        return writer.toByteArray();
    }

    public static class ExitVisitor extends ClassVisitor
    {
        private ExitVisitor(ClassVisitor cv)
        {
            super(Opcodes.ASM5, cv);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
        {
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int mAccess, final String mName, final String mDesc, String mSignature, String[] mExceptions)
        {
            return new MethodVisitor(Opcodes.ASM5, super.visitMethod(mAccess, mName, mDesc, mSignature, mExceptions))
            {
                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean isIntf)
                {
                    super.visitMethodInsn(opcode, owner, name, desc, isIntf);
                }
            };
        }
    }
}
