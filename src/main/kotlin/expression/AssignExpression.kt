package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.model.PrimitiveType
import com.danyazero.node.Operand
import com.danyazero.utils.GenerationContext

class AssignExpression(
    val operand: Operand,
    val expression: Expression,
) : Node {

    override fun produce(ctx: GenerationContext) {

        expression.produce(ctx)
        operand.getType(ctx).let {
            if (it is PrimitiveType<*>) {
                val variable = ctx.resolveVariable(operand.name) ?: throw RuntimeException("Variable ${operand.name} not found")
                it.store(ctx.getMethodVisitor(), variable.index)
                return
            }
        }

        throw RuntimeException("Unsupported assignment type")    }

}