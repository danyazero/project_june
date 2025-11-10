package com.danyazero.utils;

import com.danyazero.model.NumberType;
import com.danyazero.model.Operation;
import com.danyazero.model.Type;

public class ExpressionNode implements com.danyazero.model.ast.Expression {
    private final com.danyazero.model.ast.Expression left;
    private final com.danyazero.model.ast.Expression right;
    private final Operation op;
    private Type<?> type;

    public ExpressionNode(Operation op, com.danyazero.model.ast.Expression left, com.danyazero.model.ast.Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }


    @Override
    public void produce(GenerationContext ctx) {
        System.out.println("Expression.produce");
        if (right instanceof ExpressionNode) {
            right.produce(ctx);
            left.produce(ctx);
        } else {
            left.produce(ctx);
            right.produce(ctx);
        }

        switch (op) {
            case ADDITION -> {
                if (type instanceof NumberType<?> numberType) {
                    numberType.add(ctx.getMethodVisitor());
                }
            }
            case SUBSTRUCTION -> {
                if (type instanceof NumberType<?> numberType) {
                    numberType.sub(ctx.getMethodVisitor());
                }
            }
            case MULTIPLICATION -> {
                if (type instanceof NumberType<?> numberType) {
                    numberType.mul(ctx.getMethodVisitor());
                }
            }
            case DIVISION -> {
                if (type instanceof NumberType<?> numberType) {
                    numberType.div(ctx.getMethodVisitor());
                }
            }
        }
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        left.resolveTypes(ctx);
        right.resolveTypes(ctx);

        var leftType = left.getType();
        if  (isTypeMismatch(leftType)) throw new RuntimeException("Expression mismatch");
        type = leftType;
    }

    private boolean isTypeMismatch(Type<?> leftType) {
        return !leftType.getClass().toString().equals(right.getType().getClass().toString());
    }

    @Override
    public Type<?> getType() {
        return type;
    }
}
