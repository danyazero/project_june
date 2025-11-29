package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.type.IntegerType
import com.danyazero.utils.GenerationContext

class LogicalShiftRightExpression(
    left: Expression,
    right: Expression
) : BinaryExpression(left, right) {

    override fun produce(ctx: GenerationContext) {
        this.apply(ctx) {
            if (it is IntegerType) {
                it.ushr(ctx.getMethodVisitor())
            } else {
                throw RuntimeException("bad operand types for binary operator logical shift right")
            }
        }
    }

}