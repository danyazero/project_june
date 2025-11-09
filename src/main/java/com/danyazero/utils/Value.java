package com.danyazero.utils;

import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;
import com.danyazero.model.ast.IValue;

public class Value<T> implements Expression, IValue {
    private final T value;
    private final Type<T> type;

    public Value(T value, Type<T> type) {
        this.value = value;
        this.type = type;
    }

    public static Value<Integer> of(Integer value) {
        return new Value<>(value, new IntegerType());
    }

    public static Value<String> of(String value) {
        return new Value<>(value, new StringType());
    }

    @Override
    public void produce(GenerationContext ctx) {
        this.type.postack(ctx.getMethodVisitor(), this.value);
    }

    @Override
    public Type<?> getType() {
        return type;
    }
}
