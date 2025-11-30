package com.danyazero.node

import com.danyazero.expression.AssignExpression
import com.danyazero.expression.ValueExpression
import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.model.NumberType
import com.danyazero.type.AnyType
import com.danyazero.type.ArrayType
import com.danyazero.type.IntegerType
import com.danyazero.type.VoidType
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

class OperandLoop(
    val operand: Expression,
    val index: Parameter = Parameter(name = "_", type = IntegerType()),
    val item: Parameter = Parameter(name = "it", type = AnyType()),
    val statements: List<Node>
) : Node {

    override fun produce(ctx: GenerationContext) {
        if (index.type !is NumberType<*>) throw RuntimeException("Unsupported index type")

        val operandType = operand.getType(ctx)
        if (operandType !is ArrayType) throw RuntimeException("Operand loop can accept only arrays")

        val exitLabel = Label()
        val enterLabel = Label()

        ctx.enterScope()
        Variable(index.name, index.type, ValueExpression.of(0)).produce(ctx)
        ctx.defineVariable(item.name, operandType.child)
        ctx.getMethodVisitor().visitLabel(enterLabel)
        Operand(index.name, true).produce(ctx)

        operand.produce(ctx)
        operandType.length(ctx.getMethodVisitor())
        index.type.less(ctx.getMethodVisitor(), exitLabel)

        Variable(item.name, operandType.child, ArrayValue(operand = operand, index = Operand(index.name, true))).produce(ctx)


        statements.forEach { it.produce(ctx) }

        val indexVariable = ctx.resolveLocalVariable(index.name) ?: throw RuntimeException("Loop index resolve exception")
        if (index.type is IntegerType) {
            index.type.inc(ctx.getMethodVisitor(), indexVariable.index, 1)
        } else {
            throw RuntimeException("Unsupported index type (${index.type})")
        }

        ctx.getMethodVisitor().visitJumpInsn(Opcodes.GOTO, enterLabel)
        ctx.exitScope()
        ctx.getMethodVisitor().visitLabel(exitLabel)

    }

    override fun toString(): String {
        return "OperandLoop(operand=$operand, index=$index, item=$item, statements=$statements)"
    }

}