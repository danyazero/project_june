package com.danyazero.node

import com.danyazero.expression.ValueExpression
import com.danyazero.model.Expression
import com.danyazero.model.PrimitiveType
import com.danyazero.model.Type
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Opcodes.DUP

class ArrayItem(
    val value: Expression,
    val index: Int,
) : Expression {
    override fun produce(ctx: GenerationContext) {
        if (value.getType(ctx) is PrimitiveType<*>) {
            ctx.getMethodVisitor().visitInsn(DUP)
            ValueExpression.of(index).produce(ctx)
            value.produce(ctx)
            val type = value.getType(ctx)
            type.astore(ctx.getMethodVisitor())
        } else {
            throw RuntimeException("This type is not acceptable.")
        }
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        return value.getType(ctx)
    }
}