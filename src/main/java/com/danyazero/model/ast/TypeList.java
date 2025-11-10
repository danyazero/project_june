package com.danyazero.model.ast;

import com.danyazero.utils.GenerationContext;
import com.danyazero.utils.TypeNode;

import java.util.List;

public class TypeList implements Node {
    private final List<TypeNode> typeNodes;

    public TypeList(List<TypeNode> typeNodes) {
        this.typeNodes = typeNodes;
    }


    @Override
    public void produce(GenerationContext ctx) {
        typeNodes.forEach(node -> node.produce(ctx));
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        typeNodes.forEach(node -> node.resolveTypes(ctx));
    }

    public List<TypeNode> getStatements() {
        return typeNodes;
    }
}
