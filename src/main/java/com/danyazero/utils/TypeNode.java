package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Node;

public class TypeNode implements Node {
    private final com.danyazero.model.Type<?> type;

    public TypeNode(com.danyazero.model.Type<?> type) {
        this.type = type;
    }

    @Override
    public void produce(GenerationContext ctx) {

    }

    @Override
    public void resolveTypes(GenerationContext ctx) {

    }

    public Type<?> getType() {
        return type;
    }
}
