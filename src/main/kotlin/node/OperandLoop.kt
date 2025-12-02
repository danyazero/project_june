package com.danyazero.node

import com.danyazero.expression.ValueExpression
import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.model.UntilRange
import com.danyazero.type.ArrayType
import com.danyazero.utils.GenerationContext

class OperandLoop(
    val operand: Expression,
    val index: String = "_",
    val item: String = "it",
    val statements: List<Node>
) : Node {

    override fun produce(ctx: GenerationContext) {
        val operandType = operand.getType(ctx)
        if (operandType !is ArrayType) throw RuntimeException("Operand loop can accept only arrays")

        val arrayItem = ArrayValue(operand = operand, index = Operand(index, true))
        val itemVariable = Variable(name = item, type = operandType.child, value = arrayItem)
        RangeLoop(
            range = UntilRange(start = ValueExpression.of(0), end = Length(operand)),
            index = index,
            statements = listOf(itemVariable) + statements
        ).produce(ctx)
    }

    override fun toString(): String {
        return "OperandLoop(operand=$operand, index=$index, item=$item, statements=$statements)"
    }

}