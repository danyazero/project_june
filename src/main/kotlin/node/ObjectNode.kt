package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Opcodes

class ObjectNode(
    val signature: Signature
) : Node{
    override fun produce(ctx: GenerationContext) {
        ctx.getMethodVisitor().visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/lang/Object",
            "<init>",
            signature.getDescriptor(ctx),
            false
        )
    }
}