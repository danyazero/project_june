package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;
import com.danyazero.model.ast.Node;

import java.util.List;

public class Slice implements Node, Expression {
    private final List<Expression> items;
    private final ArrayType arrayType;

    public Slice(List<Expression> items) {
        this.items = items;
        var type = items.getFirst().getType();
        var arrayType = type.getClass().toString();
        for (var item : items) {
            var itemType = item.getType().getClass().toString();
            if (!arrayType.equals(itemType)) {
                throw new RuntimeException("Slice items must have the same type.");
            }
        }
        this.arrayType = new ArrayType(type);
    }

    @Override
    public void produce(GenerationContext ctx) {
        arrayType.postack(ctx.getMethodVisitor(), this.items.size());

        for (var i = 0; i < items.size(); i++) {
            new SliceValue(i, items.get(i)).produce(ctx);
        }
    }

    @Override
    public Type<?> getType() {
        return null;
    }
}
