package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;
import com.danyazero.model.ast.Node;

import java.util.List;

public class Slice implements Node, Expression {
    private final List<Expression> items;
    private ArrayType arrayType;

    public Slice(List<Expression> items) {
        this.items = items;
    }

    @Override
    public void produce(GenerationContext ctx) {
        arrayType.postack(ctx.getMethodVisitor(), this.items.size());

        for (var i = 0; i < items.size(); i++) {
            var sliceValue = new SliceValue(i, items.get(i));
            sliceValue.produce(ctx);
            if (arrayType == null) arrayType = new ArrayType(sliceValue.getType());
            else if (isTypeMismatch(sliceValue)) throw new RuntimeException("Slice type mismatch");
        }
    }

    private boolean isTypeMismatch(SliceValue sliceValue) {
        return !arrayType.getChild().getClass().toString().equals(sliceValue.getType().getClass().toString());
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        items.forEach(i -> i.resolveTypes(ctx));
        var elementType = items.getFirst().getType();
        for (var item : items) {
            if (!elementType.getClass().toString().equals(item.getType().getClass().toString())) throw new RuntimeException("Slice type mismatch");
        }
        arrayType = new ArrayType(elementType);
    }

    @Override
    public Type<?> getType() {
        return arrayType;
    }
}
