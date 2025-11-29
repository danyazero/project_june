package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.NumberType
import com.danyazero.utils.GenerationContext

class RemainderExpression(
    left: Expression,
    right: Expression
) : BinaryExpression(left, right){
    override fun produce(ctx: GenerationContext) {
        this.apply(ctx) {
            when (it) {
                is NumberType<*> -> {
                    it.mod(ctx.getMethodVisitor())
                }
                else -> {
                    throw RuntimeException("bad operand types for binary operator remaining")
                }
            }
        }
    }
}