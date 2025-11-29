package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.NumberType
import com.danyazero.utils.GenerationContext

class DivisionExpression(
    left: Expression,
    right: Expression
) : BinaryExpression(left, right){

    override fun produce(ctx: GenerationContext) {
        this.apply(ctx) {
            when (it) {
                is NumberType<*> -> {
                    it.div(ctx.getMethodVisitor())
                }

                else -> {
                    throw RuntimeException("Unsupported expression type.")
                }
            }
        }
    }

}