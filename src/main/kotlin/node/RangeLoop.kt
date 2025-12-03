package com.danyazero.node

import com.danyazero.expression.*
import com.danyazero.model.Node
import com.danyazero.model.Range
import com.danyazero.model.ToRange
import com.danyazero.model.UntilRange
import com.danyazero.type.IntegerType
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

class RangeLoop(
    val index: String,
    val range: Range,
    val statements: List<Node>
) : Node {

    override fun produce(ctx: GenerationContext) {
        val enterLabel = Label()
        val exitLabel = Label()
        ctx.enterScope()

        Variable(index, IntegerType(), range.start).produce(ctx)

        ctx.getMethodVisitor().visitLabel(enterLabel)

        val rangeExpression = when (range) {
            is ToRange -> ::LessOrEqualExpression
            is UntilRange -> ::LessExpression
            else -> throw RuntimeException("Unexpected range: $range")
        }(Operand(index, true), range.end)

        rangeExpression.target(exitLabel)
        rangeExpression.produce(ctx)

        statements.forEach { it.produce(ctx) }

        AssignExpression(operand = Operand(index, true), expression = AdditionExpression(Operand(index, true),
            ValueExpression.of(1))).produce(ctx)

        ctx.getMethodVisitor().visitJumpInsn(Opcodes.GOTO, enterLabel)
        ctx.exitScope()
        ctx.getMethodVisitor().visitLabel(exitLabel)
    }

    override fun toString(): String {
        return "RangeLoop(index='$index', range=$range, statements=$statements)"
    }
}