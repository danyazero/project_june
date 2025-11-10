package com.danyazero.utils;

import com.danyazero.model.PrimitiveType;
import org.objectweb.asm.MethodVisitor;

public class BooleanType implements PrimitiveType<Boolean> {
    @Override
    public void negate(MethodVisitor mv) {

    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void postack(MethodVisitor mv, Boolean value) {

    }

    @Override
    public void store(MethodVisitor mv, short index) {

    }

    @Override
    public void load(MethodVisitor mv, short index) {

    }

    @Override
    public void yield(MethodVisitor mv) {

    }

    @Override
    public void aload(MethodVisitor mv) {

    }

    @Override
    public void astore(MethodVisitor mv) {

    }

    @Override
    public short getSize() {
        return 0;
    }
}
