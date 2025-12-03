package com.danyazero.expression

import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.model.PrimitiveType
import com.danyazero.node.Operand
import com.danyazero.type.StringType
import com.danyazero.utils.GenerationContext

class AssignExpression(
    val operand: Operand,
    val expression: Expression,
) : Node {

    override fun produce(ctx: GenerationContext) {
        val variable = operand.resolveVariable(ctx) ?: throw RuntimeException("Variable ${operand.name} not found")
        if (variable.isConstant) throw RuntimeException("Variable ${variable.name} is constant and cannot be changed")

        expression.produce(ctx)
        operand.getType(ctx).let {
            if (it is PrimitiveType<*> || it is StringType) {
                it.store(ctx.getMethodVisitor(), variable.index)
                return
            }
        }

        throw RuntimeException("Unsupported assignment type")
    }

    override fun toString(): String {
        return "AssignExpression(operand=$operand, expression=$expression)"
    }

}