package com.danyazero.utils;

import com.danyazero.model.PrimitiveType;
import com.danyazero.model.ast.Expression;
import com.danyazero.model.ast.Node;

import static org.objectweb.asm.Opcodes.DUP;

public class SliceValue implements Node {
    private final Expression value;
    private final int index;

    public SliceValue(int index, Expression value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public void produce(GenerationContext ctx) {
        if (value.getType() instanceof PrimitiveType<?> type) {
            ctx.getMethodVisitor().visitInsn(DUP);
            Value.of(index).produce(ctx);
            value.produce(ctx);
            type.astore(ctx.getMethodVisitor());
        } else {
            throw new RuntimeException("This type is not acceptable.");
        }


    }
}
