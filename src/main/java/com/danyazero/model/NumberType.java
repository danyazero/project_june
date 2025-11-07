package com.danyazero.model;

import org.objectweb.asm.MethodVisitor;

public interface NumberType<T> extends PrimitiveType<T> {
    void add(MethodVisitor mv);
    void sub(MethodVisitor mv);
    void mul(MethodVisitor mv);
    void div(MethodVisitor mv);
}
