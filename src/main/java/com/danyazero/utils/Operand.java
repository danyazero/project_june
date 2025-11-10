package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.VariableInfo;
import com.danyazero.model.ast.Expression;

public class Operand implements Expression {
    private final String name;
    private VariableInfo variableInfo;


    public Operand(String name) {
        this.name = name;
    }

    @Override
    public void produce(GenerationContext ctx) {
        variableInfo.type().load(
                ctx.getMethodVisitor(),
                variableInfo.localIndex()
        );
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        variableInfo = ctx.getMethodContext().resolveVariable(this.name);
        System.out.println(variableInfo);
    }

    public Type<?> getType() {
        return variableInfo.type();
    }

    @Override
    public String toString() {
        return "Operand{" +
                "name='" + name + '\'' +
                ", variableInfo=" + variableInfo +
                '}';
    }
}
