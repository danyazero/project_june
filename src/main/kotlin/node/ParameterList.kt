package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.utils.GenerationContext

class ParameterList(
    val parameters: List<Parameter>,
) : Node{
    override fun produce(ctx: GenerationContext) {}

    override fun toString(): String {
        return "ParameterList(parameters=$parameters)"
    }
}