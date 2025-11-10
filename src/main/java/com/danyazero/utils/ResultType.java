package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Node;

public class ResultType implements Node {
    private final Type<?> type;

    public ResultType(Type<?> type) {
        this.type = type;
    }

    @Override
    public void produce(GenerationContext ctx) {

    }

    public Type<?> getType() {
        return type;
    }
}
