package com.danyazero.model;

import org.objectweb.asm.MethodVisitor;

public interface Type<T> {
    void postack(MethodVisitor mv, T value);
    void store(MethodVisitor mv, short index);
    void load(MethodVisitor mv);
    void yield(MethodVisitor mv);

    short getSize();
}
