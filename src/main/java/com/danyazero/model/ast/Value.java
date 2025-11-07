package com.danyazero.model.ast;

import com.danyazero.model.NumberType;
import com.danyazero.model.Type;
import com.danyazero.model.number.IntegerType;
import org.objectweb.asm.MethodVisitor;

public class Value<T> implements Expression, Operand {
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
    public void produce(MethodVisitor mv) {
        if (this.type instanceof NumberType<?>) {
            this.type.postack(mv, this.value);
        } else {
            throw  new UnsupportedOperationException("Cannot emmit type " + this.type);
        }
    }

    @Override
    public Type<?> getType() {
        return type;
    }
}
