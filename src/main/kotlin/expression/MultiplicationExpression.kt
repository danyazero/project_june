package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.NumberType
import com.danyazero.utils.GenerationContext

class MultiplicationExpression(
    left: Expression,
    right: Expression
) : BinaryExpression(left, right) {

    override fun produce(ctx: GenerationContext) {
        this.apply(ctx) {
            when (it) {
                is NumberType<*> -> {
                    it.mul(ctx.getMethodVisitor())
                }

                else -> {
                    throw RuntimeException("Unsupported expression type.")
                }
            }
        }
    }
}