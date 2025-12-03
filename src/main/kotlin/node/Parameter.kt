package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.model.Type
import com.danyazero.utils.GenerationContext

class Parameter(
    val name: String,
    val type: Type<*>
) : Node {

    override fun produce(ctx: GenerationContext) {
        ctx.defineVariable(name = name, type = type)
    }

    override fun toString(): String {
        return "Parameter(name='$name', type=$type)"
    }
}