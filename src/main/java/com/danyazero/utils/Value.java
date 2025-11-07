package com.danyazero.utils;

import com.danyazero.model.NumberType;
import com.danyazero.model.Type;
import com.danyazero.model.ast.Expression;
import org.objectweb.asm.MethodVisitor;

public class Value<T> implements Expression {
    private final T value;
    private final Type<T> type;

    public Value(T value, Type<T> type) {
        this.value = value;
        this.type = type;
    }

    public static Value<Integer> newIntegerValue(Integer value) {
        return new Value<>(value, new IntegerType());
    }

    @Override
    public void produce(GenerationContext ctx) {
        if (this.type instanceof NumberType<?>) {
            this.type.postack(ctx.getMethodVisitor(), this.value);
        } else {
            throw  new UnsupportedOperationException("Cannot emmit type " + this.type);
        }
    }

    @Override
    public Type<?> getType() {
        return type;
    }
}
