package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;
import com.danyazero.model.ast.Node;

public class Variable implements com.danyazero.model.Variable, Node {
    private final String name;
    private final Type<?> type;
    private final Expression value;

    public Variable(String name, Type<?> type, Expression value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public void produce(GenerationContext ctx) {
        value.produce(ctx);
        var variableIndex = ctx.defineVariable(name, this);
        type.store(ctx.getMethodVisitor(), variableIndex);
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
