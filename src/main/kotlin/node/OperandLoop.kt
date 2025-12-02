package com.danyazero.node

import com.danyazero.expression.LessExpression
import com.danyazero.expression.ValueExpression
import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.type.ArrayType
import com.danyazero.type.IntegerType
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

class OperandLoop(
    val operand: Expression,
    val index: String = "_",
    val item: String = "it",
    val statements: List<Node>
) : Node {

    override fun produce(ctx: GenerationContext) {
        val operandType = operand.getType(ctx)
        if (operandType !is ArrayType) throw RuntimeException("Operand loop can accept only arrays")

        val enterLabel = Label()
        val exitLabel = Label()
        ctx.enterScope()

        Variable(index, IntegerType(), ValueExpression.of(0)).produce(ctx)
        ctx.defineVariable(item, operandType.child)

        ctx.getMethodVisitor().visitLabel(enterLabel)

        val conditionExpression = LessExpression(
            left = Operand(index, true),
            right = Length(operand),
        )
        conditionExpression.target(exitLabel)
        conditionExpression.produce(ctx)

        Variable(item, operandType.child, ArrayValue(operand = operand, index = Operand(index, true))).produce(ctx)


        statements.forEach { it.produce(ctx) }

        val indexVariable = ctx.resolveLocalVariable(index)
            ?: throw RuntimeException("Loop index resolve exception")

        IntegerType().inc(ctx.getMethodVisitor(), indexVariable.index, 1)

        ctx.getMethodVisitor().visitJumpInsn(Opcodes.GOTO, enterLabel)
        ctx.exitScope()
        ctx.getMethodVisitor().visitLabel(exitLabel)

    }

    override fun toString(): String {
        return "OperandLoop(operand=$operand, index=$index, item=$item, statements=$statements)"
    }

}