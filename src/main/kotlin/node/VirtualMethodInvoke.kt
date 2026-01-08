package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.ReferenceType
import com.danyazero.model.Type
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Opcodes

class VirtualMethodInvoke(
    val owner: Expression,
    val signature: Signature
) : Expression {

    override fun produce(ctx: GenerationContext) {
        val ownerType = owner.getType(ctx)
        if (ownerType !is ReferenceType) throw RuntimeException("VirtualMethodInvoke only supports Reference type")
        owner.produce(ctx)
        signature.parameters.forEach { it.produce(ctx) }
        ctx.getMethodVisitor().visitMethodInsn(Opcodes.INVOKEVIRTUAL, ownerType.getName(), signature.name, ctx.resolveMethod(ownerType.getName(), signature).getDescriptor(ctx), false)
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        val ownerType = owner.getType(ctx)
        if (ownerType !is ReferenceType) throw RuntimeException("VirtualMethodInvoke only supports Reference type")

        return ctx.resolveMethod(ownerType.getName(), signature).returnType.type
    }
}