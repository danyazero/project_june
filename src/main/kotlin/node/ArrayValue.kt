package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Type
import com.danyazero.type.ArrayType
import com.danyazero.utils.GenerationContext

class ArrayValue(
    val operand: Expression,
    val index: Expression,
) : Expression {

    override fun produce(ctx: GenerationContext) {
        if (operand is Operand) {
            operand.produce(ctx)
            index.produce(ctx)
            val operandType = operand.getType(ctx)
            if (operandType is ArrayType) {
                operandType.child.aload(ctx.getMethodVisitor())
            } else {
                throw RuntimeException("Operand is not array")
            }
        } else throw RuntimeException("Unsupported kind of operand")
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        val type = operand.getType(ctx)
        if (type is ArrayType) {
            return type.child
        }

        return type
    }

    override fun toString(): String {
        return "ArrayElement(operand=$operand, index=$index)"
    }
}