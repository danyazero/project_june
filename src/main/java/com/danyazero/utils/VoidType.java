package com.danyazero.utils;

import com.danyazero.model.Type;
import org.objectweb.asm.MethodVisitor;

public class VoidType implements Type<Void> {
    @Override
    public void postack(MethodVisitor mv, Void value) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void store(MethodVisitor mv, short index) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void load(MethodVisitor mv, short index) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void yield(MethodVisitor mv) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void aload(MethodVisitor mv) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void astore(MethodVisitor mv) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public short getSize() {
        return 0;
    }
}
