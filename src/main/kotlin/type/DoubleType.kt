package com.danyazero.type

import com.danyazero.model.NumberType
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class DoubleType : NumberType<Double> {

    override fun add(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.DADD)
    }

    override fun sub(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.DSUB)
    }

    override fun mul(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.DMUL)
    }

    override fun div(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.DDIV)
    }

    override fun xor(mv: MethodVisitor) {
        throw RuntimeException("bad operand types for binary operator XOR")
    }

    override fun mod(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.DREM)
    }

    override fun ushr(mv: MethodVisitor) {
        throw RuntimeException("bad operand types for binary operator USHR")
    }

    override fun shr(mv: MethodVisitor) {
        throw RuntimeException("bad operand types for binary operator SHR")
    }

    override fun shl(mv: MethodVisitor) {
        throw RuntimeException("bad operand types for binary operator SHL")
    }

    override fun postack(mv: MethodVisitor, value: Double) {
        mv.visitLdcInsn(value)
    }

    override fun store(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.DSTORE, index.toInt())
    }

    override fun load(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.DLOAD, index.toInt())
    }

    override fun yield(mv: MethodVisitor) {
    }

    override fun aload(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.DALOAD)
    }

    override fun astore(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.DASTORE)
    }

    override fun equal(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitInsn(Opcodes.DCMPL)
        mv.visitJumpInsn(Opcodes.IFNE, jumpTarget)
    }


    override fun greater(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitInsn(Opcodes.DCMPL)
        mv.visitJumpInsn(Opcodes.IFLE, jumpTarget)
    }

    override fun less(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitInsn(Opcodes.DCMPG)
        mv.visitJumpInsn(Opcodes.IFGE, jumpTarget)
    }

    override fun greaterOrEqual(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitInsn(Opcodes.DCMPL)
        mv.visitJumpInsn(Opcodes.IFLT, jumpTarget)
    }

    override fun lessOrEqual(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitInsn(Opcodes.DCMPG)
        mv.visitJumpInsn(Opcodes.IFGT, jumpTarget)
    }

    override fun negate(mv: MethodVisitor) {
    }

    override fun getType(): Int {
        return Opcodes.T_DOUBLE
    }

    override fun getSize(): Short {
        return 2
    }
}