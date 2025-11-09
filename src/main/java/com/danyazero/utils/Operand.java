package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.VariableInfo;
import com.danyazero.model.ast.IValue;

public class Operand implements IValue {
    private final String name;
    private VariableInfo variableInfo;

    public Operand(String name) {
        this.name = name;
    }

    @Override
    public void produce(GenerationContext ctx) {
        variableInfo = ctx.resolveVariable(this.name);
        variableInfo.type().load(
                ctx.getMethodVisitor(),
                variableInfo.localIndex()
        );
    }

    public Type<?> getType() {
        return variableInfo != null ? variableInfo.type() : null;
    }
}
