package com.danyazero.model;

import org.objectweb.asm.MethodVisitor;

public interface Type<T> {
    void postack(MethodVisitor mv, T value);
    void store(MethodVisitor mv, short index);
    void load(MethodVisitor mv, short index);
    void yield(MethodVisitor mv);

    void aload(MethodVisitor mv);
    void astore(MethodVisitor mv);

    short getSize();
}
