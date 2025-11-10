package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;
import com.danyazero.model.ast.Node;

public class SliceElement implements Node, Expression {
    private final Expression value;
    private final Expression index;
    private Type<?> type;


    public SliceElement(Expression value, Expression index) {
        this.value = value;
        this.index = index;
    }


    @Override
    public void produce(GenerationContext ctx) {
        if (value instanceof Operand operand) {
            operand.produce(ctx);
            index.produce(ctx);
            if (operand.getType() instanceof ArrayType el) {
                el.getChild().aload(ctx.getMethodVisitor());
            } else {
                throw new RuntimeException("Not implemented (Array in Array)");
            }
        } else {
            throw new RuntimeException("This operand is not supported");
        }

    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        index.resolveTypes(ctx);
        value.resolveTypes(ctx);
        type = value.getType();
    }

    @Override
    public Type<?> getType() {
        return index.getType();
    }
}
