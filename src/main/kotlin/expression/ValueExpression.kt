package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.Type
import com.danyazero.type.BooleanType
import com.danyazero.type.DoubleType
import com.danyazero.type.IntegerType
import com.danyazero.type.StringType
import com.danyazero.utils.GenerationContext

class ValueExpression<T>(
    val value: T,
    private val type: Type<T>
) : Expression {

    override fun produce(ctx: GenerationContext) {
        this.type.postack(ctx.getMethodVisitor(), this.value)
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        return type
    }

    companion object {
        fun of(value: Int) : ValueExpression<Int> {
            return ValueExpression(value, IntegerType())
        }

        fun of(value: String) : ValueExpression<String> {
            return ValueExpression(value, StringType())
        }

        fun of(value: Double) : ValueExpression<Double> {
            return ValueExpression(value, DoubleType())
        }

        fun of(value: Boolean) : ValueExpression<Boolean> {
            return ValueExpression(value, BooleanType())
        }
    }

    override fun toString(): String {
        return "ValueExpression(value=$value, type=$type)"
    }
}