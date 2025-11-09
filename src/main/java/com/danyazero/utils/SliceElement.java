package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;
import com.danyazero.model.ast.Node;
import com.danyazero.model.ast.IValue;

public class SliceElement implements Node, Expression {
    private final IValue value;
    private final Value<Integer> index;


    public SliceElement(IValue value, Value<Integer> index) {
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
    public Type<?> getType() {
        return null;
    }
}
