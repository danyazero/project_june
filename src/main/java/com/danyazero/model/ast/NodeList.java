package com.danyazero.model.ast;

import com.danyazero.utils.GenerationContext;

import java.util.List;

public class NodeList implements Node {
    private final List<Node> statements;

    public NodeList(List<Node> statements) {
        this.statements = statements;
    }

    @Override
    public void produce(GenerationContext ctx) {
        statements.forEach(node -> node.produce(ctx));
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        statements.forEach(node -> node.resolveTypes(ctx));
    }

    public List<Node> getStatements() {
        return statements;
    }
}
