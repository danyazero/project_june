package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;
import com.danyazero.model.ast.Node;

public class Variable implements com.danyazero.model.Variable, Node {
    private final String name;
    private Type<?> type;
    private final Expression value;

    public Variable(String name, Type<?> type, Expression value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public void produce(GenerationContext ctx) {
        value.produce(ctx);
        var variableIndex = ctx.getMethodContext().resolveVariable(name);
        type.store(ctx.getMethodVisitor(), variableIndex.localIndex());
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        value.resolveTypes(ctx);
        ctx.getMethodContext().defineVariable(name, value.getType());
        type = value.getType();
        System.out.println();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type<?> getType() {
        return type;
    }
}
