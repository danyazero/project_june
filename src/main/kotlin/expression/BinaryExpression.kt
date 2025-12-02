package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.Type
import com.danyazero.utils.GenerationContext

abstract class BinaryExpression(
    protected val left: Expression,
    protected val right: Expression,
) : Expression {

    fun apply(ctx: GenerationContext, producer: (Type<*>) -> Unit) {
        left.produce(ctx)
        right.produce(ctx)

        left.getType(ctx).let {
            if (it.javaClass != right.getType(ctx).javaClass) {
                throw RuntimeException("Wrong type for AdditionExpression")
            }

            producer(it)
        }
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        left.getType(ctx).let {
            if (right.getType(ctx).javaClass == it.javaClass) {
                return it
            }

            throw RuntimeException("Incorrect type for AdditionExpression")
        }
    }

    override fun toString(): String {
        return "BinaryExpression(left=$left, right=$right)"
    }

}