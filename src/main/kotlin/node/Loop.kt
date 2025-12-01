package com.danyazero.node

import com.danyazero.expression.ConditionExpression
import com.danyazero.model.Node
import com.danyazero.utils.GenerationContext
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

class Loop(
    val condition: ConditionExpression? = null,
    val statements: List<Node>
) : Node {

    override fun produce(ctx: GenerationContext) {
        val exitLabel = Label()
        val enterLabel = Label()
        ctx.enterScope()
        ctx.getMethodVisitor().visitLabel(enterLabel)

        if (condition != null) {
            condition.target(exitLabel)
            condition.produce(ctx)
        }

        statements.forEach { it.produce(ctx) }

        ctx.getMethodVisitor().visitJumpInsn(Opcodes.GOTO, enterLabel)
        ctx.exitScope()
        ctx.getMethodVisitor().visitLabel(exitLabel)
    }

    override fun toString(): String {
        return "Loop(statements=$statements)"
    }

}