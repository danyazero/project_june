package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Type
import com.danyazero.utils.GenerationContext

class Operand(
    val name: String,
    val local: Boolean = false
) : Expression {

    override fun produce(ctx: GenerationContext) {
        val variable = if (local) {
            ctx.resolveLocalVariable(name)
        } else {
            ctx.resolveVariable(name)
        }

        variable?.let {
            it.type.load(
                ctx.getMethodVisitor(),
                it.index
            )
        } ?: throw RuntimeException("Could not resolve variable")
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        ctx.resolveVariable(name)?.let {
            return it.type
        }

        throw RuntimeException("Could not resolve variable")
    }

    override fun toString(): String {
        return "Operand(name='$name')"
    }
}