package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Type
import com.danyazero.model.VariableInfo
import com.danyazero.utils.GenerationContext

class Operand(
    val name: String,
    val local: Boolean = false
) : Expression {

    override fun produce(ctx: GenerationContext) {
        resolveVariable(ctx)?.let {
            it.type.load(
                ctx.getMethodVisitor(),
                it.index
            )
        } ?: throw RuntimeException("Could not resolve variable")
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        resolveVariable(ctx)?.let {
            return it.type
        }

        throw RuntimeException("Could not resolve variable")
    }

        fun resolveVariable(ctx: GenerationContext): VariableInfo? {
            return if (local) {
                ctx.resolveLocalVariable(name)
            } else {
                ctx.resolveVariable(name)
            }
        }

    override fun toString(): String {
        return "Operand(name='$name', local=$local)"
    }

}