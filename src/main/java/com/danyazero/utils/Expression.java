package com.danyazero.utils;

import com.danyazero.model.NumberType;
import com.danyazero.model.Operation;
import com.danyazero.model.Type;

public class Expression implements com.danyazero.model.ast.Expression {
    private final com.danyazero.model.ast.Expression left;
    private final com.danyazero.model.ast.Expression right;
    private final Operation op;
    private final Type<?> type;

    public Expression(Operation op, com.danyazero.model.ast.Expression left, com.danyazero.model.ast.Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.type = getExpressionType();
    }


    @Override
    public void produce(GenerationContext ctx) {
        System.out.println("Expression.produce");
        if (right instanceof Expression) {
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
    public Type<?> getType() {
        return type;
    }

    private Type<?> getExpressionType() {
        var leftExpressionType = this.getExpressionType(this.left);
        var rightExpressionType = this.getExpressionType(this.right);

//        System.out.println(leftExpressionType + " " + rightExpressionType);
        if (leftExpressionType.getClass() == rightExpressionType.getClass()) {
            return leftExpressionType;
        } else {
            throw new RuntimeException("Expression type mismatch");
        }

    }

    private Type<?> getExpressionType(com.danyazero.model.ast.Expression expression) {
        if (expression instanceof Value<?>) {
            return expression.getType();
        } else if (expression instanceof Expression) {
            return expression.getType();
        } else {
            throw new RuntimeException("Cannot extract expression type");
        }
    }
}
