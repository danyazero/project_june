package com.danyazero.utils;

import com.danyazero.model.ReferenceType;
import org.objectweb.asm.MethodVisitor;

public class StringType implements ReferenceType<String> {
    @Override
    public void postack(MethodVisitor mv, String value) {
        mv.visitLdcInsn(value);
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
        return 1;
    }
}
