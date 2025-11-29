package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.util.function.Consumer

class Class(
    val name: String,
    val members: List<Node>,
) : Node {

    override fun produce(ctx: GenerationContext) {
        ctx.getClassWriter().visit(Opcodes.V17, Opcodes.ACC_PUBLIC, name, null, "java/lang/Object", null)


        val mv = ctx.getClassWriter().visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)

        val nextScope = GenerationContext(classWriter = ctx.getClassWriter(), methodVisitor = mv)
        produceDefaultClassConstructor(nextScope.getMethodVisitor())

        members.forEach(Consumer { m: Node -> m.produce(ctx) })
        ctx.getClassWriter().visitEnd()
    }

    private fun produceDefaultClassConstructor(mv: MethodVisitor) {
        mv.visitCode()
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(Opcodes.RETURN)
        mv.visitMaxs(1, 1)
        mv.visitEnd()
    }

    override fun toString(): String {
        return "Class(name='$name', members=$members)"
    }
}