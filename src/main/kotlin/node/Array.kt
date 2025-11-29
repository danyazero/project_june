package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Type
import com.danyazero.type.ArrayType
import com.danyazero.utils.GenerationContext

class Array(
    val items: List<Expression>,
    private val type: ArrayType
) : Expression {

    override fun produce(ctx: GenerationContext) {
        items.forEach {
            if (it.getType(ctx).javaClass != type.child.javaClass)
                throw RuntimeException("Array value expression has wrong type '${it.getType(ctx).javaClass}'")
        }
        type.postack(ctx.getMethodVisitor(), items.size)

        for ((index, item) in items.withIndex()) {
            val arrayValue = ArrayItem(item, index)
            arrayValue.produce(ctx)
        }
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        return type
    }
}