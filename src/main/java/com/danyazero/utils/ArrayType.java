package com.danyazero.utils;

import com.danyazero.model.PrimitiveType;
import com.danyazero.model.Type;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ArrayType implements Type<Integer> {
    private final Type<?> child;

    public ArrayType(Type<?> type) {
        this.child = type;
    }

    public Type<?> getChild() {
        return child;
    }

    @Override
    public void postack(MethodVisitor mv, Integer value) {
        new IntegerType().postack(mv, value);

        if (child instanceof PrimitiveType<?> type) {
            System.out.println("New array.");
            mv.visitIntInsn(NEWARRAY, type.getType());
        } else {
            mv.visitInsn(ANEWARRAY);
        }
    }

    @Override
    public void store(MethodVisitor mv, short index) {
        mv.visitVarInsn(ASTORE, index);
    }

    @Override
    public void load(MethodVisitor mv, short index) {
        mv.visitVarInsn(ALOAD, index);
    }

    @Override
    public void yield(MethodVisitor mv) {
    }

    @Override
    public void aload(MethodVisitor mv) {
        throw new  RuntimeException("Not implemented");
    }

    @Override
    public void astore(MethodVisitor mv) {
        throw new  RuntimeException("Not implemented");
    }


    private void loadElement(MethodVisitor mv, int index) {
        if (child instanceof PrimitiveType<?> type) {
            new IntegerType().postack(mv, index);
            type.aload(mv);
        }

        throw new RuntimeException("This type is not acceptable.");
    }

    @Override
    public short getSize() {
        return 1;
    }
}
