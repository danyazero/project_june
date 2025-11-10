package com.danyazero.utils;

import com.danyazero.model.ReferenceType;
import org.objectweb.asm.MethodVisitor;

public class ObjectType implements ReferenceType<Object> {
    private final String name;

    public ObjectType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void postack(MethodVisitor mv, Object value) {

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
