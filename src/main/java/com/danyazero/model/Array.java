package com.danyazero.model;

import org.objectweb.asm.MethodVisitor;

public interface Array<T> extends Type<T> {
    void storeElement(MethodVisitor mv, int index, T value);
    void loadElement(MethodVisitor mv, int index);
}
