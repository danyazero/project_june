package com.danyazero.utils;

import com.danyazero.model.PrimitiveType;
import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;

import static org.objectweb.asm.Opcodes.DUP;

public class SliceValue implements Expression {
    private final Expression value;
    private final int index;
    private Type<?> type;

    public SliceValue(int index, Expression value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public void produce(GenerationContext ctx) {
        if (value.getType() instanceof PrimitiveType<?>) {
            ctx.getMethodVisitor().visitInsn(DUP);
            Value.of(index).produce(ctx);
            value.produce(ctx);
            type = value.getType();
            type.astore(ctx.getMethodVisitor());
        } else {
            throw new RuntimeException("This type is not acceptable.");
        }
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        value.resolveTypes(ctx);
        type = value.getType();
    }

    @Override
    public Type<?> getType() {
        return type;
    }
}
