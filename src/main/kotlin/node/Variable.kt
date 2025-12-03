package com.danyazero.node

import com.danyazero.model.Expression
import com.danyazero.model.Node
import com.danyazero.model.IVariable
import com.danyazero.model.Type
import com.danyazero.utils.GenerationContext
import java.lang.RuntimeException

class Variable(
    private val name: String,
    private val type: Type<*>?=null,
    val value: Expression,
    val isConstant: Boolean = false
) : Node, IVariable {

    override fun produce(ctx: GenerationContext) {
        println(type?.javaClass?.name + " " + value.getType(ctx).javaClass.name)
        if (this.type != null && !Type.deepType(this.type, value.getType(ctx))) throw RuntimeException("Variable can't be produce: Provided expression has type wrong type")
        value.produce(ctx)
        val variableIndex = ctx.defineVariable(name, isConstant, value.getType(ctx))
        value.getType(ctx).store(ctx.getMethodVisitor(), variableIndex)
    }

    override fun getName(): String {
        return name
    }

    override fun getType(ctx: GenerationContext): Type<*> {
        return value.getType(ctx)
    }

    override fun toString(): String {
        return "Variable(name='$name', type=$type, value=$value)"
    }

}