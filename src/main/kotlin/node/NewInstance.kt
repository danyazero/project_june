package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Type
import com.danyazero.type.ObjectType
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Opcodes.DUP
import org.objectweb.asm.Opcodes.INVOKESPECIAL
import org.objectweb.asm.Opcodes.NEW
import java.lang.RuntimeException

class NewInstance(
    val className: String,
    val parameters: List<Expression>,
) : Expression {
    override fun produce(ctx: GenerationContext) {
        val constructorParameters = parameters.map { Parameter("_", it.getType(ctx)) }
        val constructorSignature = Signature("constructor", constructorParameters)
        val descriptor = constructorSignature.getDescriptor(ctx)
        ctx.resolveMethod(owner = ctx.className ?: throw RuntimeException("Class name not provided while constructor initialization."), signature = constructorSignature)

        val clazz = ctx.resolveImport(className)
        ctx.getMethodVisitor().visitTypeInsn(NEW, clazz)
        ctx.getMethodVisitor().visitInsn(DUP)
        parameters.forEach { it.produce(ctx) }
        ctx.getMethodVisitor().visitMethodInsn(INVOKESPECIAL, clazz, "<init>", descriptor, false);
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        return ObjectType(ctx.resolveImport(className))
    }

    override fun toString(): String {
        return "NewInstance(className='$className', parameters=$parameters)"
    }
}