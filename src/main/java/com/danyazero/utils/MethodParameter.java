package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Node;

public class MethodParameter implements Node {
    private final String name;
    private final Type<?> type;

    public MethodParameter(String name, Type<?> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public void produce(GenerationContext ctx) {
        ctx.defineVariable(name, type);
    }

    public Type<?> getType() {
        return type;
    }
}
