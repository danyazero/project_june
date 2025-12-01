package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.NumberType
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Label

class GreaterOrEqualExpression(
    left: Expression,
    right: Expression,
) : ConditionExpression(null, left, right) {

    override fun produce(ctx: GenerationContext) {
        this.apply(ctx) {
            if (it is NumberType) {
                it.greaterOrEqual(ctx.getMethodVisitor(), this.target())
            } else {
                throw RuntimeException("Less expression can accept only number type: $it")
            }
        }
    }

}