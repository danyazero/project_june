package com.danyazero.utils;

import com.danyazero.model.PrimitiveType;
import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;

public class Assignment implements Expression {
    private final Operand operand;
    private final Expression expression;
    private Type<?> type;

    public Assignment(Operand operand, Expression expression) {
        this.operand = operand;
        this.expression = expression;
    }

    @Override
    public void produce(GenerationContext ctx) {
        var operandInfo = operand.getVariableInfo();
        expression.produce(ctx);
        System.out.println("Assignment operand type: " + operandInfo.type().getClass());
        if (operandInfo.type() instanceof PrimitiveType<?> operandType) {
            operandType.store(ctx.getMethodVisitor(), operandInfo.localIndex());
            return;
        }

        throw new RuntimeException("Unsupported assignment type");
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        operand.resolveTypes(ctx);
        type = operand.getType();
        expression.resolveTypes(ctx);
        if (isTypeMismatch()) throw new RuntimeException("Assignment type mismatch");
    }

    private boolean isTypeMismatch() {
        return !type.getClass().toString().equals(expression.getType().getClass().toString());
    }

    @Override
    public Type<?> getType() {
        return null;
    }
}
