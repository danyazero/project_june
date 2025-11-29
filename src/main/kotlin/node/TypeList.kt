package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.model.Type
import com.danyazero.utils.GenerationContext

class TypeList(
    val types: List<TypeNode>
) : Node{
    override fun produce(ctx: GenerationContext) {}

    override fun toString(): String {
        return "TypeList(types=$types)"
    }
}