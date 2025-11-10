package com.danyazero.utils;

import com.danyazero.model.NumberType;
import org.objectweb.asm.MethodVisitor;

public class DoubleType implements NumberType<Double> {
    @Override
    public void add(MethodVisitor mv) {

    }

    @Override
    public void sub(MethodVisitor mv) {

    }

    @Override
    public void mul(MethodVisitor mv) {

    }

    @Override
    public void div(MethodVisitor mv) {

    }

    @Override
    public void negate(MethodVisitor mv) {

    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void postack(MethodVisitor mv, Double value) {

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
