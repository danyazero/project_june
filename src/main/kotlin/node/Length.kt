package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Type
import com.danyazero.type.ArrayType
import com.danyazero.type.IntegerType
import com.danyazero.utils.GenerationContext

class Length(
    val child: Expression
) : Expression {

    override fun produce(ctx: GenerationContext) {
        val childType = child.getType(ctx)
        if (childType is ArrayType) {
            child.produce(ctx)
            childType.length(ctx.getMethodVisitor())
        } else throw RuntimeException("Unsupported type $childType for length method")
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        return IntegerType()
    }
}