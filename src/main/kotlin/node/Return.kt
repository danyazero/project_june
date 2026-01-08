package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.type.VoidType
import com.danyazero.utils.GenerationContext

class Return(
    val expression: Expression? = null
) : Node {
    override fun produce(ctx: GenerationContext) {
        if (expression != null) {
            expression.produce(ctx)
            expression.getType(ctx).yield(ctx.getMethodVisitor())
        } else {
            VoidType().yield(ctx.getMethodVisitor())
        }
    }

    override fun toString(): String {
        return "Return(expressionList=$expression)"
    }
}