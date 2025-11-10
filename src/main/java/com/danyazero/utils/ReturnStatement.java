package com.danyazero.utils;

import com.danyazero.model.ast.Node;

public class ReturnStatement implements Node {
    private final Node expression;

    public ReturnStatement(Node expression) {
        this.expression = expression;
    }

    @Override
    public void produce(GenerationContext ctx) {
        expression.produce(ctx);
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        expression.resolveTypes(ctx);
    }
}
