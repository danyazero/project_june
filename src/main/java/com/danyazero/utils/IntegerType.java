package com.danyazero.utils;

import com.danyazero.model.NumberType;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class IntegerType implements NumberType<Integer> {

    @Override
    public void add(MethodVisitor mv) {
        mv.visitInsn(IADD);
    }

    @Override
    public void sub(MethodVisitor mv) {
        mv.visitInsn(ISUB);
    }

    @Override
    public void mul(MethodVisitor mv) {
        mv.visitInsn(IMUL);
    }

    @Override
    public void div(MethodVisitor mv) {
        mv.visitInsn(IDIV);
    }

    @Override
    public void negate(MethodVisitor mv) {
        mv.visitInsn(INEG);
    }

    @Override
    public int getType() {
        return T_INT;
    }

    @Override
    public void postack(MethodVisitor mv, java.lang.Integer value) {
        if (value >= -1 && value <= 5) {
            switch (value) {
                case -1 -> mv.visitInsn(ICONST_M1);
                case 0 -> mv.visitInsn(ICONST_0);
                case 1 -> mv.visitInsn(ICONST_1);
                case 2 -> mv.visitInsn(ICONST_2);
                case 3 -> mv.visitInsn(ICONST_3);
                case 4 -> mv.visitInsn(ICONST_4);
                case 5 -> mv.visitInsn(ICONST_5);
            }
        } else if (value >= -128 && value <= 127) {
            mv.visitIntInsn(BIPUSH, value);
        } else if ((value >= -32768 && value <= 32767)) {
            mv.visitIntInsn(SIPUSH, value);
        } else {
            mv.visitLdcInsn(value);
        }
    }

    @Override
    public void store(MethodVisitor mv, short index) {
        mv.visitVarInsn(ISTORE, index);
    }

    @Override
    public void load(MethodVisitor mv, short index) {
        mv.visitIntInsn(ILOAD, index);
    }

    @Override
    public void yield(MethodVisitor mv) {
        mv.visitInsn(IRETURN);
    }

    @Override
    public void aload(MethodVisitor mv) {
        mv.visitInsn(IALOAD);
    }

    @Override
    public void astore(MethodVisitor mv) {
        mv.visitInsn(IASTORE);
    }

    @Override
    public short getSize() {
        return 1;
    }
}
