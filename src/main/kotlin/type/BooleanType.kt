package com.danyazero.type

import com.danyazero.model.PrimitiveType
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class BooleanType : PrimitiveType<Boolean> {

    override fun negate(mv: MethodVisitor) {
    }

    override fun getType(): Int {
        return 1
    }

    override fun postack(mv: MethodVisitor, value: Boolean) {
        if (value) {
            mv.visitInsn(Opcodes.ICONST_1)
        } else {
            mv.visitInsn(Opcodes.ICONST_0)
        }
    }

    override fun store(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ISTORE, index.toInt())
    }

    override fun load(mv: MethodVisitor, index: Short) {
        mv.visitVarInsn(Opcodes.ILOAD, index.toInt())
    }

    override fun yield(mv: MethodVisitor) {
    }

    override fun aload(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IALOAD)
    }

    override fun astore(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IASTORE)
    }

    override fun equal(mv: MethodVisitor, jumpTarget: Label) {
        mv.visitJumpInsn(Opcodes.IF_ICMPEQ, jumpTarget)
    }

    override fun getSize(): Short {
        return 0
    }
}