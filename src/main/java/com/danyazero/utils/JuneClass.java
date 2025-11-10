package com.danyazero.utils;

import com.danyazero.model.ast.Node;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;

public class JuneClass implements Node {

    private final String name;
    private final List<Node> members;

    public JuneClass(String name, List<Node> members) {
        this.name = name;
        this.members = members;
    }

    @Override
    public void produce(GenerationContext ctx) {

        ctx.getClassWriter().visit(V17, ACC_PUBLIC, name, null, "java/lang/Object", null);


        MethodVisitor mv = ctx.getClassWriter().visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        var nextScope = new GenerationContext(ctx, mv);
        produceDefaultClassConstructor(nextScope.getMethodVisitor());
        members.forEach(m -> m.produce(ctx));
        ctx.getClassWriter().visitEnd();
    }

    private void produceDefaultClassConstructor(MethodVisitor mv) {
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
}
