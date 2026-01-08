package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.type.ObjectType
import com.danyazero.type.VoidType
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Opcodes

class Constructor(
    val parameters: List<Parameter> = listOf(),
    val statements: List<Node> = listOf()
) : Node {
    override fun produce(ctx: GenerationContext) {
        val signature = Signature("constructor", parameters)
        val descriptor = signature.getDescriptor(ctx)
        ctx.defineMethod(signature)
        val mv = ctx.getClassWriter().visitMethod(Opcodes.ACC_PUBLIC, "<init>", descriptor, null, null)
        ctx.setMethodVisitor(mv, signature)
        ctx.defineVariable("this", false, ObjectType("java/lang/Object"))

        mv.visitCode()
        ObjectType("java/lang/Object").load(ctx.getMethodVisitor(), 0)
        ObjectNode(Signature("constructor", listOf())).produce(ctx)
        signature.produce(ctx)
        statements.forEach { it.produce(ctx) }
        VoidType().yield(mv)
        mv.visitMaxs(8, 8)
        mv.visitEnd()
    }

    companion object {
        fun default() : Constructor {
            return Constructor(listOf(), listOf())
        }
    }
}