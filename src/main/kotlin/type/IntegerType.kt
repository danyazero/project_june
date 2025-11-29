package com.danyazero.type

import com.danyazero.model.NumberType
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class IntegerType : NumberType<Int> {

    override fun add(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IADD)
    }

    override fun sub(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.ISUB)
    }

    override fun mul(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IMUL)
    }

    override fun div(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IDIV)
    }

    fun inc(mv: MethodVisitor, index: Short, const: Int) {
        mv.visitIincInsn(index.toInt(), const)
    }

    override fun xor(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IXOR)
    }

    override fun mod(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IREM)
    }

    override fun ushr(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.ISHR)
    }

    override fun shr(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.ISHR)
    }

    override fun shl(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.ISHL)
    }

    override fun negate(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.INEG)
    }

    override fun postack(mv: MethodVisitor, value: Int) {
        if (value >= -1 && value <= 5) {
            when (value) {
                -1 -> mv.visitInsn(Opcodes.ICONST_M1)
                0 -> mv.visitInsn(Opcodes.ICONST_0)
                1 -> mv.visitInsn(Opcodes.ICONST_1)
                2 -> mv.visitInsn(Opcodes.ICONST_2)
                3 -> mv.visitInsn(Opcodes.ICONST_3)
                4 -> mv.visitInsn(Opcodes.ICONST_4)
                5 -> mv.visitInsn(Opcodes.ICONST_5)
            }
        } else if (value >= -128 && value <= 127) {
            mv.visitIntInsn(Opcodes.BIPUSH, value)
        } else if ((value >= -32768 && value <= 32767)) {
            mv.visitIntInsn(Opcodes.SIPUSH, value)
        } else {
            mv.visitLdcInsn(value)
        }
    }

    override fun store(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ISTORE, index.toInt())
    }

    override fun load(mv: MethodVisitor, index: Short) {
        mv.visitIntInsn(Opcodes.ILOAD, index.toInt())
    }

    override fun yield(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IRETURN)
    }

    override fun aload(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IALOAD)
    }

    override fun astore(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IASTORE)
    }

    override fun equal(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitJumpInsn(Opcodes.IF_ICMPNE, jumpTarget)
    }

    override fun greater(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitJumpInsn(Opcodes.IF_ICMPLE, jumpTarget)
    }

    override fun less(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, jumpTarget)
    }

    override fun greaterOrEqual(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitJumpInsn(Opcodes.IF_ICMPLT, jumpTarget)
    }

    override fun lessOrEqual(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitJumpInsn(Opcodes.IF_ICMPGT, jumpTarget)
    }

    override fun getSize(): Short {
        return 1
    }

    override fun getType() : Int {
        return Opcodes.T_INT
    }
}