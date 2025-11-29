package com.danyazero.node

import com.danyazero.model.Node
import com.danyazero.utils.GenerationContext

class NodeList(
    val nodes: List<Node>
) : Node{
    override fun produce(ctx: GenerationContext) {
        nodes.forEach { node -> node.produce(ctx) }
    }

    override fun toString(): String {
        return "NodeList(nodes=$nodes)"
    }
}