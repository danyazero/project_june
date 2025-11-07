package com.danyazero.model;

import org.objectweb.asm.MethodVisitor;

public interface PrimitiveType<T> extends Type<T> {
    void negate(MethodVisitor mv);
}
