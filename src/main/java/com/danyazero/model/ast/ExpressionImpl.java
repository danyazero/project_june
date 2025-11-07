package com.danyazero.model.ast;

import com.danyazero.model.NumberType;
import com.danyazero.model.Operation;
import com.danyazero.model.Type;
import org.objectweb.asm.MethodVisitor;

public class ExpressionImpl implements Expression, Operand {
    private final Expression left;
    private final Expression right;
    private final Operation op;
    private final Type type;

    public ExpressionImpl(Operation op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.type = getExpressionType();
    }


    @Override
    public void produce(MethodVisitor mv) {
        if (right instanceof ExpressionImpl) {
            right.produce(mv);
            left.produce(mv);
        } else {
            left.produce(mv);
            right.produce(mv);
        }

        switch (op) {
            case ADDITION -> {
                if (type instanceof NumberType<?> numberType) {
                    numberType.add(mv);
                }
            }
            case SUBSTRUCTION -> {
                if (type instanceof NumberType<?> numberType) {
                    numberType.sub(mv);
                }
            }
            case MULTIPLICATION -> {
                if (type instanceof NumberType<?> numberType) {
                    numberType.mul(mv);
                }
            }
            case DIVISION -> {
                if (type instanceof NumberType<?> numberType) {
                    numberType.div(mv);
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

    private Type getExpressionType(Expression expression) {
        if (expression instanceof Value<?>) {
            return expression.getType();
        } else if (expression instanceof ExpressionImpl) {
            return expression.getType();
        } else {
            throw new RuntimeException("Cannot extract expression type");
        }
    }
}
