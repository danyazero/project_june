package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.model.Type
import com.danyazero.utils.GenerationContext

class TypeNode(
    val type: Type<*>,
) : Node {
    override fun produce(ctx: GenerationContext) {}

    override fun toString(): String {
        return "TypeNode(type=$type)"
    }

}