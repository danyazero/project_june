package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.NumberType
import com.danyazero.model.Type
import com.danyazero.type.ArrayType
import com.danyazero.utils.GenerationContext

class Array(
    val items: List<Expression>,
) : Expression {

    override fun produce(ctx: GenerationContext) {
        val type = getType(ctx)
        if (type !is ArrayType) throw RuntimeException("Unknown error")

        type.postack(ctx.getMethodVisitor(), items.size)

        for ((index, item) in items.withIndex()) {
            val arrayValue = ArrayItem(item, index)
            arrayValue.produce(ctx)
        }
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        val itemType = items.first().getType(ctx)
        items.forEach {
            if (it.getType(ctx).javaClass != itemType.javaClass) {
                throw RuntimeException("Array value expression has wrong type '${it.getType(ctx).javaClass}'")
            }
        }

        return ArrayType(child = itemType, length = items.size)
    }

    override fun toString(): String {
        return "Array(items=$items)"
    }
}